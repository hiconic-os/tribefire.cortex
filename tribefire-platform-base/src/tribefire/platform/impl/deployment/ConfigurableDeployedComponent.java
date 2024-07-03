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

import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.deployment.api.DeployedComponent;

public class ConfigurableDeployedComponent implements DeployedComponent {
	private Object exposedImplementation;
	private Object suppliedImplementation;
	private ComponentBinder<?, ?> binder;

	public ConfigurableDeployedComponent(ComponentBinder<?, ?> binder, Object exposedImplementation, Object suppliedImplementation) {
		super();
		this.binder = binder;
		this.exposedImplementation = exposedImplementation;
		this.suppliedImplementation = suppliedImplementation;
	}

	@Override
	public Object exposedImplementation() {
		return exposedImplementation;
	}

	@Override
	public Object suppliedImplementation() {
		return suppliedImplementation;
	}
	
	@Override
	public ComponentBinder<?, ?> binder() {
		return binder;
	}
	
}
