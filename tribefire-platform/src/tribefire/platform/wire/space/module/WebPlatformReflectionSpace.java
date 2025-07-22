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
package tribefire.platform.wire.space.module;

import java.util.List;

import com.braintribe.gm.config.yaml.ModeledYamlConfiguration;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.api.EnvironmentDenotations;
import tribefire.module.wire.contract.ModuleReflectionContract;
import tribefire.module.wire.contract.WebPlatformReflectionContract;
import tribefire.platform.wire.space.MasterResourcesSpace;
import tribefire.platform.wire.space.common.CartridgeInformationSpace;
import tribefire.platform.wire.space.common.EnvironmentSpace;

/**
 * @author peter.gazdik
 */
@Managed
public class WebPlatformReflectionSpace implements WebPlatformReflectionContract {

	@Import
	private CartridgeInformationSpace cartridgeInformation;

	@Import
	private EnvironmentSpace environment;

	@Import
	private ModuleInitializationSpace moduleInitialization;
	
	@Import
	private MasterResourcesSpace masterResources;

	@Override
	public InstanceId instanceId() {
		return cartridgeInformation.instanceId();
	}

	@Override
	public String globalId() {
		throw new UnsupportedOperationException("Method 'WebPlatformReflectionSpace.globalId' is not implemented yet!");
	}

	@Managed
	@Override
	public String nodeId() {
		return cartridgeInformation.nodeId();
	}

	@Override
	public String getProperty(String propertyName) {
		return TribefireRuntime.getProperty(propertyName);
	}

	@Override
	public EnvironmentDenotations environmentDenotations() {
		return environment.environmentDenotations();
	}

	@Override
	public List<ModuleReflectionContract> modules() {
		return moduleInitialization.moduleLoader().moduleReflectionContracts();
	}
	
	@Override
	public <C extends GenericEntity> Maybe<C> readConfig(EntityType<C> configType) {
		return modeledConfiguration().configReasoned(configType);
	}
	
	@Managed
	private ModeledYamlConfiguration modeledConfiguration() {
		ModeledYamlConfiguration bean = new ModeledYamlConfiguration();
		bean.setConfigFolder(masterResources.confPath().toFile());
		return bean;
	}
}
