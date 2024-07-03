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
package tribefire.platform.wire.space;

import static com.braintribe.model.processing.deployment.api.SchrodingerBean.schrodingerBeanId;

import java.util.function.Function;

import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.cortex.deployment.DeploymentSpace;

/**
 * @author peter.gazdik
 */
@Managed
public class SchrodingerBeansSpace implements WireSpace {

	@Import
	private DeploymentSpace deployment;

	// DC stands for DeployableComponent
	public <T, DC extends Deployable> SchrodingerBean<T> newBean(//
			String name, //
			Function<CortexConfiguration, DC> deployableResolver, //
			ComponentBinder<DC, T> componentBinder) {

		T proxy = deployment.proxyingDeployedComponentResolver().resolve(schrodingerBeanId(name), componentBinder);

		return SchrodingerBean.of(name, proxy, deployableResolver);
	}

}
