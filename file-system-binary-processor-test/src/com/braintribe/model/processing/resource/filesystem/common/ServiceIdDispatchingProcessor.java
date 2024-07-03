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
package com.braintribe.model.processing.resource.filesystem.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.service.api.DispatchableRequest;

public class ServiceIdDispatchingProcessor<T extends DispatchableRequest> implements ServiceProcessor<T, Object> {
	private Map<String, ServiceProcessor<? super T, ?>> delegates = new LinkedHashMap<String, ServiceProcessor<? super T,?>>();
	
	public void register(String serviceId, ServiceProcessor<? super T, ?> processor) {
		delegates.put(serviceId, processor);
	}
	
	@Override
	public Object process(ServiceRequestContext requestContext, T request) {
		ServiceProcessor<? super T, ?> serviceProcessor = delegates.get(request.getServiceId());
		
		if (serviceProcessor == null)
			throw new NoSuchElementException("No ServiceProcessor registered for [" + request.entityType().getTypeSignature() + "] with serviceId [" + request.getServiceId() + "]");
			
		return serviceProcessor.process(requestContext, request);
	}

}
