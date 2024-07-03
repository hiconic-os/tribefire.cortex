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
package tribefire.cortex.initializer.support.api;

import com.braintribe.model.deployment.Module;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

/**
 * This interface provides initializer (ModulePriming) assets with convenience methods that can
 * be used to link existing external GE instances with GE instances created inside
 * cartridge initializer spaces. <br>
 * 
 * Note: provided functionality is meant to be used by cartridge initializer objects.
 * 
 */
public interface InitializerContext {

	/**
	 * Returns {@link ManagedGmSession session}.
	 */
	ManagedGmSession session();

	/**
	 * Uses {@link ManagedGmSession session} to lookup existing instances by global id.
	 */
	<T extends GenericEntity> T lookup(String globalId);

	/** Just like {@link #lookup(String)}, but throws an exception if given entity cannot be found. */
	<T extends GenericEntity> T require(String globalId);

	/** @return the {@link Module} instance corresponding to the current initializer module. */
	Module currentModule();
}
