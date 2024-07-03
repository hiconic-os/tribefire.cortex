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

import com.braintribe.gm.model.security.reason.AuthenticationFailure;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.securityservice.OpenUserSession;
import com.braintribe.model.securityservice.OpenUserSessionResponse;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.securityservice.credentials.GrantedCredentials;
import com.braintribe.model.securityservice.credentials.identification.EmailIdentification;
import com.braintribe.model.securityservice.credentials.identification.UserIdentification;
import com.braintribe.model.securityservice.credentials.identification.UserNameIdentification;

/**
 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#openUserSession(OpenUserSession)}
 * tests based on {@link GrantedCredentials}
 * 
 */
public abstract class GrantedCredentialsAuthenticationTestBase extends SecurityServiceTest {

	protected abstract Credentials getValidGrantingCredentials();

	@Test
	public void testGrantingToUserName() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testGrantingToUserNameIdentification(grantingCredentials);
	}

	@Test
	public void testGrantingToNullUserName() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToUserNameIdentification(null, grantingCredentials, "null user name identification", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmptyUserName() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToUserNameIdentification("", grantingCredentials, "empty user name identification", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToInexistentUserName() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToUserNameIdentification("john.smith.", grantingCredentials, "inexistent user name identification", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmail() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testGrantingToEmailIdentification(grantingCredentials);

	}

	@Test
	public void testGrantingToNullEmail() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToEmailIdentification(null, grantingCredentials, "null e-mail identification", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToEmptyEmail() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToUserNameIdentification("", grantingCredentials, "empty e-mail identification", InvalidCredentials.T);

	}

	@Test
	public void testGrantingToInexistentEmail() throws Exception {

		Credentials grantingCredentials = getValidGrantingCredentials();

		testFailedGrantingToUserNameIdentification("john.smith@braintribe.com.", grantingCredentials, "inexistent e-mail identification",
				InvalidCredentials.T);

	}

	@Test
	public void testNullIdentification() {

		Credentials credentials = createGrantedCredentials(null, getValidGrantingCredentials());

		OpenUserSession openUserSession = createOpenUserSession(credentials);

		testFailedAuthenticationExpectingReason(openUserSession, "null identification", InvalidCredentials.T);

	}

	protected void testGrantingToUserNameIdentification(Credentials grantingCredentials) throws Exception {

		UserNameIdentification userNameIdentification = createUserNameIdentification("john.smith");

		testGrantedCredentials(userNameIdentification, grantingCredentials);

	}

	protected void testGrantingToEmailIdentification(Credentials grantingCredentials) throws Exception {

		EmailIdentification emailIdentification = createEmailIdentification("john.smith@braintribe.com");

		testGrantedCredentials(emailIdentification, grantingCredentials);

	}

	protected OpenUserSessionResponse testGrantedCredentials(UserIdentification userIdentification, Credentials grantingCredentials)
			throws Exception {

		GrantedCredentials grantedCredentials = createGrantedCredentials(userIdentification, grantingCredentials);

		OpenUserSession authReq = createOpenUserSession(grantedCredentials);

		return testSuccessfulAuthentication(authReq);

	}

	protected void testFailedGrantingToUserNameIdentification(String userName, Credentials grantingCredentials, String context,
			EntityType<? extends AuthenticationFailure> expectedReason) throws Exception {

		UserNameIdentification userNameIdentification = createUserNameIdentification(userName);

		testFailedGrantedCredentials(userNameIdentification, grantingCredentials, context, expectedReason);

	}

	protected void testFailedGrantingToEmailIdentification(String email, Credentials grantingCredentials, String context,
			EntityType<? extends AuthenticationFailure> expectedReason) throws Exception {

		EmailIdentification emailIdentification = createEmailIdentification(email);

		testFailedGrantedCredentials(emailIdentification, grantingCredentials, context, expectedReason);

	}

	protected void testFailedGrantedCredentials(UserIdentification userIdentification, Credentials grantingCredentials, String context,
			EntityType<? extends AuthenticationFailure> expectedReason) throws Exception {

		GrantedCredentials grantedCredentials = createGrantedCredentials(userIdentification, grantingCredentials);

		OpenUserSession authReq = createOpenUserSession(grantedCredentials);

		testFailedAuthenticationExpectingReason(authReq, context, expectedReason);

	}

	protected GrantedCredentials createGrantedCredentials(UserIdentification userIdentification, Credentials grantingCredentials) {
		GrantedCredentials grantedCredentials = GrantedCredentials.T.create();
		grantedCredentials.setUserIdentification(userIdentification);
		grantedCredentials.setGrantingCredentials(grantingCredentials);
		return grantedCredentials;
	}

}
