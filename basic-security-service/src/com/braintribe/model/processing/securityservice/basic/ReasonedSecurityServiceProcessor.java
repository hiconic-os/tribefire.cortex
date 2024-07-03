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
package com.braintribe.model.processing.securityservice.basic;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.UnsupportedOperation;
import com.braintribe.gm.model.security.reason.AuthenticationFailure;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.gm.model.security.reason.InvalidSession;
import com.braintribe.gm.model.security.reason.SessionExpired;
import com.braintribe.gm.model.security.reason.SessionNotFound;
import com.braintribe.model.processing.securityservice.api.exceptions.AuthenticationException;
import com.braintribe.model.processing.securityservice.api.exceptions.ExpiredSessionException;
import com.braintribe.model.processing.securityservice.api.exceptions.InvalidCredentialsException;
import com.braintribe.model.processing.securityservice.api.exceptions.InvalidSessionException;
import com.braintribe.model.processing.securityservice.api.exceptions.SessionNotFoundException;
import com.braintribe.model.processing.securityservice.api.exceptions.UserNotFoundException;
import com.braintribe.model.processing.service.api.ReasonedServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.api.UnsupportedRequestTypeException;
import com.braintribe.model.securityservice.SecurityRequest;

/**
 * <p>
 * ServiceProcessor that wraps the actualy Security ServiceProcessor and turns exceptions into reasons
 * @author Dirk Scheffler
 */
public class ReasonedSecurityServiceProcessor implements ReasonedServiceProcessor<SecurityRequest, Object> {

	private ServiceProcessor<SecurityRequest, ?> delegate;

	@Required
	@Configurable
	public void setDelegate(ServiceProcessor<SecurityRequest, ?> delegate) {
		this.delegate = delegate;
	}

	@Override
	public Maybe<? extends Object> processReasoned(ServiceRequestContext requestContext, SecurityRequest request) {
		try {
			Object response = delegate.process(requestContext, request);
			return Maybe.complete(response);
		} //
		catch (SessionNotFoundException e) {
			return Reasons.build(SessionNotFound.T).text(e.getMessage()).toMaybe();
		} //
		catch (ExpiredSessionException e) {
			return Reasons.build(SessionExpired.T).text(e.getMessage()).toMaybe();
		} //
		catch (InvalidSessionException e) {
			return Reasons.build(InvalidSession.T).text(e.getMessage()).toMaybe();
		} //
		catch (InvalidCredentialsException | UserNotFoundException e) {
			return Reasons.build(InvalidCredentials.T).text(e.getMessage()).toMaybe();
		} //
		catch (AuthenticationException e) {
			return Reasons.build(AuthenticationFailure.T).text(e.getMessage()).toMaybe();
		} // 
		catch (UnsupportedRequestTypeException e) {
			return Reasons.build(UnsupportedOperation.T).text(e.getMessage()).toMaybe();
		}
	}
}