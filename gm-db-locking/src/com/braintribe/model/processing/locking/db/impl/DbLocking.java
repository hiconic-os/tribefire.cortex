// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.locking.db.impl;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

import javax.sql.DataSource;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.LifecycleAware;
import com.braintribe.cfg.Required;
import com.braintribe.exception.Exceptions;
import com.braintribe.logging.Logger;
import com.braintribe.model.messaging.Message;
import com.braintribe.model.messaging.Topic;
import com.braintribe.model.processing.lock.api.Locking;
import com.braintribe.model.processing.lock.api.ReentrableLocking;
import com.braintribe.model.processing.lock.api.ReentrableReadWriteLock;
import com.braintribe.provider.Box;
import com.braintribe.transport.messaging.api.MessageConsumer;
import com.braintribe.transport.messaging.api.MessageProducer;
import com.braintribe.transport.messaging.api.MessagingException;
import com.braintribe.transport.messaging.api.MessagingSession;
import com.braintribe.util.jdbc.JdbcTools;
import com.braintribe.util.jdbc.dialect.JdbcDialect;
import com.braintribe.utils.DigestGenerator;
import com.braintribe.utils.lcd.NullSafe;

/**
 * {@link Locking} implementation backed by an SQL database.
 * <p>
 * It uses a table called "TF_LOCKS".
 * <p>
 * For each {@link Lock}, acquired e.g. via {@link #forIdentifier(String)} (or similar methods), an entry is created with a certain expiration date.
 * This expiration date is the current time plus the configured {@link #setLockExpirationInSecs(int)}.
 * 
 * <h3>Updating expiration dates automatically</h3>
 * 
 * This expiration date should be updated automatically, ideally as a scheduled task. It should be configured externally, and the update is performed
 * by calling {@link #refreshLockedLocks()}.
 * <p>
 * Should a node fail to update the expiration date, another node will consider such entry as stale and will try to acquire the lock again.
 * <p>
 * For this reason it is advised to configure the refreshing interval significantly smaller than the lock expiration, for example one half of it.
 */
public class DbLocking implements Locking, LifecycleAware {

	private static final Logger log = Logger.getLogger(DbLocking.class);

	public static final int DEFAULT_POLL_INTERVAL_MS = 100;
	public static final int DEFAULT_LOCK_EXPIRATION_MS = 5 * 60 * 1000;

	/* package */ DataSource dataSource;

	private Supplier<MessagingSession> messagingSessionProvider;

	private int pollIntervalInMillies = DEFAULT_POLL_INTERVAL_MS;
	/* package */ int lockExpirationInMs = DEFAULT_LOCK_EXPIRATION_MS;

	private boolean autoUpdateSchema = true;

	private String topicName = "tf-unlock";
	private long topicExpiration = 5000L;
	private Topic topic;
	private MessageProducer messageProducer;

	protected boolean messagingInitialized = false;
	private MessagingSession messagingSession;

	private final DbLockRefresher refresher = new DbLockRefresher(this);

	// @formatter:off
	@Required     public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

	@Configurable public void setAutoUpdateSchema(boolean autoUpdateSchema) { this.autoUpdateSchema = autoUpdateSchema; }

	/**
	 * Determines the re-try interval to acquire a lock in case the first try wasn't successful.  
	 * <p>
	 * Default value is {@value #DEFAULT_POLL_INTERVAL_MS} 
	 */
	@Configurable public void setPollIntervalInMillies(int pollIntervalInMillies) { this.pollIntervalInMillies = pollIntervalInMillies; }
	@Configurable public void setLockExpirationInSecs(int lockExpirationInSecs) { this.lockExpirationInMs = 1000 * lockExpirationInSecs; }

	@Configurable public void setTopicExpiration(long topicExpiration) { this.topicExpiration = topicExpiration; }
	@Configurable public void setTopicName(String topicName) { this.topicName = topicName; }
	@Configurable public void setMessagingSessionProvider(Supplier<MessagingSession> messagingSessionProvider) { this.messagingSessionProvider = messagingSessionProvider; }
	// @formatter:on

