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
package com.braintribe.web.servlet.auth.cookie;

import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.braintribe.cfg.Configurable;
import com.braintribe.common.attribute.common.Waypoint;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.securityservice.OpenUserSessionWithUserAndPassword;
import com.braintribe.utils.collection.impl.AttributeContexts;
import com.braintribe.web.servlet.auth.Constants;
import com.braintribe.web.servlet.auth.CookieHandler;
import com.braintribe.web.servlet.auth.providers.CookieProvider;

public class DefaultCookieHandler implements CookieHandler {

	private static final Logger log = Logger.getLogger(DefaultCookieHandler.class);

	private int cookieExpiry = 24 * 60 * 60; // 24h
	private String cookiePath = null;
	private String cookieDomain = null;
	private Function<HttpServletRequest, Cookie> sessionCookieProvider = new CookieProvider(Constants.COOKIE_SESSIONID);
	private boolean addSessionCookie = true;
	private Map<String, Boolean> cookieHttpOnlyPerWaypoint = Map.of("default", Boolean.FALSE);

	@Override
	public Cookie ensureCookie(HttpServletRequest req, HttpServletResponse resp, String sessionId) {
		return this.ensureCookie(req, resp, sessionId, null);
	}

	@Override
	public Cookie ensureCookie(HttpServletRequest req, HttpServletResponse resp, String sessionId,
			OpenUserSessionWithUserAndPassword openUserSessionRequest) {
		boolean staySignedInRequested = false;
		if (openUserSessionRequest != null) {
			staySignedInRequested = openUserSessionRequest.getStaySignedIn();
		}
		return ensureCookie(req, resp, sessionId, staySignedInRequested);
	}

	@Override
	public Cookie ensureCookie(HttpServletRequest req, HttpServletResponse resp, String sessionId, boolean staySignedIn) {

		if (!addSessionCookie) {
			log.trace(() -> "Session cookies are disabled.");
			return null;
		}

		Cookie sessionCookie = acquireCookie(req, sessionId);
		sessionCookie.setMaxAge(getMaxAge(req, staySignedIn));

		String cookiePath = this.cookiePath;
		if (cookiePath == null) {
			cookiePath = "/"; // if not explicitly configured we enable this cookie for all paths on the domain.
		}
		sessionCookie.setPath(cookiePath);

		if (this.cookieDomain != null) {
			sessionCookie.setDomain(this.cookieDomain);
		}

		String scheme = req != null ? req.getScheme() : "https";
		if (scheme != null && scheme.equalsIgnoreCase("https")) {
			sessionCookie.setSecure(true);
		}

		sessionCookie.setHttpOnly(httpOnlyCookie());

		resp.addCookie(sessionCookie);

		return sessionCookie;
	}

	@Override
	public void invalidateCookie(HttpServletRequest req, HttpServletResponse resp, String sessionId) {

		Cookie sessionCookie = sessionCookieProvider.apply(req);
		if (sessionCookie != null) {
			sessionCookie.setMaxAge(0);
			sessionCookie.setValue("");

			String cookiePath = this.cookiePath;
			if (cookiePath == null) {
				cookiePath = "/";
			}

			sessionCookie.setPath(cookiePath);

			if (this.cookieDomain != null) {
				sessionCookie.setDomain(this.cookieDomain);
			}

			String scheme = req.getScheme();
			if (scheme != null && scheme.equalsIgnoreCase("https")) {
				sessionCookie.setSecure(true);
			}

			sessionCookie.setHttpOnly(true);

			resp.addCookie(sessionCookie);
		}
	}

	private boolean httpOnlyCookie() {
		String waypoint = AttributeContexts.peek().findOrDefault(Waypoint.class, "default");
		Boolean httpOnlyForWaypoint = cookieHttpOnlyPerWaypoint.get(waypoint);
		if (httpOnlyForWaypoint == null && !waypoint.equals("default")) {
			httpOnlyForWaypoint = cookieHttpOnlyPerWaypoint.get("default");
		}
		if (httpOnlyForWaypoint != null) {
			return httpOnlyForWaypoint;
		}
		return false;
	}

	private Cookie acquireCookie(HttpServletRequest req, String sessionId) throws RuntimeException {
		Cookie sessionCookie = req != null ? sessionCookieProvider.apply(req) : null;
		if (sessionCookie == null) {
			sessionCookie = new Cookie(Constants.COOKIE_SESSIONID, sessionId);
		} else {
			sessionCookie.setValue(sessionId);
		}
		return sessionCookie;
	}

	private int getMaxAge(HttpServletRequest req, boolean staySignedInRequested) {

		if (!offerStaySigned()) {
			if (log.isTraceEnabled())
				log.trace(Constants.TRIBEFIRE_RUNTIME_OFFER_STAYSIGNED
						+ " is disabled. Any user-session cookie will be invalid after a browser restart.");
			return -1;
		}

		if (!staySignedInRequested) {
			return -1;
		}

		String staySigned = req.getParameter(Constants.REQUEST_PARAM_STAYSIGNED);
		if (staySigned != null && staySigned.equals(Boolean.TRUE.toString())) {
			staySignedInRequested = true;
		}

		if (staySignedInRequested) {
			return cookieExpiry;
		} else {
			return -1;
		}
	}

	protected static boolean offerStaySigned() {
		String offerStayLoggedIn = TribefireRuntime.getProperty(Constants.TRIBEFIRE_RUNTIME_OFFER_STAYSIGNED);
		if (offerStayLoggedIn != null && offerStayLoggedIn.equalsIgnoreCase("false")) {
			return false;
		} else {
			return true;
		}
	}

	@Configurable
	public void setCookieExpiry(int cookieExpiry) {
		this.cookieExpiry = cookieExpiry;
	}
	@Configurable
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}
	@Configurable
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}
	@Configurable
	public void setSessionCookieProvider(Function<HttpServletRequest, Cookie> sessionCookieProvider) {
		this.sessionCookieProvider = sessionCookieProvider;
	}
	@Configurable
	public void setAddCookie(boolean addCookie) {
		this.addSessionCookie = addCookie;
	}
	@Configurable
	public void setCookieHttpOnlyPerWaypoint(Map<String, Boolean> cookieHttpOnlyMap) {
		if (cookieHttpOnlyMap != null) {
			this.cookieHttpOnlyPerWaypoint = cookieHttpOnlyMap;
		}
	}
}
