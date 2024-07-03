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
package tribefire.platform.wire.space.cortex.deployment.deployables.processor;

import com.braintribe.model.processing.ValidationExpertRegistry;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.preprocess.RequestValidatorPreProcessor;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;

@Managed
public class PreProcessorSpace implements WireSpace {

	@Import
	private GmSessionsSpace gmSessions;

	@Managed
	public RequestValidatorPreProcessor requestValidatorPreProcessor() {
		RequestValidatorPreProcessor bean = new RequestValidatorPreProcessor();
		bean.setModelAccessoryFactory(gmSessions.systemModelAccessoryFactory());
		bean.setValidationExpertRegistry(ValidationExpertRegistry.createDefault());
		return bean;
	}

}
