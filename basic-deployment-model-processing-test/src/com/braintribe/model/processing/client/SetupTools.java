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
package com.braintribe.model.processing.client;

import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.session.GmSessionFactories;
import com.braintribe.model.processing.session.GmSessionFactoryBuilderException;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;

/**
 * 
 */
public class SetupTools {

	private static final String user = "cortex";
	private static final String password = "cortex";

	private static final String SERVER_URL = "http://localhost:8080/tribefire-services";

	public static PersistenceGmSession createNewSession(String accessId) {
		try {
			PersistenceGmSessionFactory sessionFactory = setupRemoteSessionFactory(SERVER_URL, user, password);
			return newSession(sessionFactory, accessId);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static PersistenceGmSessionFactory setupRemoteSessionFactory(String baseUrl, String user, String password) {
		try {
			PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote(baseUrl).authentication(user, password).done();
			return sessionFactory;
		} catch (GmSessionFactoryBuilderException e) {
			throw new RuntimeException("Could not create a session to "+baseUrl+" using user "+user, e);
		}
	}

	private static PersistenceGmSession newSession(PersistenceGmSessionFactory factory, String accessId) {
		try {
			return factory.newSession(accessId);
		} catch (GmSessionException e) {
			throw new RuntimeException("Error while creating new session for access '" + accessId + "'!", e);
		}
	}

}
