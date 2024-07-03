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

import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.braintribe.cfg.Required;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.HardwiredDeployable;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.deployment.api.ConfigurableComponentInterfaceBindings;
import com.braintribe.model.processing.deployment.api.MutableDeployRegistry;

import tribefire.cortex.module.loading.api.HardwiredDenotationBinder;

public class HardwiredDenotationBinding implements HardwiredDenotationBinder {

	private final Map<HardwiredDeployable, Map<ComponentBinder<?, ?>, Supplier<?>>> bindings = new LinkedHashMap<>();

	private ConfigurableComponentInterfaceBindings interfaceBindings;
	private final Map<String, HardwiredDeployable> externalIdToDeployable = newMap();

	@Required
	public void setInterfaceBindings(ConfigurableComponentInterfaceBindings interfaceBindings) {
		this.interfaceBindings = interfaceBindings;
	}

	public Stream<HardwiredDeployable> deployableStream() {
		return bindings.keySet().stream() //
				.filter(this::isBoundByPlatform);
	}

	/** @return true if given {@link HardwiredDeployable} was bound by the platform and not one of the modules. */
	private boolean isBoundByPlatform(HardwiredDeployable hd) {
		return hd.getModule() == null;
	}

	@Override
	public <D extends HardwiredDeployable> HardwiredComponentBinding<D> bind(D deployable) {
		checkExternalIdUnique(deployable);

		Map<ComponentBinder<?, ?>, Supplier<?>> deployableBindings = bindings.computeIfAbsent(deployable, d -> new HashMap<>());

		return new HardwiredComponentBinding<D>() {
			@Override
			public <T> HardwiredComponentBinding<D> component(ComponentBinder<? super D, T> binder, Supplier<? extends T> expertSupplier) {
				deployableBindings.put(binder, expertSupplier);
				interfaceBindings.registerComponentInterfaces(binder);

				return this;
			}
		};
	}

	private void checkExternalIdUnique(HardwiredDeployable hd) {
		HardwiredDeployable other = externalIdToDeployable.put(hd.getExternalId(), hd);
		if (other != null)
			throw new IllegalStateException("Two different hardwired deployables register with the same externalId: '" + hd.getExternalId()
					+ "'. FIRST: " + deployableDescriptor(other) + ", SECOND: " + deployableDescriptor(hd));
	}

	private static String deployableDescriptor(HardwiredDeployable hd) {
		String origin = hd.getModule() == null ? "platform" : "module " + hd.getModule().getName();
		return hd.getName() + " (" + hd.entityType().getShortName() + " from " + origin + ")";
	}

	public void deploy(MutableDeployRegistry registry) {
		for (Map.Entry<HardwiredDeployable, Map<ComponentBinder<?, ?>, Supplier<?>>> entry : bindings.entrySet()) {
			Deployable deployable = entry.getKey();
			Map<ComponentBinder<?, ?>, Supplier<?>> deployableBindings = entry.getValue();

			ConfigurableDeployedUnit unit = new ConfigurableDeployedUnit();

			for (Map.Entry<ComponentBinder<?, ?>, Supplier<?>> componentEntry : deployableBindings.entrySet()) {
				Object suppliedImplementation = componentEntry.getValue().get();

				ComponentBinder<Deployable, Object> binder = (ComponentBinder<Deployable, Object>) componentEntry.getKey();

				HardwiredDeploymentContext<Deployable, Object> deploymentContext = new HardwiredDeploymentContext<>(suppliedImplementation);
				deploymentContext.setDeployable(deployable);

				Object exposedImplementation = binder.bind(deploymentContext);

				ConfigurableDeployedComponent component = new ConfigurableDeployedComponent(binder, exposedImplementation, suppliedImplementation);

				unit.putDeployedComponent(binder.componentType(), component);
			}

			registry.register(deployable, unit);
		}
	}

}
