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

import org.junit.Assert;
import org.junit.Test;

import com.braintribe.model.usersession.UserSession;

/**
 * Tests for {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#logout(String)}
 *
 */
public class SessionLogoutTestBase extends SecurityServiceTest {

	@Test
	public void testLogout() throws Exception {

		UserSession userSession = openSession();

		testLogoutValidSession(userSession);

	}

	/**
	 * Targets: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#logout(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing valid existent {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>session id must be considered invalid by
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * <li>No exceptions shall be thrown
	 * </ul>
	 */
	@Test
	public void testLogoutValidSession() throws Exception {
		testLogoutValidSession(openSession());
		testLogoutValidSession(openSession());
		testLogoutValidSession(openSession());
		testLogoutValidSession(openSession());
		testLogoutValidSession(openSession());
	}

	/**
	 * Targets: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#logout(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing invalid or inexistent {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>session id must be remain invalid by
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * <li>No exceptions shall be thrown
	 * </ul>
	 */
	@Test
	public void testLogoutInvalidSession() {
		testLogoutInvalidSession(null);
		testLogoutInvalidSession(empty);
		testLogoutInvalidSession("  \r\t  ");
		testLogoutInvalidSession(random);
		testLogoutInvalidSession(generateRandomString());
		testLogoutInvalidSession(generateRandomString());
		testLogoutInvalidSession(generateRandomString());
		testLogoutInvalidSession(generateRandomString());
		testLogoutInvalidSession(generateRandomString());
	}

	private void testLogoutValidSession(UserSession validUserSession) {
		try {

			Assert.assertTrue(validUserSession + " should be valid right after creation", isValidSession(validUserSession.getSessionId()));

			Assert.assertTrue(validUserSession + " should had been successfully logged out", logout(validUserSession.getSessionId()));

			assertUserSessionLoggedOut(validUserSession.getSessionId());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("unexpected exception while logging out " + validUserSession + ": " + e.getMessage());
		}
	}

	private void testLogoutInvalidSession(String invalidSessionId) {
		try {

			Assert.assertFalse("session " + invalidSessionId + " should be initially invalid", isValidSession(invalidSessionId));

			Assert.assertFalse("logging out invalid session " + invalidSessionId + " should have returned true", logout(invalidSessionId));

			assertUserSessionLoggedOut(invalidSessionId);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("unexpected exception while logging out already invalid session " + invalidSessionId + ": " + e.getMessage());
		}
	}

}
