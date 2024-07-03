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

import com.braintribe.cartridge.common.processing.deployment.DeploymentScope;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.deployment.api.DeploymentContext;
import com.braintribe.model.processing.deployment.api.DeploymentScoping;

/**
 * <p>
 * A {@link DeploymentScoping} implementation for the wire-based deployment scope ({@link DeploymentScope}).
 * 
 * @author dirk.scheffler
 */
public class WireDeploymentScoping implements DeploymentScoping {

	private DeploymentScope scope;

	@Required
	public void setScope(DeploymentScope scope) {
		this.scope = scope;
	}

	@Override
	public void push(DeploymentContext<?, ?> context) {
		scope.push(context);
	}

	@Override
	public DeploymentContext<?, ?> pop(DeploymentContext<?, ?> context) {
		return scope.pop();
	}

	@Override
	public void end(DeploymentContext<?, ?> context) {
		scope.end(context);
	}

}
