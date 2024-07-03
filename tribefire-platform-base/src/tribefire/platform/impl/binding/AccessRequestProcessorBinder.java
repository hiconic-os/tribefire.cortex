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
package tribefire.platform.impl.binding;

import com.braintribe.cartridge.common.processing.accessrequest.InternalizingAccessRequestProcessor;
import com.braintribe.model.extensiondeployment.ServiceProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.deployment.api.IndirectComponentBinder;
import com.braintribe.model.processing.deployment.api.MutableDeploymentContext;

public class AccessRequestProcessorBinder extends AbstractSessionFactoryBasedBinder implements IndirectComponentBinder<//
		ServiceProcessor, //
		com.braintribe.model.extensiondeployment.access.AccessRequestProcessor, //
		AccessRequestProcessor<?, ?>> {

	@Override
	public AccessRequestProcessor<?, ?> bind(
			MutableDeploymentContext<com.braintribe.model.extensiondeployment.access.AccessRequestProcessor, AccessRequestProcessor<?, ?>> context) {

		InternalizingAccessRequestProcessor<?, ?> internalizer = new InternalizingAccessRequestProcessor<>(context.getInstanceToBeBound(),
				requestSessionFactory, systemSessionFactory);
		return internalizer;
	}

	@Override
	public EntityType<ServiceProcessor> componentType() {
		return ServiceProcessor.T;
	}

	@Override
	public Class<?>[] componentInterfaces() {
		return new Class<?>[] { com.braintribe.model.processing.service.api.ServiceProcessor.class };
	}

}
