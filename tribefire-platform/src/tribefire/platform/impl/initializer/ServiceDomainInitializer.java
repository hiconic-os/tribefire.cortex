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
package tribefire.platform.impl.initializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.tools.PreparedQueries;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.service.domain.ServiceDomain;

public class ServiceDomainInitializer extends SimplePersistenceInitializer {

	private final Map<String, ServiceDomainInfo> serviceDomainModels = new HashMap<>();

	public ServiceDomainInitializer() {
	}

	public ServiceDomainInitializer register(String globalId, String externalId, String modelName, String domainName) {
		serviceDomainModels.put(globalId, new ServiceDomainInfo(externalId, modelName, domainName));
		return this;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {

		ManagedGmSession session = context.getSession();

		for (Entry<String, ServiceDomainInfo> e : serviceDomainModels.entrySet()) {

			GmMetaModel model = queryMetaModel(session, e.getValue().modelName);

			ServiceDomain serviceDomain = session.create(ServiceDomain.T, e.getKey());
			serviceDomain.setExternalId(e.getValue().externalId);
			serviceDomain.setName(e.getValue().domainName);
			serviceDomain.setServiceModel(model);

		}

	}

	private GmMetaModel queryMetaModel(ManagedGmSession session, String modelName) throws ManipulationPersistenceException {
		GmMetaModel result = session.query().select(PreparedQueries.modelByName(modelName)).unique();
		if (result == null) {
			throw new ManipulationPersistenceException("Model not found in the context session: " + modelName);
		}
		return result;
	}

	private static class ServiceDomainInfo {

		ServiceDomainInfo(String externalId, String modelName, String domainName) {
			this.externalId = externalId;
			this.modelName = modelName;
			this.domainName = domainName;
		}

		private final String externalId;
		private final String modelName;
		private final String domainName;

	}

}
