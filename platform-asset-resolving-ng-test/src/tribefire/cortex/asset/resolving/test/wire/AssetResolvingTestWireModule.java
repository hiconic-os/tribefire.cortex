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
package tribefire.cortex.asset.resolving.test.wire;

import static org.assertj.core.util.Lists.list;

import java.util.List;

import com.braintribe.devrock.mc.core.wirings.configuration.contract.RepositoryConfigurationContract;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.cortex.asset.resolving.ng.wire.AssetResolverWireModule;
import tribefire.cortex.asset.resolving.test.wire.contract.AssetResolvingTestContract;
import tribefire.cortex.asset.resolving.test.wire.space.RepoConfigSpace;

public enum AssetResolvingTestWireModule implements WireTerminalModule<AssetResolvingTestContract> {
	INSTANCE;
	
	@Override
	public List<WireModule> dependencies() {
		return list(AssetResolverWireModule.INSTANCE);
	}
	
	@Override
	public void configureContext(WireContextBuilder<?> contextBuilder) {
		WireTerminalModule.super.configureContext(contextBuilder);
		
		contextBuilder.bindContract(RepositoryConfigurationContract.class, RepoConfigSpace.class);
	}
}
