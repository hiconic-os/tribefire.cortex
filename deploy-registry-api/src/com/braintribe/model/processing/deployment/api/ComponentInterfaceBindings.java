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

import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * <p>
 * Offers access to the component interfaces of bound component types.
 * 
 * @author dirk.scheffler
 */
public interface ComponentInterfaceBindings {

	static final Logger log = Logger.getLogger(ComponentInterfaceBindings.class);

	/**
	 * <p>
	 * Returns the component interfaces for the given component type.
	 * 
	 * <p>
	 * {@code null} is returned if no interface is bound to the given type.
	 * 
	 * @param componentType
	 *            The component type for which component interfaces are to be retrieved.
	 * @return The component interfaces for the given component type or {@code null} if no interface is bound to the
	 *         given type.
	 */
	Class<?>[] findComponentInterfaces(EntityType<? extends Deployable> componentType);

	/**
	 * <p>
	 * Returns the component interfaces for the given component type.
	 * 
	 * <p>
	 * {@link DeploymentException} is thrown if no interface is bound to the given type.
	 * 
	 * @param componentType
	 *            The component type for which component interfaces are to be retrieved.
	 * @return The component interfaces for the given component type.
	 * @throws DeploymentException
	 *             If no interface is bound to the given type
	 */
	Class<?>[] getComponentInterfaces(EntityType<? extends Deployable> componentType) throws DeploymentException;
}
