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

import java.net.URL;

/**
 * This is the entry point for accessing an implementation of {@link GmSessionFactoryBuilder}.
 * <br>
 * <br>
 * Typical example for accessing an access remotely:
 * <br>
 * <pre>
 * // Create a session factory that points to a tribefire instance running on the same host at port 8443 (https) 
 * PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services").authentication("cortex", "cortex").done();
 * 
 * // Open a session to the "cortex" access. 
 * PersistenceGmSession session = sessionFactory.newSession("cortex");
 * </pre>
 * 
 * If a session ID is already present, this could also be used:
 * <br>
 * <pre>
 * ExistingSessionCredentials credentials = ExistingSessionCredentials.T.create();
 * credentials.setExistingSessionId(sessionId);
 *
 * PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services").authentication(credentials).done();
 * PersistenceGmSession session = sessionFactory.newSession("cortex");
 * </pre>
 * 
 * If a different user should be impersonated, the following code can be used:
 * <br>
 * <pre>
 * GrantedCredentials grantedCredentials = GrantedCredentials.T.create();
 * IntrinsicUserIdentification iui = IntrinsicUserIdentification.T.create();
 * // Assign the roles that the impersonated user should temporarily have 
 * iui.getRoles().add("tf-admin");
 * // Provide the name of the user that should be impersonated. 
 * iui.setUserName("alternative-user");
 * grantedCredentials.setUserIdentification(iui);
 * 
 * // Authenticate with a user that is allowed to impersonate other users.
 * UserPasswordCredentials grantingCredentials = UserPasswordCredentials.T.create();
 * UserNameIdentification superUserIdentification = UserNameIdentification.T.create();
 * superUserIdentification.setUserName("cortex");
 * grantingCredentials.setUserIdentification(superUserIdentification);
 * grantingCredentials.setPassword("cortex");
 * grantedCredentials.setGrantingCredentials(grantingCredentials);
 * 
 * PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services").authentication(grantedCredentials).done();

 * PersistenceGmSession session = sessionFactory.newSession("cortex");
 * </pre>
 *
 */
public abstract class GmSessionFactories {

	/**
	 * Creates an implementation of the {@link GmRemoteSessionFactoryBuilder} with the specified remote URL as a String. 
	 * @param url The URL where the remote tribefire services are reachable.
	 * @return An implementation of {@link GmRemoteSessionFactoryBuilder}, initialized with the remote URL.
	 */
	public static GmRemoteSessionFactoryBuilder remote(String url) {
		return new GmRemoteSessionFactoryBuilderImpl(url);
	}

	/**
	 * Creates an implementation of the {@link GmRemoteSessionFactoryBuilder} with the specified remote URL. 
	 * @param url The URL where the remote tribefire services are reachable.
	 * @return An implementation of {@link GmRemoteSessionFactoryBuilder}, initialized with the remote URL.
	 */
	public static GmRemoteSessionFactoryBuilder remote(URL url) {
		return new GmRemoteSessionFactoryBuilderImpl(url.toString());
	}
	
	/**
	 * Creates an implementation of the {@link GmLocalSessionFactoryBuilder}.
	 * @return An implementation of {@link GmLocalSessionFactoryBuilder}.
	 */
	public static GmLocalSessionFactoryBuilder local() {
		return new GmLocalSessionFactoryBuilderImpl();
	}
	
}
