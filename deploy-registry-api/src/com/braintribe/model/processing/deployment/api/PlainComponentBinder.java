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

import java.util.Objects;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

public class PlainComponentBinder<D extends Deployable, T> implements ComponentBinder<D, T> {

	private EntityType<D> componentType;
	private Class<?>[] componentInterfaces;

	public PlainComponentBinder(EntityType<D> componentType, Class<T>... componentInterfaces) {
		super();

		Objects.requireNonNull(componentType, "componentType must not be null");
		Objects.requireNonNull(componentInterfaces, "componentInterfaces must not be null");

		if (componentInterfaces.length == 0) {
			throw new IllegalArgumentException("componentInterfaces is empty");
		}

		for (Class<?> componentInterface : componentInterfaces) {
			Objects.requireNonNull(componentInterface, "componentInterfaces contains null entries");
			if (!componentInterface.isInterface()) {
				throw new IllegalArgumentException("Invalid plain binding. " + componentInterface.getName() + " is not an interface");
			}
		}

		this.componentType = componentType;
		this.componentInterfaces = componentInterfaces;

	}

	@Override
	public Object bind(MutableDeploymentContext<D, T> context) throws DeploymentException {
		return context.getInstanceToBeBound();
	}

	@Override
	public EntityType<D> componentType() {
		return componentType;
	}

	@Override
	public Class<?>[] componentInterfaces() {
		return componentInterfaces;
	}

}
