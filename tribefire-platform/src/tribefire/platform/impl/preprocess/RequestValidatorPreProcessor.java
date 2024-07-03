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
package tribefire.platform.impl.preprocess;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.ValidationExpertRegistry;
import com.braintribe.model.processing.Validator;
import com.braintribe.model.processing.impl.ValidatorImpl;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.service.api.ServicePreProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.service.api.ServiceRequest;

public class RequestValidatorPreProcessor implements ServicePreProcessor<ServiceRequest> {
	private ModelAccessoryFactory modelAccessoryFactory;
	private ValidationExpertRegistry validationExpertRegistry;
	
	@Configurable
	@Required
	public void setValidationExpertRegistry(ValidationExpertRegistry validationExpertRegistry) {
		this.validationExpertRegistry = validationExpertRegistry;
	}
	
	@Configurable
	@Required
	public void setModelAccessoryFactory(ModelAccessoryFactory modelAccessoryFactory) {
		this.modelAccessoryFactory = modelAccessoryFactory;
	}
	
	@Override
	public ServiceRequest process(ServiceRequestContext requestContext, ServiceRequest request) {
		
		CmdResolver serviceDomainCmdResolver = modelAccessoryFactory.getForServiceDomain(requestContext.getDomainId()).getCmdResolver();
		Validator validator = new ValidatorImpl(serviceDomainCmdResolver, validationExpertRegistry);
		validator.validate(request);
		
		return request;
	}
	
}
