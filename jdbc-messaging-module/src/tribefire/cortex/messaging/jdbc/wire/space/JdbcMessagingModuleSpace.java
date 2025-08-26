
package tribefire.cortex.messaging.jdbc.wire.space;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.braintribe.common.concurrent.ScheduledTask;
import com.braintribe.common.concurrent.TaskScheduler;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.messaging.jdbc.JdbcConnectionProvider;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.scope.InstanceConfiguration;

import tribefire.cortex.model.deployment.messaging.postgres.model.JdbcMessaging;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.DenotationTransformerRegistry;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class JdbcMessagingModuleSpace implements TribefireModuleContract {

	public static final String globalIdPrefix = "edr2cc:jdbc-msg:";

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Override
	public void bindHardwired() {
		DenotationTransformerRegistry registry = tfPlatform.hardwiredExperts().denotationTransformationRegistry();

		registry.registerStandardMorpher(DatabaseConnectionPool.T, JdbcMessaging.T, this::dbConnection_To_JdbcMessaging);
	}

	private Maybe<JdbcMessaging> dbConnection_To_JdbcMessaging(DenotationTransformationContext context, DatabaseConnectionPool connectionPool) {
		JdbcMessaging deployable = context.create(JdbcMessaging.T);
		deployable.setGlobalId(globalIdPrefix + context.denotationId());
		deployable.setExternalId(deployable.getGlobalId());

		deployable.setName("JDBC Messaging");
		deployable.setConnectionPool(connectionPool);

		return Maybe.complete(deployable);
	}

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		bindings.bind(JdbcMessaging.T) //
				.component(tfPlatform.binders().messaging()) //
				.expertFactory(ctx -> deployJdbcMessaging(ctx));
	}

	@Managed
	private JdbcConnectionProvider deployJdbcMessaging(ExpertContext<JdbcMessaging> ctx) {
		JdbcMessaging deployable = ctx.getDeployable();

		DataSource dataSource = ctx.resolve(deployable.getConnectionPool(), DatabaseConnectionPool.T);

		JdbcConnectionProvider bean = new JdbcConnectionProvider();
		bean.setName(deployable.getName());
		bean.setSqlPrefix(deployable.getSqlPrefix());
		bean.setDataSource(dataSource);
		bean.setMessagingContext(tfPlatform.messaging().context());

		configureExpiredMessagesDeleting(bean, InstanceConfiguration.currentInstance());

		return bean;
	}

	private void configureExpiredMessagesDeleting(JdbcConnectionProvider bean, InstanceConfiguration instanceConfiguration) {
		int refreshHour = 1;

		TaskScheduler scheduler = tfPlatform.worker().taskScheduler();
		ScheduledTask task = scheduler
				.scheduleAtFixedRate("jdbc-messaging-expired-deleting", bean::deleteExpiredMessages, refreshHour, refreshHour, TimeUnit.HOURS).done();

		instanceConfiguration.onDestroy(() -> {
			task.cancel();
			// Await termination?
		});
	}

}
