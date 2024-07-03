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
package com.braintribe.product.rat.imp;

import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.product.rat.imp.impl.service.ServiceHelper;
import com.braintribe.product.rat.imp.impl.service.ServiceHelperCave;
import com.braintribe.product.rat.imp.impl.utils.GeneralGmUtils;

/**
 * This is the entrance point to the ImpAPI for all methods that do not require a cortex session. (see also:
 * {@link ImpApi})
 */
public class ReducedImpApi extends AbstractHasSession {

	protected final ImpApiFactory impApiFactory;

	public ReducedImpApi(ImpApiFactory impApiFactory, PersistenceGmSession session) {
		super(session);
		this.impApiFactory = impApiFactory;
	}

	public ImpApiFactory getImpApiFactory() {
		return impApiFactory;
	}

	/**
	 * Common utility methods (e.g. create a {@code Resource} entity)
	 */
	public GeneralGmUtils utils() {
		return new GeneralGmUtils(impApiFactory, session());
	}

	/**
	 * Returns an imp that manages (i.e. evals) the passed service request
	 *
	 * @param request
	 *            the request that should be managed by the imp
	 */
	public ServiceHelper<ServiceRequest, Object> service(ServiceRequest request) {
		return new ServiceHelper<ServiceRequest, Object>(session(), request);
	}

	/**
	 * Goes to a {@link ServiceHelperCave deeper level} of the ServiceImp API with lots of utility methods for calling
	 * common or custom services
	 */
	public ServiceHelperCave service() {
		return new ServiceHelperCave(session());
	}

	public String getUrl() {
		return impApiFactory.getURL();
	}
	
	public String getBaseUrl() {
		return impApiFactory.getBaseURL();
	}
}
