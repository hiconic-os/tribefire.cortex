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
package tribefire.cortex.asset.resolving.ng.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;

import tribefire.cortex.asset.resolving.ng.api.PlatformAssetResolvingContext;

public abstract class AbstractPlatformAssetResolvingContext implements PlatformAssetResolvingContext {
	
	private ManagedGmSession session = new BasicManagedGmSession();
	private Map<Class<?>, Object> sharedInfos = new HashMap<>();

	@Override
	public ManagedGmSession session() {
		return session;
	}
	
	@Override
	public <C> C findSharedInfo(Class<C> key) {
		return (C) sharedInfos.get(key);
	}
	
	@Override
	public <C> C getSharedInfo(Class<C> key, Supplier<C> supplier) {
		return (C) sharedInfos.computeIfAbsent(key, k -> supplier.get());
	}
}
