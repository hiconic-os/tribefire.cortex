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
package tribefire.cortex.module.loading;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.braintribe.cfg.ScopeContext;
import com.braintribe.wire.api.context.ScopeContextHolders;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;
import com.braintribe.wire.api.space.ContractSpaceResolver;

import tribefire.module.wire.contract.TribefireModuleContract;

/**
 * This {@link WireModule} is used to load the wire configuration of a tribefire module.
 * 
 * @see ModuleLoader
 * 
 * @author peter.gazdik
 */
public class ModuleLoadingWireModule implements WireTerminalModule<TribefireModuleContract> {

	private final WireModule tfModuleWireModule;
	private final ModuleContractsRegistry contractsRegistry;
	private final ContractSpaceResolver propertyContractResolver;
	private final Function<ScopeContext, Map<ScopeContext, ScopeContextHolders>> shareScopeContextsExpert;

	public ModuleLoadingWireModule(ModuleContractsRegistry contractsRegistry, WireModule tfModuleWireModule,
			ContractSpaceResolver propertyContractResolver, Function<ScopeContext, Map<ScopeContext, ScopeContextHolders>> shareScopeContextsExpert) {

		this.tfModuleWireModule = tfModuleWireModule;
		this.contractsRegistry = contractsRegistry;
		this.propertyContractResolver = propertyContractResolver;
		this.shareScopeContextsExpert = shareScopeContextsExpert;
	}

	@Override
	public Class<TribefireModuleContract> contract() {
		return TribefireModuleContract.class;
	}

	@Override
	public void configureContext(WireContextBuilder<?> contextBuilder) {
		contextBuilder.loadSpacesFrom(tfModuleWireModule.getClass().getClassLoader());
		contextBuilder.bindContracts(propertyContractResolver);
		contextBuilder.shareScopeContexts(shareScopeContextsExpert);

		contractsRegistry.bindContracts(contextBuilder);
	}

	@Override
	public List<WireModule> dependencies() {
		return asList(tfModuleWireModule);
	}

}
