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
 * Tests for the
 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)} "touch"
 * behaviour
 *
 */
public class SessionTouchTestBase extends SecurityServiceTest {

	/**
	 * Targets:
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing valid existent {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@link UserSession#getLastAccessedDate()} must be updated to a most present date
	 * <li>No exceptions shall be thrown
	 * </ul>
	 */
	@Test
	public void testSessionTouch() throws Exception {

		if (!testConfig.getEnableExpiration()) {
			System.out.println("suppressed test as testConfig.getEnableExpiration() is " + testConfig.getEnableExpiration());
			return;
		}

		String userSessionId = openSession().getSessionId();

		Thread.sleep(19000);

		Assert.assertTrue("session " + userSessionId + " should had been considered valid.", isValidSession(userSessionId));

		Thread.sleep(19000);

		Assert.assertTrue("session " + userSessionId + " should had been considered valid again.", isValidSession(userSessionId));

		Thread.sleep(21000);

		Assert.assertFalse("session " + userSessionId + " should had been considered invalid. ", isValidSession(userSessionId));

	}

	/**
	 * Targets:
	 * {@link com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor#isSessionValid(String)}
	 * 
	 * <p>
	 * Input: sessions ids referencing invalid existent {@link UserSession} objects
	 * 
	 * <p>
	 * Assertions:
	 * <ul>
	 * <li>{@link UserSession#getLastAccessedDate()} must remain unaltered
	 * <li>No exceptions shall be thrown
	 * </ul>
	 */
	@Test
	public void testInvalidSessionTouch() throws Exception {

		if (!testConfig.getEnableExpiration()) {
			System.out.println("suppressed test as testConfig.getEnableExpiration() is " + testConfig.getEnableExpiration());
			return;
		}

		UserSession userSession = openSession();

		logout(userSession.getSessionId());

		Thread.sleep(19000);

		Assert.assertFalse("session " + userSession + " should had been considered invalid.", isValidSession(userSession.getAccessId()));
	}

}
