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
package tribefire.cortex.gm_db_locking.wire.space;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.braintribe.common.concurrent.ScheduledTask;
import com.braintribe.common.concurrent.TaskScheduler;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.scope.InstanceConfiguration;

import tribefire.cortex.model.lockingdeployment.db.DbLocking;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationMorpher;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.DenotationTransformerRegistry;
import tribefire.module.api.PlatformBindIds;
import tribefire.module.wire.contract.ClusterBindersContract;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * Binds:
 * <ul>
 * <li>Deployment expert for {@link DbLocking}
 * <li>Standard {@link DenotationMorpher} from {@link DatabaseConnectionPool} to {@link DbLocking}
 * </ul>
 */
@Managed
public class GmDbLockingModuleSpace implements TribefireModuleContract {

	private static final String EDR_2_CC_LOCKING_NAME = "Db Locking";
	private static final String EDR_2_CC_LOCKING_ID = "edr2cc:db-locking";

	private static final Logger log = Logger.getLogger(GmDbLockingModuleSpace.class);

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Import
	private ClusterBindersContract clusterBinders;

	// ###############################################
	// ## . . . . . Hardwired deployables . . . . . ##
	// ###############################################

	@Override
	public void bindHardwired() {
		DenotationTransformerRegistry denotransRegistry = tfPlatform.hardwiredExperts().denotationTransformationRegistry();

		// New Locking
		denotransRegistry.registerStandardMorpher(DatabaseConnectionPool.T, DbLocking.T, this::dbConnectionPoolToDbLocking);

		denotransRegistry.registerEnricher("DbLockingEdr2ccEnricher", DbLocking.T, this::enrichDbLocking);
	}

	// Locking

	private Maybe<DbLocking> dbConnectionPoolToDbLocking(DenotationTransformationContext context, DatabaseConnectionPool pool) {
		DbLocking result = context.create(DbLocking.T);
		result.setDatabaseConnection(pool);

		return Maybe.complete(result);
	}

	private DenotationEnrichmentResult<DbLocking> enrichDbLocking(DenotationTransformationContext context, DbLocking lockManager) {
		if (!PlatformBindIds.TRIBEFIRE_LOCKING_BIND_ID.equals(context.denotationId()))
			return DenotationEnrichmentResult.nothingNowOrEver();

		StringBuilder sb = new StringBuilder();

		if (lockManager.getName() == null) {
			lockManager.setName(EDR_2_CC_LOCKING_NAME);
			sb.append(" name to [" + EDR_2_CC_LOCKING_NAME + "]");
		}

		if (lockManager.getExternalId() == null) {
			lockManager.setExternalId(EDR_2_CC_LOCKING_ID);
			sb.append(" externalId to [" + EDR_2_CC_LOCKING_ID + "]");
		}

		if (lockManager.getGlobalId() == null) {
			lockManager.setGlobalId(lockManager.getExternalId());
			sb.append(" globalId to [" + lockManager.getExternalId() + "]");
		}

		if (sb.length() == 0)
			return DenotationEnrichmentResult.nothingNowOrEver();
		else
			return DenotationEnrichmentResult.allDone(lockManager, "Configured " + sb.toString());
	}

	// ###############################################
	// ## . . . . . . . Deployables . . . . . . . . ##
	// ###############################################

	/**
	 * Binds a deployment expert for {@link DbLocking}.
	 */
	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		bindings.bind(DbLocking.T) //
				.component(clusterBinders.locking()) //
				.expertFactory(ctx -> deployLocking(ctx));
	}

	@Managed
	private com.braintribe.model.processing.locking.db.impl.DbLocking deployLocking(ExpertContext<DbLocking> ctx) {
		DbLocking deployable = ctx.getDeployable();

		int lockExpirationInSecs = resolveLockExpiration(deployable);

		com.braintribe.model.processing.locking.db.impl.DbLocking bean = new com.braintribe.model.processing.locking.db.impl.DbLocking();
		// DB
		bean.setAutoUpdateSchema(deployable.getAutoUpdateSchema());
		bean.setDataSource(resolveDataSource(ctx, deployable, deployable.getDatabaseConnection()));
		// Locking
		bean.setLockExpirationInSecs(lockExpirationInSecs);
		bean.setPollIntervalInMillies(deployable.getPollIntervalInMillis());
		// Messaging
		bean.setMessagingSessionProvider(tfPlatform.messaging().sessionProvider()::provideMessagingSession);
		bean.setTopicExpiration(deployable.getTopicExpirationInMillis());
		bean.setTopicName(tfPlatform.messaging().destinations().unlockTopicName());

		configureLockRefreshing(bean, InstanceConfiguration.currentInstance(), lockExpirationInSecs);

		return bean;
	}

	private void configureLockRefreshing(com.braintribe.model.processing.locking.db.impl.DbLocking bean, //
			InstanceConfiguration instanceConfiguration, int lockExpirationInSecs) {

		int refreshSecs = lockExpirationInSecs / 3;

		TaskScheduler scheduler = tfPlatform.worker().taskScheduler();
		ScheduledTask task = scheduler
				.scheduleAtFixedRate("db-locking-refresher", bean::refreshLockedLocks, refreshSecs, refreshSecs, TimeUnit.SECONDS).done();

		instanceConfiguration.onDestroy(() -> {
			task.cancel();
			// Await termination?
		});
	}

	private int resolveLockExpiration(DbLocking deployable) {
		int lockExpirationInSecs = deployable.getLockExpirationInSecs();
		if (lockExpirationInSecs >= 10)
			return lockExpirationInSecs;

		log.warn("DbLocking [" + deployable.getExternalId() + "] has its lockExpiration set to [" + lockExpirationInSecs
				+ "] seconds, but minimum value is 10, which will be used instead.");
		return 10;
	}

	private DataSource resolveDataSource(ExpertContext<?> ctx, Deployable deployable, DatabaseConnectionPool dbConnection) {
		if (dbConnection == null)
			throw new IllegalArgumentException("Cannot deploy " + deployable.entityType().getShortName() + "[" + deployable.getExternalId()
					+ "]. It's databaseConnection is null. Deployable: " + deployable);

		return ctx.resolve(dbConnection, DatabaseConnectionPool.T);
	}

}
