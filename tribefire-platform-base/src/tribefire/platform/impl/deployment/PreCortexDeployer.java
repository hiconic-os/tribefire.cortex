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

import static com.braintribe.utils.lcd.CollectionTools2.first;
import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;

import com.braintribe.exception.ThrowableNormalizer;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.processing.deployment.api.BasicDeployContext;
import com.braintribe.model.processing.deployment.api.DeploymentService;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * @author peter.gazdik
 */
public class PreCortexDeployer extends BasicDeployContext {

	public static void doPreCortexDeploy(DeploymentService deploymentService, PersistenceGmSession session, List<Deployable> deployables) {
		PreCortexDeployer deployContext = new PreCortexDeployer(session, deployables);

		deploymentService.deploy(deployContext);

		deployContext.failIfErrors();
	}

	private final List<Throwable> errors = newList();

	public PreCortexDeployer(PersistenceGmSession session, List<Deployable> deployables) {
		super(session, deployables);
	}

	@Override
	public void failed(Deployable deployable, Throwable failure) {
		errors.add(failure);
	}

	public void failIfErrors() {
		if (errors.isEmpty())
			return;

		if (errors.size() == 1)
			throwUnchecked(first(errors));

		throwSingleUnchecked();
	}

	private void throwUnchecked(Throwable t) {
		throw new RuntimeException(new ThrowableNormalizer(t).asThrowableOrThrowUnchecked());
	}

	private void throwSingleUnchecked() {
		RuntimeException e = new RuntimeException("Error(s) occurred while pre-cortex deployment");
		errors.forEach(e::addSuppressed);
		throw e;
	}

}
