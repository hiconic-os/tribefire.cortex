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
package tribefire.cortex.asset.resolving.ng.api;

import java.util.function.Supplier;

import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.ve.api.VirtualEnvironment;

public interface PlatformAssetResolvingContext extends DependencySelectorContext {
	ManagedGmSession session();

	/**
	 * <p>
	 * Acquires shared information data of a given type. If an instance for that type was already created it is returned
	 * otherwise the <code>supplier</code> is used to create such an instance which is then registered and returned.
	 * 
	 * <p>
	 * Shared information can be used to share data between individual calls to the same or different SOC experts. in
	 * order to aggregate and share information through the whole processing and to support comprehensive operations
	 * that close over the scope of a the single calls.
	 */
	<C> C getSharedInfo(Class<C> key, Supplier<C> supplier);

	/**
	 * This method looks up shared information and returns an instance if existing and otherwise null.
	 * 
	 * @see #getSharedInfo(Class, Supplier)
	 */
	<C> C findSharedInfo(Class<C> key);

	VirtualEnvironment getVirtualEnvironment();
}
