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
package com.braintribe.model.processing.access.service.impl.standard;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.processing.access.service.api.registry.AccessRegistrationInfo;

/**
 * Wrapper class for an {@link AccessRegistrationInfo} that is aware of the {@link Origin} of the registration.
 * 
 * @see AccessServiceImpl
 * 
 * 
 */
class OriginAwareAccessRegistrationInfo {

	/**
	 * Enumeration containing the possible origins of accesses in the {@link AccessServiceImpl}.
	 * 
	 * 
	 */
	enum Origin {
		/**
		 * Indicates that an access has been registered by configuration.
		 */
		CONFIGURATION,
		/**
		 * Indicates that an access has been registered during runtime.
		 */
		REGISTRATION;
	}

	private AccessRegistrationInfo accessRegistrationInfo;
	private Origin origin;

	protected OriginAwareAccessRegistrationInfo(AccessRegistrationInfo accessRegistrationInfo, Origin origin) {
		this.accessRegistrationInfo = accessRegistrationInfo;
		this.origin = origin;
	}
	
	public Origin getOrigin() {
		return origin;
	}

	public IncrementalAccess getAccess() {
		return accessRegistrationInfo.getAccess();
	}

	public String getAccessId() {
		return accessRegistrationInfo.getAccessId();
	}

	public String getAccessDenotationType() {
		return accessRegistrationInfo.getAccessDenotationType();
	}

	public String getMetaModelAccessId() {
		return accessRegistrationInfo.getModelAccessId();
	}

	public String getDataMetaModelName() {
		return accessRegistrationInfo.getModelName();
	}

	public String getWorkbenchMetaModelName() {
		return accessRegistrationInfo.getWorkbenchModelName();
	}
	
	public String getWorkbenchAccessId() {
		return accessRegistrationInfo.getWorkbenchAccessId();
	}
	
	public String getResourceAccessFactoryId() {
		return accessRegistrationInfo.getResourceAccessFactoryId();
	}

	public String getServiceModelName() {
		return accessRegistrationInfo.getServiceModelName();
	}
}
