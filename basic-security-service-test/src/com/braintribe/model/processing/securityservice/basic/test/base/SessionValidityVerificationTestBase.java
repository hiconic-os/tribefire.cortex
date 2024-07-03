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
 * Tests for
 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
 *
 */
public class SessionValidityVerificationTestBase extends SecurityServiceTest {

	@Test
	public void testExpiration() throws Exception {

		if (!testConfig.getEnableExpiration()) {
			System.out.println("suppressed test as testConfig.getEnableExpiration() is " + testConfig.getEnableExpiration());
			return;
		}

		openSession();

		Thread.sleep(35000);
	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing valid existent {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code true} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testValidSessionValidity() {

		String sessionId1 = openSession().getSessionId();
		String sessionId2 = openSession().getSessionId();
		String sessionId3 = openSession().getSessionId();

		for (int i = 0; i < 100; i++) {
			testValidUserSession(sessionId1);
			testValidUserSession(sessionId2);
			testValidUserSession(sessionId3);
		}

	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: invalid/inexistent sessions ids
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code false} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testInexistentSessionValidity() {

		testInvalidUserSession(null);
		testInvalidUserSession(empty);
		testInvalidUserSession("  \r\t  ");
		testInvalidUserSession(random);
		testInvalidUserSession(generateRandomString());
		testInvalidUserSession(generateRandomString());
		testInvalidUserSession(generateRandomString());
		testInvalidUserSession(generateRandomString());
		testInvalidUserSession(generateRandomString());

		// slightly different session id from existing valid
		String validSessionId = openSession().getSessionId();

		testInvalidUserSession(validSessionId + "\t");
		testInvalidUserSession("\t" + validSessionId + "\t");
		testInvalidUserSession("\t" + validSessionId);
	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing existent expired {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code false} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testExpiredSessionValidity() throws Exception {

		if (!testConfig.getEnableExpiration()) {
			System.out.println("suppressed test as testConfig.getEnableExpiration() is " + testConfig.getEnableExpiration());
			return;
		}

		String sessionId1 = openSession().getSessionId();
		String sessionId2 = openSession().getSessionId();
		String sessionId3 = openSession().getSessionId();

		Thread.sleep(20500);

		// expired sessions
		for (int i = 0; i < 100; i++) {
			testInvalidUserSession(sessionId1);
			testInvalidUserSession(sessionId2);
			testInvalidUserSession(sessionId3);
		}
	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing existent invalidated {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code false} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testInvalidatedSessionValidity() throws Exception {

		String sessionId1 = openSession().getSessionId();
		String sessionId2 = openSession().getSessionId();
		String sessionId3 = openSession().getSessionId();

		logout(sessionId1);
		logout(sessionId2);
		logout(sessionId3);

		// invalidated sessions
		for (int i = 0; i < 100; i++) {
			testInvalidUserSession(sessionId1);
			testInvalidUserSession(sessionId2);
			testInvalidUserSession(sessionId3);
		}

	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing existent expired and invalidated {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code false} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testExpiredAndInvalidatedSessionValidity() throws Exception {

		if (!testConfig.getEnableExpiration()) {
			System.out.println("suppressed test as testConfig.getEnableExpiration() is " + testConfig.getEnableExpiration());
			return;
		}

		String sessionId1 = openSession().getSessionId();
		String sessionId2 = openSession().getSessionId();
		String sessionId3 = openSession().getSessionId();

		logout(sessionId1);
		logout(sessionId2);
		logout(sessionId3);

		Thread.sleep(20500);

		// expired and invalidated sessions
		for (int i = 0; i < 100; i++) {
			testInvalidUserSession(sessionId1);
			testInvalidUserSession(sessionId2);
			testInvalidUserSession(sessionId3);
		}

	}

	/**
	 * Target: {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: invalid/inexistent sessions ids
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@code false} must be returned from
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}</li>
	 * <li>No exceptions shall be thrown</li>
	 * </ul>
	 */
	@Test
	public void testInexistentSessionValidityVerification() {
		testInvalidUserSession(null);
		testInvalidUserSession("");
		testInvalidUserSession(empty);
		testInvalidUserSession("\r ");
		testInvalidUserSession(" \t");
		testInvalidUserSession("abc");
	}

	private boolean testIsSessionValid(String sessionId) {
		boolean isSessionValid = false;
		try {
			isSessionValid = isValidSession(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("unexpected session validation failure: " + e.getMessage());
		}
		return isSessionValid;
	}

	private void testValidUserSession(String sessionId) {
		Assert.assertTrue("valid session considered invalid: " + sessionId + " should be valid but was considered invalid.",
				testIsSessionValid(sessionId));
	}

	private void testInvalidUserSession(String sessionId) {
		Assert.assertFalse("invalid session considered valid: " + sessionId + " should be invalid but was considered valid.",
				testIsSessionValid(sessionId));
	}

}
