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
package com.braintribe.model.processing.securityservice.basic.test.base;

import org.junit.Test;

import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.model.securityservice.OpenUserSession;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.securityservice.credentials.GrantedCredentials;
import com.braintribe.model.securityservice.credentials.UserPasswordCredentials;

/**
 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#openUserSession(OpenUserSession)}
 * tests based on {@link GrantedCredentials} with {@link UserPasswordCredentials} as granting credentials.
 * 
 */
public class GrantingUserPasswordCredentialsAuthenticationTestBase extends GrantedCredentialsAuthenticationTestBase {

	@Test
	public void testGrantingToUserNameNullUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials(null, "cortex");

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "null granting user name", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToUserNameEmptyUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("", "cortex");

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "empty granting user name", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToUserNameInexistentUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex.", "cortex");

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "inexistent granting user name", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToUserNameNullPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", null);

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "null granting password", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToUserNameEmptyPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", "");

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "empty granting password", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToUserNameWrongPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", "cortex.");

		testFailedGrantingToUserNameIdentification("john.smith", grantingCredentials, "wrong granting password", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailNullUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials(null, "cortex");

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "null granting user name", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailEmptyUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("", "cortex");

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "empty granting user name", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailInexistentUserName() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex.", "cortex");

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "inexistent granting user name",
				InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailNullPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", null);

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "null granting password", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailEmptyPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", "");

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "empty granting password", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmailWrongPassword() throws Exception {

		Credentials grantingCredentials = createUserNamePasswordCredentials("cortex", "cortex.");

		testFailedGrantingToEmailIdentification("john.smith@braintribe.com", grantingCredentials, "wrong granting password", InvalidCredentials.T);

	}

	@Override
	protected Credentials getValidGrantingCredentials() {

		UserPasswordCredentials grantingUserPasswordCredentials = UserPasswordCredentials.T.create();
		grantingUserPasswordCredentials.setUserIdentification(createUserNameIdentification("cortex"));
		grantingUserPasswordCredentials.setPassword("cortex");

		return grantingUserPasswordCredentials;

	}

	protected UserPasswordCredentials createUserNamePasswordCredentials(String userName, String password) {

		UserPasswordCredentials grantingUserPasswordCredentials = UserPasswordCredentials.T.create();
		grantingUserPasswordCredentials.setUserIdentification(createUserNameIdentification(userName));
		grantingUserPasswordCredentials.setPassword(password);

		return grantingUserPasswordCredentials;

	}

	protected UserPasswordCredentials createEmailPasswordCredentials(String email, String password) {

		UserPasswordCredentials grantingUserPasswordCredentials = UserPasswordCredentials.T.create();
		grantingUserPasswordCredentials.setUserIdentification(createEmailIdentification(email));
		grantingUserPasswordCredentials.setPassword(password);

		return grantingUserPasswordCredentials;

	}

}
