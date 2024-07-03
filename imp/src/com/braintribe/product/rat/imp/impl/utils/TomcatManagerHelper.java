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
package com.braintribe.product.rat.imp.impl.utils;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.tfconstants.TribefireComponent;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.utils.Base64;
import com.braintribe.utils.lcd.CommonTools;

/**
 * A helper class than provides convenient methods to trigger commands on the <strong>Tomcat Manager</strong>
 * application. For example one can start, stop, reload tribefire services.
 */
public class TomcatManagerHelper {

	private static Logger logger = Logger.getLogger(TomcatManagerHelper.class);

	private final String tomcatManagerUrl;
	private final String user;
	private final String password;
	private boolean failOnError = false;

	public TomcatManagerHelper(String tomcatBaseUrl, String user, String password) {
		this.tomcatManagerUrl = tomcatBaseUrl + "/manager";
		this.user = user;
		this.password = password;
		logger.debug("Created tomcat helper with "
				+ CommonTools.getParametersString("tomcatManagerUrl", tomcatManagerUrl, "user", user, "password", password));
	}

	public TomcatManagerHelper failOnError() {
		failOnError = true;
		return this;
	}

	public int reload(TribefireComponent tribefireComponent) {
		return applyCommandOnWebContainer("reload", TribefireConstants.getComponentUrlName(tribefireComponent));
	}

	public int start(TribefireComponent tribefireComponent) {
		return applyCommandOnWebContainer("start", TribefireConstants.getComponentUrlName(tribefireComponent));
	}

	public int stop(TribefireComponent tribefireComponent) {
		return applyCommandOnWebContainer("stop", TribefireConstants.getComponentUrlName(tribefireComponent));
	}

	/**
	 * Requests an existing application to apply an action (e.g. reload, start, stop, undeploy, deploy) via an HTTP
	 * request without having to shutdown the entire web container.
	 *
	 * @param command
	 *            the action to be applied
	 * @param webapp
	 *            the web application for which the command is applied. In addition the web application is attached to
	 *            the context path <code>/webapp</code>
	 * @return the status code of the respective HTTP response
	 */
	private int applyCommandOnWebContainer(String command, String webapp) {
		logger.debug("Applying command '" + command + "' on webapp '" + webapp + "' ...");
		String url = tomcatManagerUrl + "/text/" + command + "?path=/" + webapp;
		int responseCode = sendHttpRequest(url);
		if (failOnError && responseCode != 200) {
			throw new RuntimeException("Couldn't apply command '" + command + "' on webapp '" + webapp + "'! Error code: " + responseCode);
		}
		return responseCode;
	}

	private int sendHttpRequest(String url) {
		logger.debug("Sending http request: " + url);
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String encoding = Base64.encodeString(user + ":" + password);
		HttpGet httppost = new HttpGet(url);
		httppost.setHeader("Authorization", "Basic " + encoding);

		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httppost);
		} catch (IOException e) {
			throw new UncheckedIOException("Http call failed requesting [" + url + "].", e);
		}

		int responseCode = httpResponse.getStatusLine().getStatusCode();
		logger.debug("Applied command. Response Code: " + responseCode);
		if (responseCode != 200) {
			logger.warn("Requesting url '" + url + "' returned response code " + responseCode + ". Reason: "
					+ httpResponse.getStatusLine().getReasonPhrase());
		}
		return httpResponse.getStatusLine().getStatusCode();
	}
}
