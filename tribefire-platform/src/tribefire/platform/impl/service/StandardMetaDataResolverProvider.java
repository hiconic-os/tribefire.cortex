// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.platform.impl.service;

import java.util.function.Function;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.service.api.ServiceProcessorAddressingException;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.service.api.ServiceRequest;

public class StandardMetaDataResolverProvider implements Function<ServiceRequest, CmdResolver> {
	
	private ModelAccessoryFactory modelAccessoryFactory;

	@Required
	@Configurable
	public void setModelAccessoryFactory(ModelAccessoryFactory modelAccessoryFactory) {
		this.modelAccessoryFactory = modelAccessoryFactory;
	}
	
	@Override
	public CmdResolver apply(ServiceRequest serviceRequest) {
		
		ModelAccessory modelAccessory = null;
		
		
		String domainId = ServiceDomains.getDomainId(serviceRequest);

		modelAccessory = modelAccessoryFactory.getForServiceDomain(domainId);
		/*
		if (domainId != null) {
			modelAccessory = modelAccessoryFactory.getForServiceDomain(domainId);
		} else {
			modelAccessory = modelAccessoryFactory.getForAccess("cortex");
		}*/

		if (modelAccessory == null) {
			throw new ServiceProcessorAddressingException("Failed to obtain a model accessory for domainId: "+domainId);
		}
		
		CmdResolver cmdResolver = modelAccessory.getCmdResolver();

		if (cmdResolver == null) {
			throw new ServiceProcessorAddressingException("Failed to obtain the meta data resolver for domainId: "+domainId);
		}
		
		return cmdResolver;
	}
	
}