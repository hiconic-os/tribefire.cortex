// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.processing.platformreflection.db;

import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.DeploymentStatus;
import com.braintribe.model.deployment.database.connector.DatabaseConnectionDescriptor;
import com.braintribe.model.deployment.database.pool.ConfiguredDatabaseConnectionPool;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.platformreflection.db.DatabaseConnectionInfo;
import com.braintribe.model.platformreflection.db.DatabaseConnectionPoolMetrics;
import com.braintribe.model.platformreflection.db.DatabaseInformation;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.utils.StringTools;

public class StandardDatabaseInformationProvider implements DatabaseInformationProvider {

	private static Logger logger = Logger.getLogger(StandardDatabaseInformationProvider.class);

	private Supplier<PersistenceGmSession> cortexSessionSupplier;

	@Required
	public void setCortexSessionSupplier(Supplier<PersistenceGmSession> cortexSessionSupplier) {
		this.cortexSessionSupplier = cortexSessionSupplier;
	}

	@Override
	public DatabaseInformation get() {
		logger.debug(() -> "Compiling database information.");

		try {
			DatabaseInformation result = DatabaseInformation.T.create();
			List<DatabaseConnectionInfo> cps = collectHikariCpConnectionPools();
			result.getConnectionPools().addAll(cps);

			List<DatabaseConnectionInfo> combined = combineConnectionPoolInfos(cps);
			result.getCombinedConnectionInfo().addAll(combined);

			return result;

		} finally {
			logger.debug(() -> "Done with database information.");
		}
	}

	private List<DatabaseConnectionInfo> collectHikariCpConnectionPools() {
		try {
			List<DatabaseConnectionPool> list = queryConnectionPools();

			return list.stream() //
					.map(this::toDbConnectionInfo) //
					.collect(Collectors.toList());

		} catch (Exception e) {
			logger.debug(() -> "Error while trying to retrieve DB connectors from Cortex.", e);
			return emptyList();
		}
	}

	private List<DatabaseConnectionPool> queryConnectionPools() {
		PersistenceGmSession session = cortexSessionSupplier.get();
		EntityQuery query = EntityQueryBuilder.from(DatabaseConnectionPool.T).where().property(Deployable.deploymentStatus)
				.eq(DeploymentStatus.deployed).done();

		return session.query().entities(query).list();
	}

	private DatabaseConnectionInfo toDbConnectionInfo(DatabaseConnectionPool cp) {
		DatabaseConnectionInfo result = DatabaseConnectionInfo.T.create();
		result.setName(cp.getExternalId());
		result.setConnectionDescription(descriptionFor(cp));

		return result;
	}

	private String descriptionFor(DatabaseConnectionPool cp) {
		if (!(cp instanceof ConfiguredDatabaseConnectionPool))
			return null;

		DatabaseConnectionDescriptor desc = ((ConfiguredDatabaseConnectionPool) cp).getConnectionDescriptor();
		if (desc == null)
			return null;

		return desc.describeConnection();
	}

	private List<DatabaseConnectionInfo> combineConnectionPoolInfos(List<DatabaseConnectionInfo> cps) {
		TreeMap<String, DatabaseConnectionInfo> map = new TreeMap<>();

		for (DatabaseConnectionInfo dcp : cps) {
			String key = createMapKeyFromConnectionPool(dcp);
			DatabaseConnectionInfo entry = map.get(key);
			if (entry == null) {
				entry = DatabaseConnectionInfo.T.create();
				entry.setName(key);
				entry.setConnectionDescription(dcp.getName());
				entry.setMaximumPoolSize(dcp.getMaximumPoolSize());
				entry.setMinimumPoolSize(dcp.getMinimumPoolSize());
				DatabaseConnectionPoolMetrics entryMetrics = DatabaseConnectionPoolMetrics.T.create();
				entry.setMetrics(entryMetrics);

				DatabaseConnectionPoolMetrics dcpMetrics = dcp.getMetrics();
				if (dcpMetrics != null) {
					entryMetrics.setActiveConnections(dcpMetrics.getActiveConnections());
					entryMetrics.setIdleConnections(dcpMetrics.getIdleConnections());
					entryMetrics.setLeaseCount(dcpMetrics.getLeaseCount());
					entryMetrics.setThreadsAwaitingConnections(dcpMetrics.getThreadsAwaitingConnections());
					entryMetrics.setTotalConnections(dcpMetrics.getTotalConnections());
				}
				map.put(key, entry);
			} else {
				String oldDesc = entry.getConnectionDescription();
				if (StringTools.isBlank(oldDesc)) {
					entry.setConnectionDescription(dcp.getName());
				} else {
					entry.setConnectionDescription(oldDesc + ", " + dcp.getName());
				}

				entry.setMaximumPoolSize(addIntegers(dcp.getMaximumPoolSize(), entry.getMaximumPoolSize()));
				entry.setMinimumPoolSize(addIntegers(entry.getMinimumPoolSize(), entry.getMinimumPoolSize()));
				DatabaseConnectionPoolMetrics dcpMetrics = dcp.getMetrics();
				if (dcpMetrics != null) {
					DatabaseConnectionPoolMetrics entryMetrics = entry.getMetrics();

					entryMetrics.setActiveConnections(addIntegers(dcpMetrics.getActiveConnections(), entryMetrics.getActiveConnections()));
					entryMetrics.setIdleConnections(addIntegers(dcpMetrics.getIdleConnections(), entryMetrics.getIdleConnections()));
					entryMetrics.setLeaseCount(addLongs(dcpMetrics.getLeaseCount(), entryMetrics.getLeaseCount()));
					entryMetrics.setThreadsAwaitingConnections(
							addIntegers(dcpMetrics.getThreadsAwaitingConnections(), entryMetrics.getThreadsAwaitingConnections()));
					entryMetrics.setTotalConnections(addIntegers(dcpMetrics.getTotalConnections(), entryMetrics.getTotalConnections()));
				}
			}
		}

		return newList(map.values());
	}

	private Integer addIntegers(Integer i1, Integer i2) {
		if (i1 == null)
			return i2;
		if (i2 == null)
			return i1;
		return i1 + i2;
	}

	private Long addLongs(Long l1, Long l2) {
		if (l1 == null)
			return l2;
		if (l2 == null)
			return l1;
		return l1 + l2;
	}

	private static String createMapKeyFromConnectionPool(DatabaseConnectionInfo dcp) {
		String key = dcp.getName();
		String desc = dcp.getConnectionDescription();
		if (!StringTools.isBlank(desc) && desc.startsWith("jdbc:")) {
			try {
				String cleanURI = desc.substring(5);

				URI uri = URI.create(cleanURI);

				StringBuilder sb = new StringBuilder();
				sb.append(uri.getHost());
				int port = uri.getPort();
				if (port != -1) {
					sb.append(":");
					sb.append(port);
				}
				key = sb.toString();
			} catch (Exception e) {
				logger.debug(() -> "Could not parse JDBC URL " + desc, e);
			}
		}
		return key;
	}
}
