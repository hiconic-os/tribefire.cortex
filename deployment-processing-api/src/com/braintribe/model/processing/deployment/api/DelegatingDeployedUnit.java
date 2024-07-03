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

import java.util.Map;
import java.util.function.Supplier;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

public class DelegatingDeployedUnit implements DeployedUnit {
	
	private DeployedUnit delegate;
	private final Supplier<DeployedUnit> delegateSupplier;
	
	public DelegatingDeployedUnit(Supplier<DeployedUnit> delegateSupplier) {
		this.delegateSupplier = delegateSupplier;
	}

	public DeployedUnit getDelegate() {
		if (delegate == null) {
			delegate = delegateSupplier.get();
		}
		return delegate;
	}
	
	@Override
	public <C> C getComponent(EntityType<? extends Deployable> componentType) throws DeploymentException {
		return getDelegate().getComponent(componentType);
	}

	@Override
	public <C> C findComponent(EntityType<? extends Deployable> componentType) {
		return getDelegate().findComponent(componentType);
	}
	
	@Override
	public DeployedComponent findDeployedComponent(EntityType<? extends Deployable> componentType) {
		return getDelegate().findDeployedComponent(componentType);
	}
	
	@Override
	public DeployedComponent getDeployedComponent(EntityType<? extends Deployable> componentType) {
		return getDelegate().getDeployedComponent(componentType);
	}

	@Override
	public Map<EntityType<? extends Deployable>, DeployedComponent> getComponents() {
		return getDelegate().getComponents();
	}

}
