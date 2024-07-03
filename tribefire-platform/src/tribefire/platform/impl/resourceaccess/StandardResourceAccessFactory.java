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

import java.util.Objects;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.session.exception.GmSessionRuntimeException;
import com.braintribe.model.processing.deployment.api.DeployRegistry;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.resource.ResourceAccess;
import com.braintribe.model.processing.session.api.resource.ResourceAccessFactory;

public class StandardResourceAccessFactory implements ResourceAccessFactory<PersistenceGmSession> {

	private DeployRegistry deployRegistry;

	public StandardResourceAccessFactory() {
	}

	@Required
	@Configurable
	public void setDeployRegistry(DeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;
	}

	@Override
	public ResourceAccess newInstance(PersistenceGmSession session) {
		Objects.requireNonNull(session, "session must not be null");
		String externalId = session.getAccessId();
		ResourceAccessFactory<PersistenceGmSession> resourceAccessFactory = deployRegistry.resolve(externalId, IncrementalAccess.T);
		if (resourceAccessFactory == null) {
			throw new GmSessionRuntimeException("No deployed ResourceAccessFactory returned for " + externalId);
		}
		return resourceAccessFactory.newInstance(session);
	}

}
