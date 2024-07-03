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

import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.DeploymentException;
import com.braintribe.model.processing.deployment.api.DirectComponentBinder;
import com.braintribe.model.processing.deployment.api.MutableDeploymentContext;
import com.braintribe.model.processing.service.api.ServiceProcessor;

import tribefire.cortex.check.processing.TimeMeasuringCheckServiceProcessor;

public class CheckProcessorBinder extends AbstractSessionFactoryBasedBinder
		implements DirectComponentBinder<CheckProcessor, com.braintribe.model.processing.check.api.CheckProcessor> {

	public static final CheckProcessorBinder INSTANCE = new CheckProcessorBinder();
	
	
	@Override
	public Object bind(MutableDeploymentContext<CheckProcessor, com.braintribe.model.processing.check.api.CheckProcessor> context)
			throws DeploymentException {

		return new TimeMeasuringCheckServiceProcessor(context.getInstanceToBeBound());
	}


	@Override
	public EntityType<CheckProcessor> componentType() {
		return CheckProcessor.T;
	}

	@Override
	public Class<?>[] componentInterfaces() {
		return new Class<?>[] { ServiceProcessor.class };
	}

}
