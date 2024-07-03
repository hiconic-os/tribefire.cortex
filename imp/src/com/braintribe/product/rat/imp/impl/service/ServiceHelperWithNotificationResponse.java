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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.notification.HasNotifications;
import com.braintribe.model.notification.MessageNotification;
import com.braintribe.model.notification.Notification;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * A {@link ServiceHelper} for a {@link ServiceRequest} which response extends {@link HasNotifications}. Has convenience
 * methods for evaluating or logging the notifications of the response
 *
 * @param <S>
 *            Type of ServiceRequest managed by this imp
 * @param <R>
 *            Type of ServiceResult that belongs to this imp's ServiceRequest
 */
public class ServiceHelperWithNotificationResponse<S extends ServiceRequest, R extends HasNotifications> extends ServiceHelper<S, R> {

	ServiceHelperWithNotificationResponse(PersistenceGmSession session, S serviceRequest) {
		super(session, serviceRequest);
	}

	/**
	 * calls the service via {@link ServiceHelper#call()}
	 *
	 * @return list of notifications returned by the respective service response
	 */
	public List<Notification> callAndGetNotifications() {
		R result = call();
		return result.getNotifications();
	}

	/**
	 * calls the service via {@link ServiceHelper#call()}
	 *
	 * @return list of MessageNotifications returned by the respective service response -> drops all other notifications
	 */
	public List<MessageNotification> callAndGetMessageNotifications() {
		R result = call();
		//@formatter:off
		return result.getNotifications()
				.stream()
				.filter(n -> n instanceof MessageNotification)
				.map(n -> (MessageNotification) n)
				.collect(Collectors.toList());
		//@formatter:on
	}

	public R callAndPrintMessages() {
		callAndPrintMessages(null);
		
		return serviceResult;
	}

	public R callAndPrintMessages(Logger logger) {
		Collection<String> messages = callAndGetMessages();

		if (logger == null) {
			messages.forEach(System.out::println);
		} else {
			messages.forEach(logger::info);
		}
		
		return serviceResult;
	}

	/**
	 * calls the service via {@link ServiceHelper#call()}
	 *
	 * @return list of Messages returned by MessageNotifications via the service response
	 */
	public List<String> callAndGetMessages() {
		//@formatter:off
		return callAndGetMessageNotifications().stream()
				.map(n -> n.getMessage())
				.collect(Collectors.toList());
		//@formatter:on
	}

	/**
	 * only call AFTER (any) call() method
	 */
	@Override
	protected void printWarning() {
		if (serviceResult == null) {
			throw new RuntimeException("Imp: Cannot print warning: Don't have any service result");
		}

		serviceResult.getNotifications().stream().filter(n -> n instanceof MessageNotification).map(n -> (MessageNotification) n)
				.map(n -> n.getMessage() + ": \nDetails: " + n.getDetails()).forEach(logger::warn);
	}

	@Override
	// specializes return type
	public ServiceHelperWithNotificationResponse<S, R> verifyingResultBy(Function<R, Boolean> verifyingResultBy) {
		super.verifyingResultBy(verifyingResultBy);

		return this;
	}

	@Override
	// specializes return type
	public ServiceHelperWithNotificationResponse<S, R> verifyingResultBy(Supplier<Boolean> verifyingResultBy) {
		super.verifyingResultBy(verifyingResultBy);

		return this;
	}
	
	@Override
	public ServiceHelperWithNotificationResponse<S, R> addEntitiesForAutoRefresh(Collection<? extends GenericEntity> entities) {
		super.addEntitiesForAutoRefresh(entities);
		
		return this;
	}

}
