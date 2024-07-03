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
package tribefire.platform.impl.servicedomain;

import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.api.aspect.DomainIdAspect;

import tribefire.cortex.model.sdmt.DomainAwareRequest;

public class DomainAwareProcessor implements ServiceProcessor<DomainAwareRequest, String>, ServiceDomainMappedDispatchingTestCommons {
	@Override
	public String process(ServiceRequestContext requestContext, DomainAwareRequest request) {
		String domainId = requestContext.getAspect(DomainIdAspect.class).orElseThrow(() -> new IllegalStateException("did not find a DomainIdAspect value in requestContext"));
		
		return RETVAL_PREFIX_DOMAIN_AWARE_PROCESSOR + domainId;
	}
}
