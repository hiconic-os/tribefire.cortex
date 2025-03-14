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
package com.braintribe.model.processing.deployment.api;

import java.util.Optional;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * <p>
 * Offers access to the deployed components during deployment-time.
 * 
 * @author dirk.scheffler
 */
public interface DeployedComponentResolver {

	/**
	 * Equivalent to {@code resolve(deployable.getExternalId, componentType)}
	 * 
	 * @see #resolve(String, EntityType)
	 */
	<E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType);

	/**
	 * Resolves an expert that was deployed for given {@link Deployable#getExternalId() Deployable's externalId} as given deployable component.
	 */
	<E> E resolve(String externalId, EntityType<? extends Deployable> componentType);

	<E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType, Class<E> expertInterface, E defaultDelegate);

	<E> E resolve(String externalId, EntityType<? extends Deployable> componentType, Class<E> expertInterface, E defaultDelegate);

	<E> Optional<ResolvedComponent<E>> resolveOptional(String externalId, EntityType<? extends Deployable> componentType, Class<E> expertInterface);
}
