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

import java.util.List;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * <p>
 * A registry of deployed units for individual instances of {@link Deployable}
 * 
 * @author dirk.scheffler
 */
public interface DeployRegistry {

	/**
	 * Equivalent to {@code resolve(deployable.getExternalId)}
	 * 
	 * @see #resolve(String)
	 */
	DeployedUnit resolve(Deployable deployable);

	/**
	 * Resolves {@link DeployedUnit} for the given {@link Deployable#getExternalId() Deployable's externalId}, or <tt>null</tt> if no unit is found
	 * (i.e. no Deployable with such externalId was deployed.).
	 */
	DeployedUnit resolve(String externalId);

	/**
	 * Equivalent to {@code resolve(deployable.getExternalId, componentType)}
	 * 
	 * @see #resolve(String, EntityType)
	 */
	<E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType);

	/**
	 * Resolves an expert that was deployed for given {@link Deployable#getExternalId() Deployable's externalId} as given deployable component.
	 * <p>
	 * NOTE the resolved instance could become out-dated if given {@link Deployable} is re-deployed. For a re-deployment safe resolution there is a
	 * "proxying" implementation of {@link DeployedComponentResolver}.
	 * 
	 * @see DeployedComponentResolver#resolve(String, EntityType)
	 */
	<E> E resolve(String externalId, EntityType<? extends Deployable> componentType);

	/**
	 * <p>
	 * Returns the {@link Deployable}(s) in the order they were deployed
	 * 
	 * @return The {@link Deployable}(s) in the order they were deployed
	 */
	List<Deployable> getDeployables();

	void addListener(DeployRegistryListener listener);

	void removeListener(DeployRegistryListener listener);

	boolean isDeployed(Deployable deployable);

	boolean isDeployed(String externalId);

}