	@Override
	public void postConstruct() {
		if (autoUpdateSchema)
			try {
				ensureLocksTable();
			} catch (Exception e) {
				throw new RuntimeException("Error while ensuring TF_LOCKS table used by Locking", e);
			}
	}

	private void ensureLocksTable() throws SQLException {
		Connection connection = dataSource.getConnection();
		try {
			if (!locksTableExists(connection))
				createLocksTable(connection);

		} finally {
			connection.close();
		}
	}

	private void createLocksTable(Connection connection) throws SQLException {
		log.debug("Creating table TF_LOCKS as it doesn't exist yet.");

		String sql = createTableSql();

		Statement statement = connection.createStatement();
		try {
			log.debug("Creating table with statement: " + sql);
			statement.executeUpdate(sql);
			log.debug("Successfully created table TF_LOCKS.");

		} catch (SQLException e) {
			if (!locksTableExists(connection))
				throw e;
		} finally {
			statement.close();
		}
	}

	// TABLE

	private String createTableSql() {
		JdbcDialect jdbcDialect = JdbcDialect.detectDialect(dataSource);
		String dateType = jdbcDialect.timestampType();
		String intType = jdbcDialect.intType();

		return "create table TF_LOCKS (" + //
				"id varchar(255) primary key not null, " + //
				"reentranceId varchar(255) not null, " + //
				"count " + intType + " not null, " + //
				"expires " + dateType + " not null, " + //
				"created " + dateType + " not null, " + //
				"caller varchar(255), " + //
				"machine varchar(255))";
	}

	private boolean locksTableExists(Connection connection) {
		return JdbcTools.tableExists(connection, "TF_LOCKS") != null;
	}

	@Override
	public void preDestroy() {
		if (messageProducer != null) {
			try {
				messageProducer.close();
			} catch (Exception e) {
				log.warn("error while closing message producer");
			}
		}
		if (messagingSession != null) {
			try {
				messagingSession.close();
			} catch (MessagingException e) {
				log.warn("error while closing messaging session");
			}
		}
	}

	@Override
	public ReentrableLocking withReentranceId(String reentranceId) {
		NullSafe.nonNull(reentranceId, "reentranceId");
		if (READ_LOCK_REENTRANCE_ID.equals(reentranceId))
			throw new IllegalArgumentException("reentranceId cannot be " + READ_LOCK_REENTRANCE_ID);

		return new ReentrableLocking() {
			@Override
			public ReentrableReadWriteLock forIdentifier(String id) {
				return forIdentifierAndReentranceId(id, reentranceId);
			}
		};
	}

	@Override
	public ReentrableReadWriteLock forIdentifier(String id) {
		return forIdentifierAndReentranceId(id, UUID.randomUUID().toString());
	}

	private ReentrableReadWriteLock forIdentifierAndReentranceId(String id, String reentranceId) {
		String tuncId = truncateTo240Chars(id);
		String caller = identifyCaller();
		return new DbRwLock(tuncId, reentranceId, caller);
	}

