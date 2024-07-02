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
package com.braintribe.model.processing.credential.authenticator;

import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.securityservice.api.exceptions.UserNotFoundException;
import com.braintribe.model.processing.securityservice.basic.user.UserInternalService;
import com.braintribe.model.processing.securityservice.basic.user.UserInternalServiceImpl;
import com.braintribe.model.processing.securityservice.impl.AbstractAuthenticateCredentialsServiceProcessor;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.securityservice.credentials.identification.UserIdentification;
import com.braintribe.model.user.User;

/**
 * <p>
 * Abstraction for authentication experts.
 * 
 * @param <T>
 *            The type of {@link Credentials} the expert handles.
 */
public abstract class BasicAuthenticateCredentialsServiceProcessor<T extends Credentials> extends AbstractAuthenticateCredentialsServiceProcessor<T> {

	protected Supplier<PersistenceGmSession> authGmSessionProvider;

	private static Logger log = Logger.getLogger(BasicAuthenticateCredentialsServiceProcessor.class);

	/**
	 * <p>
	 * Sets the Provider of {@link PersistenceGmSession}(s) used by this expert for fetching standard {@link User}
	 * information.
	 * 
	 * @param authGmSessionProvider
	 *            The Provider of {@link PersistenceGmSession}(s) used by this expert for fetching standard {@link User}
	 *            information.
	 */
	@Required
	@Configurable
	public void setAuthGmSessionProvider(Supplier<PersistenceGmSession> authGmSessionProvider) {
		this.authGmSessionProvider = authGmSessionProvider;
	}

	/**
	 * <p>
	 * Fetches a single user from the authentication access using the identification given by the {@code userIdentification}
	 * parameter.
	 * 
	 * <p>
	 * This method will use the given {@link PersistenceGmSession} for accessing the authentication data.
	 * 
	 * @param gmSession
	 *            {@link PersistenceGmSession} used for accessing the authentication access
	 * @param userIdentification
	 *            {@link UserIdentification} used for fetching a user from the authentication access
	 * @return the {@link User} found based on the given {@code userIdentification}
	 */
	protected Maybe<User> retrieveUser(PersistenceGmSession gmSession, UserIdentification userIdentification) {
		try {
			return Maybe.complete(getUserService(gmSession).retrieveUser(userIdentification));
		} catch (UserNotFoundException e) {
			return Reasons.build(NotFound.T).text("Missing user with identification: " + userIdentification).toMaybe();
		}
	}

	/**
	 * <p>
	 * Fetches a single user from the authentication access using the identification given by the {@code userIdentification}
	 * parameter.
	 * <p>
	 * This method will retrieve and discard a {@link PersistenceGmSession} for accessing the authentication data.
	 * 
	 * @param userIdentification
	 *            {@link UserIdentification} used for fetching a user from the authentication access
	 * @return the {@link User} found based on the given {@code userIdentification}
	 */
	protected Maybe<User> retrieveUser(UserIdentification userIdentification) {
		try {
			return Maybe.complete(getUserService(authGmSessionProvider.get()).retrieveUser(userIdentification));
		} catch (UserNotFoundException e) {
			return Reasons.build(NotFound.T).text("Missing user with identification: " + userIdentification).toMaybe();
		}
	}

	/**
	 * <p>
	 * Fetches a single user from the authentication access using the identification given by the {@code userIdentification}
	 * parameter.
	 * <p>
	 * This method will use the given {@link PersistenceGmSession} for accessing the authentication data.
	 * 
	 * @param gmSession
	 *            {@link PersistenceGmSession} used for accessing the authentication access
	 * @param userIdentification
	 *            {@link UserIdentification} used for fetching a user from the authentication access
	 * @param password
	 *            Password used for fetching a user from the authentication access
	 * @return the {@link User} found based on the given {@code userIdentification}
	 */
	protected Maybe<User> retrieveUser(PersistenceGmSession gmSession, UserIdentification userIdentification, String password) {
		try {
			return Maybe.complete(getUserService(gmSession).retrieveUser(userIdentification, password));
		} catch (UserNotFoundException e) {
			return Reasons.build(NotFound.T).text("Missing user with identification: " + userIdentification).toMaybe();
		}
	}

	/**
	 * <p>
	 * Fetches a single user from the authentication access using the identification given by the {@code userIdentification}
	 * parameter.
	 * <p>
	 * This method will retrieve and discard a {@link PersistenceGmSession} for accessing the authentication data.
	 * 
	 * @param userIdentification
	 *            {@link UserIdentification} used for fetching a user from the authentication access
	 * @return the {@link User} found based on the given {@code userIdentification}
	 */
	protected Maybe<User> retrieveUser(UserIdentification userIdentification, String password) {
		try {
			return Maybe.complete(getUserService(authGmSessionProvider.get()).retrieveUser(userIdentification, password));
		} catch (UserNotFoundException e) {
			return Reasons.build(NotFound.T).text("Missing user with identification: " + userIdentification).toMaybe();
		}
	}

	/**
	 * <p>
	 * Fetches a single user id from the authentication access using the identification given by the
	 * {@code userIdentification} parameter.
	 * <p>
	 * This method will use the given {@link PersistenceGmSession} for accessing the authentication data.
	 * 
	 * @param gmSession
	 *            {@link PersistenceGmSession} used for accessing the authentication access
	 * @param userIdentification
	 *            {@link UserIdentification} used for fetching a user from the authentication access
	 * @return the {@link User} id found based on the given {@code userIdentification}
	 * @throws UserNotFoundException
	 *             if an unique user is not found
	 */
	protected Maybe<String> retrieveUserId(PersistenceGmSession gmSession, UserIdentification userIdentification) {
		try {
			return Maybe.complete(getUserService(gmSession).retrieveUserId(userIdentification));
		} catch (UserNotFoundException e) {
			return Reasons.build(NotFound.T).text("Missing user with identification: " + userIdentification).toMaybe();
		}
	}

	/**
	 * <p>
	 * Creates a new {@link UserInternalService} for the given {@link PersistenceGmSession}.
	 * 
	 * @param gmSession
	 *            The {@link PersistenceGmSession} used to create a new {@link UserInternalService}
	 * @return A new {@link UserInternalService} for the given {@link PersistenceGmSession}
	 */
	// TODO: is this really needed elsewhere? Otherwise maybe internalize this UserInternalService logic
	private static UserInternalService getUserService(PersistenceGmSession gmSession) {
		return new UserInternalServiceImpl(gmSession);
	}

}
