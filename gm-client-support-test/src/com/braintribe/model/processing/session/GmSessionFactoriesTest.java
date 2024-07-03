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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.securityservice.credentials.ExistingSessionCredentials;
import com.braintribe.model.securityservice.credentials.UserPasswordCredentials;
import com.braintribe.model.securityservice.credentials.identification.UserNameIdentification;
import com.braintribe.model.user.User;

//@Category(TribefireServices.class)
@Ignore // TODO
public class GmSessionFactoriesTest {

	@Test
	public void testStandaloneResourceAccess() throws Exception {

		PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services")
				.authentication("cortex", "cortex").done();
		PersistenceGmSession session = sessionFactory.newSession("auth");

		EntityQuery query = EntityQueryBuilder.from(User.class).done();
		List<User> users = session.query().entities(query).list();

		assertThat(users).isNotNull();
		assertThat(users.size()).isGreaterThan(0);

	}

	@Test
	public void testStandaloneResourceAccessWithCredentials() throws Exception {

		UserPasswordCredentials credentials = UserPasswordCredentials.T.create();
		UserNameIdentification ui = UserNameIdentification.T.create();
		ui.setUserName("cortex");
		credentials.setUserIdentification(ui);
		credentials.setPassword("cortex");

		PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services")
				.authentication(credentials).done();
		PersistenceGmSession session = sessionFactory.newSession("auth");

		EntityQuery query = EntityQueryBuilder.from(User.class).done();
		List<User> users = session.query().entities(query).list();

		assertThat(users).isNotNull();
		assertThat(users.size()).isGreaterThan(0);

	}

	@Test
	public void testStandaloneResourceAccessWithExistingSessionId() throws Exception {

		PersistenceGmSessionFactory sessionFactoryAuth = GmSessionFactories.remote("https://localhost:8443/tribefire-services")
				.authentication("cortex", "cortex").done();
		PersistenceGmSession sessionAuth = sessionFactoryAuth.newSession("auth");
		String sessionid = sessionAuth.getSessionAuthorization().getSessionId();

		ExistingSessionCredentials credentials = ExistingSessionCredentials.T.create();
		credentials.setExistingSessionId(sessionid);

		PersistenceGmSessionFactory sessionFactory = GmSessionFactories.remote("https://localhost:8443/tribefire-services")
				.authentication(credentials).done();
		PersistenceGmSession session = sessionFactory.newSession("auth");

		EntityQuery query = EntityQueryBuilder.from(User.class).done();
		List<User> users = session.query().entities(query).list();

		assertThat(users).isNotNull();
		assertThat(users.size()).isGreaterThan(0);

	}
}