	/**
	 * Stacktrace might have many different forms, but with no proxy the earliest possible caller position is 3.
	 * 
	 * {@code
		 StackTraceElement[0]: Thread.getStackTrace()
		 StackTraceElement[1]: this.identifyCaller()
		 StackTraceElement[2]: this.forIdentifier(String)
		 StackTraceElement[?]: ?Locking.forIdentifier(String, String)
		 StackTraceElement[?]: ?Locking.forEntity(GenericEntity)
		 StackTraceElement[?]: ?tribefire.proxy.deploy.Locking.forEntity();
		 StackTraceElement[x]: >> Caller <<
	 * }
	 */
	private String identifyCaller() {
		int n = 3;

		while (true) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[n++];

			String className = stackTraceElement.getClassName();
			if (className.startsWith(Locking.class.getName()) || className.startsWith("tribefire.proxy.deploy.Locking"))
				continue;

			int lineNumber = stackTraceElement.getLineNumber();
			String methodName = stackTraceElement.getMethodName();

			int i = className.lastIndexOf(".");
			if (i > 0)
				className = className.substring(i + 1);

			String caller = className + '.' + methodName + '(' + (lineNumber >= 0 ? lineNumber : "") + ')';
			if (caller.length() <= 240)
				return caller;

			int len = caller.length();
			return caller.substring(len - 240, len);
		}
	}

	/* package */ class DbRwLock implements ReentrableReadWriteLock {
		public final String id;
		public final String caller;

		public final DistributedLock readLock;
		public final DistributedLock writeLock;

		public Timestamp created;

		public DbRwLock(String id, String reentranceId, String caller) {
			this.id = id;
			this.caller = caller;

			this.readLock = new DistributedLock(this, Locking.READ_LOCK_REENTRANCE_ID, false);
			this.writeLock = new DistributedLock(this, reentranceId, true);
		}

		// @formatter:off
		@Override public String lockId() { return id; }
		@Override public String reentranceId() { return writeLock.reentranceId; }
		@Override public Lock readLock()  { return readLock; }
		@Override public Lock writeLock() { return writeLock; }
		// @formatter:on
	}

	private String truncateTo240Chars(String id) {
		NullSafe.nonNull(id, "id");
		if (id.length() <= 240)
			return id;

		try {
			String md5 = DigestGenerator.stringDigestAsString(id, "MD5");
			return id.substring(0, 200) + "#" + md5;

		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Could not generate MD5 of lock id " + id);
		}
	}

	protected MessageConsumer listenForUnlockNotification(String id, Object monitor) {
		ensureMessagingInitialized();
		if (messagingSession != null) {
			try {
				MessageConsumer messageConsumer = messagingSession.createMessageConsumer(topic);
				messageConsumer.setMessageListener(message -> onUnlockMessage(id, monitor, message));

				return messageConsumer;

			} catch (MessagingException e) {
				log.error("error while adding message listener for a lock queue", e);
				return null;
			}
		}
		return null;
	}

	private void onUnlockMessage(String id, Object monitor, Message message) {
		Object body = message.getBody();
		if (!(body instanceof String))
			return;

		String lockId = (String) body;
		if (lockId.equals(id))
			synchronized (monitor) {
				monitor.notify();
			}
	}

	/**
	 * This method should be called periodically to refresh the locks.
	 */
	public void refreshLockedLocks() {
		refresher.refreshLockedLocks();
	}

	// ##############################################
	// ## . . . . . . . . . Lock . . . . . . . . . ##
	// ##############################################

	private class DistributedLock implements Lock {
		private final DbRwLock rwLock;
		private final String reentranceId;
		private final boolean isWriteLock;
		private boolean s_locking; // to make sure writeLock is not re-entrant

		public DistributedLock(DbRwLock rwLock, String reentranceId, boolean isWriteLock) {
			this.rwLock = rwLock;
			this.reentranceId = reentranceId;
			this.isWriteLock = isWriteLock;
			this.s_locking = false;
		}

		private String lockContext() {
			return " id " + rwLock.id + ", reentranceId " + reentranceId;
		}

		@Override
		public void lock() {
			try {
				if (tryLockMs(Long.MAX_VALUE))
					return;
			} catch (InterruptedException e) {
				// ignore as there is a special lock method that supports interruptibility
				log.debug("non interruptible lock() call internally caught an InterruptedException");
				Thread.currentThread().interrupt();
				throw new RuntimeException("Thread was interrupted, cannot lock " + lockContext(), e);
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			if (Thread.interrupted())
				throw new InterruptedException();

			if (tryLockMs(Long.MAX_VALUE))
				return;
		}

		@Override
		public boolean tryLock() {
			try {
				return tryLockMs(0);
			} catch (InterruptedException e) {
				return false;
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return tryLockMs(unit.toMillis(time));
		}

		private boolean tryLockMs(long tryMs) throws InterruptedException {
			markLocking();

			long now = System.currentTimeMillis();
			long tryUntil = now + tryMs;
			if (tryUntil < 0)
				tryUntil = Long.MAX_VALUE;

			Thread currentThread = Thread.currentThread();
			String oldThreadName = currentThread.getName();
			currentThread.setName(oldThreadName + " > waiting for lock " + rwLock.id);
			try {
				var successIndicator = new Box<Boolean>();
				while (true) {
					JdbcTools.withConnection(dataSource, true, () -> "Trying to acquire lock " + rwLock.id, connection -> {
						if (tryInsert(connection)) {
							successIndicator.value = Boolean.TRUE;
							return;
						}

						if (tryIncreaseCount(connection)) {
							successIndicator.value = Boolean.TRUE;
							return;
						}

						if (!deleteLockIfExpired(connection))
							return;

						if (tryInsert(connection)) {
							successIndicator.value = Boolean.TRUE;
							return;
						}
					});

					if (successIndicator.value != null) {
						refresher.startRefreshing(rwLock);
						return true;
					}

					long millisLeft = tryUntil - System.currentTimeMillis();
					if (millisLeft <= 0) {
						unmarkLocking();
						return false;
					}

					waitBeforeTryLockAgain(millisLeft);
				}

			} catch (InterruptedException e) {
				unmarkLocking();
				throw e;

			} catch (Exception e) {
				unmarkLocking();
				throw new RuntimeException("Could not get lock.", e);

			} finally {
				currentThread.setName(oldThreadName);
			}
		}

		// id, expires, created, caller, machine
		private boolean tryInsert(Connection c) {
			long current = System.currentTimeMillis();
			long expires = current + lockExpirationInMs;

			Timestamp currentTs = new Timestamp(current);
			Timestamp expiresTs = new Timestamp(expires);

			String query = "insert into TF_LOCKS (id, reentranceId, count, expires, created, caller, machine) values (?,?,?,?,?,?,?)";

			try {
				JdbcTools.withPreparedStatement(c, query, () -> "inserting entry for lock with id " + rwLock.id, ps -> {
					int i = 1;
					ps.setString(i++, rwLock.id);
					ps.setString(i++, reentranceId);
					ps.setInt(i++, 1); // count
					ps.setTimestamp(i++, expiresTs);
					ps.setTimestamp(i++, currentTs);
					ps.setString(i++, rwLock.caller);
					ps.setString(i++, "machine"); // TODO machine

					ps.executeUpdate();
				});

				rwLock.created = currentTs;
				return true;

			} catch (Exception e) {
				// Exception is expected here if a row already exists
				log.trace(() -> "Lock not obtained due to " + e.getClass().getSimpleName() + ""
						+ (e.getMessage() != null ? ": " + e.getMessage() : ""));

				return false;
			}
		}

		private boolean tryIncreaseCount(Connection c) {
			Timestamp created = queryCreatedTime(c);
			if (!tryChangeCount(c, created, +1))
				return false;

			rwLock.created = created;
			return true;
		}

		private boolean tryChangeCount(Connection c, Timestamp created, int diff) {
			long current = System.currentTimeMillis();
			long expires = current + lockExpirationInMs;

			Timestamp expiresTs = new Timestamp(expires);

			String query = "update TF_LOCKS set count = count + ?, expires = ? where id = ? and reentranceId = ? and created = ?";

			var updated = new Box<Integer>();
			try {
				JdbcTools.withPreparedStatement(c, query, () -> "updating count for lock with id " + rwLock.id, ps -> {
					ps.setInt(1, diff);
					ps.setTimestamp(2, expiresTs);
					ps.setString(3, rwLock.id);
					ps.setString(4, reentranceId);
					ps.setTimestamp(5, created);

					updated.value = ps.executeUpdate();
				});

				if (updated.value == 0)
					return false;

				return true;

			} catch (Exception e) {
				log.debug(() -> "Lock not obtained due to " + e.getClass().getSimpleName() + ""
						+ (e.getMessage() != null ? ": " + e.getMessage() : ""));

				return false;
			}
		}

		private Timestamp queryCreatedTime(Connection c) {
			String query = "select created from TF_LOCKS where id = ? and reentranceId = ?";
			List<Object> params = asList(rwLock.id, reentranceId);

			var result = new Box<Timestamp>();
			JdbcTools.withPreparedStatement(c, query, params, () -> "Querying lock if expired with id " + rwLock.id, ps -> {
				ps.setString(1, rwLock.id);
				ps.setString(2, reentranceId);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						result.value = rs.getTimestamp(1);
					}
				}
			});

			return result.value;
		}

		private void waitBeforeTryLockAgain(long millisLeft) throws InterruptedException {
			// TODO use CountDownLatch instead? This could not-catch a notification
			Object monitor = new Object();

			MessageConsumer consumer = listenForUnlockNotification(rwLock.id, monitor);

			try {
				long waitMillies = Math.min(millisLeft, pollIntervalInMillies);

				synchronized (monitor) {
					monitor.wait(waitMillies);
				}

			} finally {
				if (consumer != null) {
					try {
						consumer.close();
					} catch (MessagingException e) {
						log.error("error while closing message consumer", e);
					}
				}
			}
		}
		@Override
		public void unlock() {
			refresher.stopRefreshing(rwLock);

			try {
				JdbcTools.withConnection(dataSource, true, () -> "Deleting unlocked lock " + rwLock.id, connection -> {
					if (tryChangeCount(connection, rwLock.created, -1))
						deleteLockIfExpired(connection);
				});

				notifyUnlock(rwLock.id);
				return;

			} catch (Exception e) {
				log.warn("Error while releasing lock " + rwLock.id, e);
			} finally {
				unmarkLocking();
			}
		}

		protected boolean deleteLockIfExpired(Connection c) throws Exception {
			Timestamp curentTs = new Timestamp(System.currentTimeMillis());

			var deleted = new Box<Integer>();

			String query = "delete from TF_LOCKS where id = ? and (expires < ? or count = 0)";
			List<Object> params = asList(rwLock.id, curentTs);

			JdbcTools.withPreparedStatement(c, query, params, () -> "Deleting expired lock " + rwLock.id, ps -> {
				ps.setString(1, rwLock.id);
				ps.setTimestamp(2, curentTs);

				deleted.value = ps.executeUpdate();
			});

			return deleted.value > 0;
		}

		private void markLocking() {
			if (!isWriteLock)
				return;

			synchronized (this) {
				if (s_locking)
					throw new IllegalStateException("Cannot try lock " + lockContext() + " instance, it is already locked.");
				s_locking = true;
			}
		}

		private void unmarkLocking() {
			if (!isWriteLock)
				return;

			// Is this needed to be sure that another thread who enters markLocking sees this?
			synchronized (this) {
				s_locking = false;
			}
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException("newCondition not supported by this implementation");
		}

	}

	private void notifyUnlock(String id) {
		ensureMessagingInitialized();
		if (messagingSession != null && messageProducer != null) {
			try {
				Message message = messagingSession.createMessage();
				message.setBody(id);
				message.setTimeToLive(topicExpiration);

				messageProducer.sendMessage(message);

			} catch (MessagingException e) {
				log.error("error while producing message for a lock", e);
			}
		}
	}

	private void ensureMessagingInitialized() {
		if (messagingInitialized)
			return;

		messagingInitialized = true;

		if (messagingSessionProvider == null)
			return;

		try {
			messagingSession = messagingSessionProvider.get();
			topic = messagingSession.createTopic(topicName);
			messageProducer = messagingSession.createMessageProducer(topic);

		} catch (MessagingException e) {
			log.error("error while retrieving messaging components", e);
		}
	}

}
