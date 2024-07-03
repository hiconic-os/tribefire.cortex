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
package tribefire.platform.impl.resourceaccess;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.processing.deployment.api.DeployedComponentResolver;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.resource.ResourceAccess;
import com.braintribe.model.processing.session.api.resource.ResourceAccessFactory;

public class ProxyResourceAccessFactory implements ResourceAccessFactory<PersistenceGmSession> {

	private DeployedComponentResolver deployedComponentResolver;

	@Configurable @Required
	public void setDeployedComponentResolver(DeployedComponentResolver deployedComponentResolver) {
		this.deployedComponentResolver = deployedComponentResolver;
	}

	@Override
	public ResourceAccess newInstance(PersistenceGmSession session) {
		String externalId = session.getAccessId();
		// works because MasterIncrementalAccess implements ResourceAccessFactory 
		ResourceAccessFactory<PersistenceGmSession> resourceAccessFactory = deployedComponentResolver.resolve(externalId, IncrementalAccess.T);
		return resourceAccessFactory.newInstance(session);
	}
}
