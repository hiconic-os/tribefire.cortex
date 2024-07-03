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

import java.util.concurrent.Callable;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.UnsatisfiedMaybeTunneling;
import com.braintribe.gm.model.reason.essential.InternalError;
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

public interface SecurityExceptionsTrait {
	default <T> Maybe<T> executeUnreasoned(Callable<T> callable) {
		try {
			return Maybe.complete(callable.call());
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
		catch (UnsatisfiedMaybeTunneling e) {
			return e.getMaybe();
		}
		catch (UnsupportedOperationException e) {
			return Reasons.build(UnsupportedOperation.T).text(e.getMessage()).toMaybe();
		}
		catch (Exception e) {
			return InternalError.from(e).asMaybe();
		}
	}
}
