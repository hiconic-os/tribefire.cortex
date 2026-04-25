// ============================================================================
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
package tribefire.platform.wire.space.common;

import javax.sql.DataSource;

import com.braintribe.gm.initializer.api.InitializerRegistry;
import com.braintribe.gm.initializer.jdbc.processing.AbstractInitializerManager;
import com.braintribe.gm.initializer.jdbc.processing.FileSystemInitializerManager;
import com.braintribe.gm.initializer.jdbc.processing.GmDbInitializerManager;
import com.braintribe.gm.initializer.model.configuration.InitializerDbConfiguration;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.MasterResourcesSpace;
import tribefire.platform.wire.space.cortex.deployment.DeploymentSpace;
import tribefire.platform.wire.space.cortex.services.ClusterSpace;
import tribefire.platform.wire.space.module.WebPlatformReflectionSpace;

@Managed
public class InitializerManagerSpace implements WireSpace {

	@Import
	private WebPlatformReflectionSpace platformReflection;

	@Import
	private MasterResourcesSpace resources;

	@Import
	private ClusterSpace cluster;

	@Import
	private DeploymentSpace deployment;

	public void runInitialization() {
		initializerManager().runInitializers();
	}

	public InitializerRegistry initializerRegistry() {
		return initializerManager();
	}

	@Managed
	private AbstractInitializerManager initializerManager() {
		AbstractInitializerManager bean = newInitializerRegistry();
		bean.setUseCase("platform-initializers");
		
		return bean;
	}
	
	private AbstractInitializerManager newInitializerRegistry() {
		InitializerDbConfiguration initConfiguration = configuration();
		if (!isEmpty(initConfiguration))
			return newDbRegistry(initConfiguration);
		else
			return newFsRegistry();
	}

	private boolean isEmpty(InitializerDbConfiguration c) {
		return c.getDatabaseId() == null;
	}

	private GmDbInitializerManager newDbRegistry(InitializerDbConfiguration initConfiguration) {
		GmDbInitializerManager bean = new GmDbInitializerManager();
		bean.setNodeId(platformReflection.nodeId());
		bean.setDataSource(resolveDataSource(initConfiguration));
		bean.setTasksTableName(initConfiguration.getTasksTableName());
		bean.setLocking(cluster.locking());
		return bean;
	}

	private DataSource resolveDataSource(InitializerDbConfiguration initConfiguration) {
		String externalId = initConfiguration.getDatabaseId();
		DataSource dataSource = deployment.registry().resolve(externalId, DatabaseConnectionPool.T);
		if (dataSource == null)
			throw new IllegalArgumentException("Cannot deploy GmDbInitializerManager. " + "No deployed DatabaseConnectionPool found for id ["
					+ externalId + "]. Configuration entity: [" + InitializerDbConfiguration.T.getShortName() + "]");

		return dataSource;
	}

	private FileSystemInitializerManager newFsRegistry() {
		FileSystemInitializerManager bean = new FileSystemInitializerManager();
		bean.setTasksPropertiesFile(resources.storage(".").asPath().resolve("initializers/platform-initializers.properties").toFile());
		return bean;
	}

	private InitializerDbConfiguration configuration() {
		return platformReflection.readConfig(InitializerDbConfiguration.T).get();
	}

}
