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
package tribefire.platform.impl.deployment;

import java.util.function.Function;
import java.util.function.Supplier;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.deployment.api.ComponentBinding;
import com.braintribe.model.processing.deployment.api.ExpertContext;

public class ConfigurableComponentBinding<D extends Deployable, T> implements ComponentBinding {

	private final ComponentBinder<D, T> componentBinder;
	private final Function<? extends ExpertContext<?>, ?> valueSupplier;

	public ConfigurableComponentBinding(ComponentBinder<D, T> componentBinder, Supplier<? extends T> valueSupplier) {
		this.componentBinder = componentBinder;
		this.valueSupplier = c -> valueSupplier.get();
	}
	
	public ConfigurableComponentBinding(ComponentBinder<D, T> componentBinder, Function<? extends ExpertContext<?>, ?> factory) {
		this.componentBinder = componentBinder;
		this.valueSupplier = factory;
	}

	@Override
	public EntityType<? extends Deployable> componentType() {
		return componentBinder.componentType();
	}
	
	@Override
	public ComponentBinder<D, T> getComponentBinder() {
		return componentBinder;
	}

	@Override
	public Function<? extends ExpertContext<?>, ?> componentValueSupplier() {
		return valueSupplier;
	}

}
