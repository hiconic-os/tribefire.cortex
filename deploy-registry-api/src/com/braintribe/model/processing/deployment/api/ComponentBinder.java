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

import java.util.Arrays;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.DeployableComponent;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * <p>
 * Binds a given component implementation for the matching component type.
 * 
 * @author dirk.scheffler
 *
 * @param <D>
 *            The denotation base type
 * @param <T>
 *            The component implementation type
 * 
 */
public interface ComponentBinder<D extends Deployable, T> {

	/**
	 * This method is responsible for providing the actual expert for given {@link Deployable}. It might use the one
	 * from given {@link MutableDeploymentContext#getInstanceToBeBound() context}, or it might create a completely
	 * independent instance, e.g. if a simulation of given deployable should be used instead.
	 */
	Object bind(MutableDeploymentContext<D, T> context) throws DeploymentException;

	/**
	 * This method is called right after a {@link Deployable} is undeployed, i.e. removed from the {@link DeployRegistry}.
	 */
	default void unbind(@SuppressWarnings("unused") UndeploymentContext<D, T> context) {
		// NOOP
	}

	/**
	 * Returns the component type of this binder which must be a sub type of a {@link Deployable} that is marked with
	 * the {@link DeployableComponent} meta data. Note: the return type must also be super type of D but it cannot
	 * expressed with generics. Avoid the pitfall by extending either {@link DirectComponentBinder} or
	 * {@link IndirectComponentBinder}
	 */
	EntityType<? extends Deployable> componentType();

	/**
	 * @return The interfaces the component implements.
	 */
	Class<?>[] componentInterfaces();

	/**
	 * <p>
	 * Returns a {@link ComponentBinder} which serves only as a standardized source of component type and component
	 * interfaces.
	 * 
	 * @param <D>
	 *            The component type
	 * @param <T>
	 *            The component interfaces
	 * @param componentType
	 *            Determines the component type to be exposed by the plain binder via
	 *            {@link ComponentBinder#componentType()}.
	 * @param componentInterface
	 *            Determines the component interface to be exposed by the plain binder.
	 * @return A plain {@link ComponentBinder} based on the given parameters.
	 */
	static <D extends Deployable, T> ComponentBinder<D, T> plainBinder(EntityType<D> componentType, Class<T> componentInterface,
			Class<?>... additionalComponentInterfaces) {

		Class<T>[] interfaces = new Class[1 + additionalComponentInterfaces.length];
		interfaces[0] = componentInterface;
		System.arraycopy(additionalComponentInterfaces, 0, interfaces, 1, additionalComponentInterfaces.length);
		return new PlainComponentBinder<D, T>(componentType, interfaces);
	}

	default public String stringify() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append('[');
		EntityType<? extends Deployable> componentType = componentType();
		sb.append("type: ");
		if (componentType == null) {
			sb.append("null");
		} else {
			sb.append(componentType.getTypeSignature());
		}
		sb.append(", interfaces: ");
		Class<?>[] componentInterfaces = componentInterfaces();
		if (componentInterfaces == null) {
			sb.append("null");
		} else {
			sb.append(Arrays.toString(componentInterfaces));
		}
		sb.append(']');
		return sb.toString();
	}

}
