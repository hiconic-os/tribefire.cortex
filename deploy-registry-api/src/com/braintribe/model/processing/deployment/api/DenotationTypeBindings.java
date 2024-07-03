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

import java.util.Set;
import java.util.function.Function;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * <p>
 * Offers access to the {@link DeployedUnit} of bound denotation types and the {@link ComponentBinding} of bound
 * component types.
 * 
 * @author dirk.scheffler
 */
public interface DenotationTypeBindings extends ComponentInterfaceBindings {

	/**
	 * <p>
	 * Resolves a {@link DeployedUnit} supplier can be resolved for the given {@link Deployable}.
	 * 
	 * @param deployable
	 *            The {@link Deployable} for which a {@link DeployedUnit} supplier must be resolved.
	 * @return A {@link DeployedUnit} supplier can be resolved for the given {@link Deployable}.
	 * @throws DeploymentException
	 *             If no DeployedUnit supplier can be resolved for the given {@link Deployable}.
	 */
	Function<MutableDeploymentContext<?, ?>, DeployedUnit> resolveDeployedUnitSupplier(Deployable deployable) throws DeploymentException;

	ComponentBinding findComponentProxyBinding(EntityType<? extends Deployable> componentType);

	Set<EntityType<? extends Deployable>> boundTypes();

}
