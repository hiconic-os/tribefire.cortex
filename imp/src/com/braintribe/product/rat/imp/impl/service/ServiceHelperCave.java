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
package com.braintribe.product.rat.imp.impl.service;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.notification.HasNotifications;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.product.rat.imp.AbstractHasSession;

/**
 * An entrance point in the "ServiceRequest" part of the ImpApi. Helps you to create and manage any service requests
 */
public class ServiceHelperCave extends AbstractHasSession {

	public ServiceHelperCave(PersistenceGmSession session) {
		super(session);
	}

	/**
	 * Creates a {@link ServiceHelper} for passed request and result type. If you pass a wrong result type, a
	 * {@link ClassCastException} might happen later during calling the request
	 */
	public <S extends ServiceRequest, R extends GenericEntity> ServiceHelper<S, R> with(S serviceRequest,
			@SuppressWarnings("unused") EntityType<R> serviceResultType) {
		return new ServiceHelper<S, R>(session(), serviceRequest);
	}

	/**
	 * Creates a {@link ServiceHelper} for passed request
	 */
	public <S extends ServiceRequest> ServiceHelper<S, GenericEntity> with(S serviceRequest) {
		return new ServiceHelper<S, GenericEntity>(session(), serviceRequest);
	}

	/**
	 * Use this method if the response type of your service request extends {@link HasNotifications}. This gives you
	 * additional features to work with the response notifications. Otherwise use
	 * {@link #with(ServiceRequest, EntityType)}
	 * <p>
	 * Creates a {@link ServiceHelper} for passed request and result type. If you pass a wrong result type, a
	 * {@link ClassCastException} might happen later during calling the request
	 */
	public <S extends ServiceRequest, R extends HasNotifications> ServiceHelperWithNotificationResponse<S, R> withNotificationResponse(S serviceRequest,
			@SuppressWarnings("unused") EntityType<R> serviceResultType) {
		return new ServiceHelperWithNotificationResponse<S, R>(session(), serviceRequest);
	}

	/**
	 * Use this method if the response type of your service request extends {@link HasNotifications}. This gives you
	 * additional features to work with the response notifications. Otherwise use {@link #with(ServiceRequest)}
	 * <p>
	 * Creates a {@link ServiceHelper} for passed request
	 */
	public <S extends ServiceRequest> ServiceHelperWithNotificationResponse<S, HasNotifications> withNotificationResponse(S serviceRequest) {
		return new ServiceHelperWithNotificationResponse<S, HasNotifications>(session(), serviceRequest);
	}

}
