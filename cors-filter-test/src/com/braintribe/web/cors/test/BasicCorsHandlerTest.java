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
package com.braintribe.web.cors.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.braintribe.web.cors.exception.OriginDeniedException;
import com.braintribe.web.cors.exception.UnsupportedMethodException;
import com.braintribe.web.cors.handler.CorsHandler;
import com.braintribe.web.cors.handler.CorsRequestType;

/**
 * <p>
 * Tests covering {@link com.braintribe.web.cors.handler.BasicCorsHandler} public methods:
 * 
 * <ul>
 * <li>{@link com.braintribe.web.cors.handler.BasicCorsHandler#handleActual(HttpServletRequest, HttpServletResponse)}
 * <li>{@link com.braintribe.web.cors.handler.BasicCorsHandler#handlePreflight(HttpServletRequest, HttpServletResponse)}
 * </ul>
 * 
 */
public class BasicCorsHandlerTest extends CorsTest {

	// =====================//
	// == ACTUAL REQUESTS ==//
	// =====================//

	@Test
	public void testSuccessfulCrossOriginRequestWithPermissiveConfiguration() throws Exception {

		HttpServletRequest request = createHttpServletRequest("POST", "http", "server-host", "http://any-origin");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(permissiveConfiguration);

		corsHandler.handleActual(request, response);

		assertCorsResponseHeaders(CorsRequestType.actual, permissiveConfiguration, request, response);

	}

	@Test
	public void testSuccessfulCrossOriginRequestWithStrictConfiguration() throws Exception {

		HttpServletRequest request = createHttpServletRequest("POST", "http", "server-host", "http://valid-origin-1");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handleActual(request, response);

		assertCorsResponseHeaders(CorsRequestType.actual, strictConfiguration, request, response);

	}

	@Test
	public void testSuccessfulCrossOriginRequestWithStrictConfigurationAndNonStandardPort() throws Exception {

		HttpServletRequest request = createHttpServletRequest("POST", "http", "server-host", "http://valid-origin-1:8080");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handleActual(request, response);

		assertCorsResponseHeaders(CorsRequestType.actual, strictConfiguration, request, response);

	}

	@Test(expected = OriginDeniedException.class)
	public void testCrossOriginRequestFromUnallowedOrigin() throws Exception {

		HttpServletRequest request = createHttpServletRequest("POST", "http", "server-host", "http://any-origin");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handleActual(request, response);

	}

	@Test(expected = OriginDeniedException.class)
	public void testCrossOriginRequestFromUnallowedOriginPort() throws Exception {

		HttpServletRequest request = createHttpServletRequest("POST", "http", "server-host", "http://valid-origin-1:7777");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handleActual(request, response);

	}

	// =========================//
	// == PRE FLIGHT REQUESTS ==//
	// =========================//

	@Test(expected = UnsupportedMethodException.class)
	public void testCrossOriginRequestFromUnallowedMethod() throws Exception {

		HttpServletRequest request = createHttpServletRequest("DELETE", "http", "server-host", "http://valid-origin-1");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handleActual(request, response);

	}

	@Test
	public void testSuccessfulPreflightRequestWithPermissiveConfiguration() throws Exception {

		HttpServletRequest request = createHttpServletRequestForPreflight("POST", "http", "server-host", "http://any-origin");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(permissiveConfiguration);

		corsHandler.handlePreflight(request, response);

		assertCorsResponseHeaders(CorsRequestType.preflight, permissiveConfiguration, request, response);

	}

	@Test
	public void testSuccessfulPreflightRequestWithStrictConfigurationAndNonStandardPort() throws Exception {

		HttpServletRequest request = createHttpServletRequestForPreflight("POST", "http", "server-host", "http://valid-origin-1:8080");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handlePreflight(request, response);

		assertCorsResponseHeaders(CorsRequestType.preflight, strictConfiguration, request, response);

	}

	@Test
	public void testSuccessfulPreflightRequestWithStrictConfiguration() throws Exception {

		HttpServletRequest request = createHttpServletRequestForPreflight("POST", "http", "server-host", "http://valid-origin-1");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handlePreflight(request, response);

		assertCorsResponseHeaders(CorsRequestType.preflight, strictConfiguration, request, response);

	}

	@Test(expected = OriginDeniedException.class)
	public void testPreflightRequestFromUnallowedOrigin() throws Exception {

		HttpServletRequest request = createHttpServletRequestForPreflight("POST", "http", "server-host", "http://any-origin");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handlePreflight(request, response);

	}

	@Test(expected = OriginDeniedException.class)
	public void testPreflightRequestFromUnallowedOriginPort() throws Exception {

		HttpServletRequest request = createHttpServletRequestForPreflight("POST", "http", "server-host", "http://valid-origin-1:7777");
		HttpServletResponse response = createHttpServletResponse();

		CorsHandler corsHandler = createBasicCorsHandler(strictConfiguration);

		corsHandler.handlePreflight(request, response);

	}

}
