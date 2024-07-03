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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.processing.deployment.api.DeployContext;
import com.braintribe.model.processing.deployment.api.UndeployContext;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * @author christina.wilpernig
 */
public class TestDeployContext implements DeployContext, UndeployContext {

	private PersistenceGmSession session;
	private List<Deployable> deployables;
	
	public Set<Deployable> successfulDeployables = new HashSet<>();
	public Map<Deployable,Throwable> failedDeployables = new HashMap<>();

	public void setSession(PersistenceGmSession session) {
		this.session = session;
	}
	
	public void setDeployables(List<Deployable> deployables) {
		this.deployables = deployables;
	}
	
	@Override
	public List<Deployable> deployables() {
		return deployables;
	}

	@Override
	public void succeeded(Deployable deployable) {
		successfulDeployables.add(deployable);
	}

	@Override
	public void failed(Deployable deployable, Throwable failure) {
		failedDeployables.put(deployable, failure);
	}

	@Override
	public PersistenceGmSession session() {
		return session;
	}

}
