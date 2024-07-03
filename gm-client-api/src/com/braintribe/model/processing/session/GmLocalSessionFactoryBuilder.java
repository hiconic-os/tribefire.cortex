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
package com.braintribe.model.processing.session;

import java.util.function.Supplier;

import com.braintribe.model.access.AccessService;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.auth.SessionAuthorization;
import com.braintribe.model.processing.session.api.resource.ResourceAccessFactory;

/**
 * Interface for a session factory builder that is able to create a session within tribefire. Both the
 * {@link #resourceAccessFactory(ResourceAccessFactory)} and the {@link #accessService(AccessService)} method must be used.
 */
public interface GmLocalSessionFactoryBuilder extends GmSessionFactoryBuilder {

	/**
	 * Provides the {@link AccessService} that could be used to access Accesses.
	 * 
	 * @param accessService
	 *            The {@link AccessService} that can be used to access Accesses.
	 * @return The {@link GmLocalSessionFactoryBuilder} instance currently used.
	 */
	GmLocalSessionFactoryBuilder accessService(AccessService accessService);

	/** {@inheritDoc} */
	@Override
	GmLocalSessionFactoryBuilder resourceAccessFactory(ResourceAccessFactory<PersistenceGmSession> resourceAccessFactory);

	/**
	 * Sets an optional {@link SessionAuthorization} provider.
	 * 
	 * @param sessionAuthorizationProvider
	 *            The {@link SessionAuthorization} that should be used.
	 * @return The {@link GmLocalSessionFactoryBuilder} instance currently used.
	 */
	GmLocalSessionFactoryBuilder sessionAuthorizationProvider(Supplier<SessionAuthorization> sessionAuthorizationProvider);

	/**
	 * Specifies a fixed {@link SessionAuthorization} object that will be used to authorize sessions.
	 * 
	 * @param sessionAuthorization
	 *            the {@link SessionAuthorization} object that should be used to authorize sessions.
	 * @return The {@link GmLocalSessionFactoryBuilder} instance currently used.
	 */
	GmLocalSessionFactoryBuilder sessionAuthorization(SessionAuthorization sessionAuthorization);
}
