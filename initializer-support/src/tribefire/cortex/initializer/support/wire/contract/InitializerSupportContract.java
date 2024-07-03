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
package tribefire.cortex.initializer.support.wire.contract;

import com.braintribe.model.deployment.Module;
import com.braintribe.model.descriptive.HasExternalId;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.wire.api.space.WireSpace;

/**
 * This contract provides convenience methods that can be used to create GE instances and link them with existing external GE instances. <br>
 * 
 * Note: provided functionality is meant to be used by initializer spaces.
 * 
 */
public interface InitializerSupportContract extends WireSpace {

	/**
	 * Returns {@link ManagedGmSession session}.
	 */
	ManagedGmSession session();

	/**
	 * Uses {@link ManagedGmSession session} to create requested GE instance.
	 */
	<T extends GenericEntity> T create(EntityType<T> entityType);

	/**
	 * Uses {@link ManagedGmSession session} to lookup existing GE instances by global id.
	 */
	<T extends GenericEntity> T lookup(String globalId);

	/**
	 * @return the {@link Module} instance corresponding to the current initializer module if called when initializing the cortex access.
	 * 
	 * @throws UnsupportedOperationException
	 *             if invoked while initializing an access other than cortex.
	 */
	Module currentModule();

	/**
	 * Uses {@link ManagedGmSession session} to lookup existing GE instances by the external ID.
	 */
	<T extends HasExternalId> T lookupExternalId(String externalId);

	/**
	 * Returns initializer id.
	 */
	String initializerId();

	/**
	 * If entities are not {@link #create(EntityType) created} on a session, they have to be imported to ensure proper globalId management. <br/>
	 * QueryBuilders and CriterionBuilders are examples which require imports.
	 */
	<T> T importEntities(T entities);

}
