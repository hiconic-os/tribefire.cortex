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
package tribefire.cortex.asset.resolving.wire;

import static com.braintribe.wire.api.util.Lists.list;

import java.util.List;

import com.braintribe.build.artifacts.mc.wire.buildwalk.BuildDependencyResolverWireModule;
import com.braintribe.build.artifacts.mc.wire.buildwalk.contract.BuildDependencyResolutionContract;
import com.braintribe.build.artifacts.mc.wire.buildwalk.contract.FilterConfigurationContract;
import com.braintribe.build.artifacts.mc.wire.buildwalk.contract.GeneralConfigurationContract;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.cortex.asset.resolving.wire.space.GeneralConfigurationSpace;

public class PlatformAssetResolvingWireModule implements WireTerminalModule<BuildDependencyResolutionContract> {
	private FilterConfigurationContract filterConfigurationContract;
	private WireModule integrationModule;

	public PlatformAssetResolvingWireModule(FilterConfigurationContract filterConfigurationContract, WireModule integrationModule) {
		this.filterConfigurationContract = filterConfigurationContract;
		this.integrationModule = integrationModule;
	}
	
	@Override
	public List<WireModule> dependencies() {
		return list(BuildDependencyResolverWireModule.DEFAULT, integrationModule);
	}
	
	@Override
	public void configureContext(WireContextBuilder<?> contextBuilder) {
		WireTerminalModule.super.configureContext(contextBuilder);
		contextBuilder.bindContract(FilterConfigurationContract.class, filterConfigurationContract);
		contextBuilder.bindContract(GeneralConfigurationContract.class, GeneralConfigurationSpace.class);
	}

	@Override
	public Class<BuildDependencyResolutionContract> contract() {
		return BuildDependencyResolutionContract.class;
	}
}
