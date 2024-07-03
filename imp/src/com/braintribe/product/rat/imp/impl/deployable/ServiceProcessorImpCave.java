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
package com.braintribe.product.rat.imp.impl.deployable;

import com.braintribe.model.extensiondeployment.ServiceProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.AbstractImpCave;

/**
 * An {@link AbstractImpCave} specialized in {@link ServiceProcessor}
 */
public class ServiceProcessorImpCave extends AbstractImpCave<ServiceProcessor, ServiceProcessorImp> {

	public ServiceProcessorImpCave(PersistenceGmSession session) {
		super(session, "externalId", ServiceProcessor.T);
	}

	@Override
	protected ServiceProcessorImp buildImp(ServiceProcessor instance) {
		return new ServiceProcessorImp(session(), instance);
	}

	public <T extends ServiceProcessor> ServiceProcessorImp create(EntityType<T> entityType, String name, String externalId) {
		T serviceProcessor = session().create(entityType);
		serviceProcessor.setName(name);
		serviceProcessor.setExternalId(externalId);
		return new ServiceProcessorImp(session(), serviceProcessor);
	}

}
