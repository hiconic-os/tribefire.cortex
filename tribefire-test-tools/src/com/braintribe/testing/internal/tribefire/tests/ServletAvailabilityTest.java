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
package com.braintribe.testing.internal.tribefire.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.BeforeClass;

import com.braintribe.product.rat.imp.ImpApiFactory;
import com.braintribe.utils.IOTools;

public abstract class ServletAvailabilityTest extends AbstractTribefireQaTest {

	protected static String sessionId;
	private static String publicServicesUrl;

	@BeforeClass
	public static void beforeClass() throws Exception {
		publicServicesUrl = apiFactory().getURL();
		sessionId = authenticate();
	}

	/**
	 * @return a valid sessionId
	 */
	protected static String authenticate() {
		return apiFactory().build().session().getSessionAuthorization().getSessionId();
	}
	
	/**
	 * Asserts that given URL returns a 200 response code.
	 * 
	 * @param relativeUrl URL relative to the URL of the tested tribefire services ( {@link ImpApiFactory#getURL()} )
	 */
	protected void assertServletAvailability(String relativeUrl) throws IOException, ClientProtocolException {
		CloseableHttpResponse response = httpGet(relativeUrl);
		
		assertThat(response).isNotNull();
		int statusCode = response.getStatusLine().getStatusCode();
		
		logger.info("Endpoint returns HTTP status code " + statusCode + " for url " + relativeUrl);
		
		assertThat(statusCode).isEqualTo(200);
	}

	/**
	 * @param relativeUrl URL relative to the URL of the tested tribefire services ( {@link ImpApiFactory#getURL()} )
	 */
	protected static CloseableHttpResponse httpGet(String relativeUrl) throws IOException, ClientProtocolException {
		CloseableHttpResponse response;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			response = httpClient.execute(new HttpGet(publicServicesUrl + relativeUrl));
		}
		return response;
	}

	/**
	 * Executes a GET request and returns the content of the response body.
	 * 
	 * @param relativeUrl URL relative to the URL of the tested tribefire services ( {@link ImpApiFactory#getURL()} )
	 * @return HTTP response body as UTF-8 string
	 */
	protected static String httpGetContent(String relativeUrl) throws Exception {
		try (InputStream in = httpGet(relativeUrl).getEntity().getContent()) {
			return IOTools.slurp(in, "UTF-8");
		}
	}
}