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
package tribefire.cortex.initializer.support.impl;

import static java.util.Objects.requireNonNull;

import com.braintribe.model.deployment.Module;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.api.WiredInitializerContext;
import tribefire.module.wire.contract.ModuleReflectionContract;

public class InitializerContextImpl<S extends WireSpace> implements WiredInitializerContext<S> {
	
	private final ManagedGmSession session;
	private final WireContext<S> wireContext;
	
	private Module currentModule; // caching because lookup does a read-lock synchronization

	public InitializerContextImpl(ManagedGmSession session, WireContext<S> wireContext) {
		this.session = session;
		this.wireContext = wireContext;
	}

	@Override
	public ManagedGmSession session() {
		return session;
	}

	@Override
	public Module currentModule() {
		if (currentModule == null) {
			ModuleReflectionContract moduleReflection = wireContext.contract(ModuleReflectionContract.class);
			currentModule = require(moduleReflection.globalId());
		}

		return currentModule;
	}

	@Override
	public <T extends GenericEntity> T require(String globalId) {
		return requireNonNull(lookup(globalId), () -> "No entity found with globalId: " + globalId);
	}

	@Override
	public <T extends GenericEntity> T lookup(String globalId) {
		return session.findEntityByGlobalId(globalId);
	}

	@Override
	public S contract() {
		return wireContext.contract();
	}

	@Override
	public WireContext<S> wireContext() {
		return wireContext;
	}

	@Override
	public <C extends WireSpace> C contract(Class<C> wireContractClass) {
		return wireContext.contract(wireContractClass);
	}

}
