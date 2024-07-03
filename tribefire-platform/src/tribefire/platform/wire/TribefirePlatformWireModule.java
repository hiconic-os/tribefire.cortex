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
package tribefire.platform.wire;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.braintribe.cfg.ScopeContext;
import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.wire.api.context.ScopeContextHolders;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.cortex.module.loading.PropertyContractResolver;
import tribefire.platform.wire.contract.MainTribefireContract;
import tribefire.platform.wire.space.MainTribefireSpace;

/**
 * @author peter.gazdik
 */
public enum TribefirePlatformWireModule implements WireTerminalModule<MainTribefireContract>  {
	INSTANCE;

	private final Map<ScopeContext, ScopeContextHolders> deploymentScopeContextHolders = new ConcurrentHashMap<>();

	@Override
	public void configureContext(WireContextBuilder<?> contextBuilder) {
		contextBuilder.bindContracts(new PropertyContractResolver());
		contextBuilder.bindContracts(MainTribefireSpace.class);
		contextBuilder.shareScopeContexts(getShareScopeContextsExpert());
	}

	public Function<ScopeContext, Map<ScopeContext, ScopeContextHolders>> getShareScopeContextsExpert() {
		return this::getScopeContextHoldersMap;
	}

	private Map<ScopeContext, ScopeContextHolders> getScopeContextHoldersMap(ScopeContext scopeContext) {
		if (scopeContext instanceof ExpertContext<?>) {
			return deploymentScopeContextHolders;
		} else {
			return null;
		}
	}
}
