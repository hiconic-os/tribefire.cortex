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
package com.braintribe.web.credentials.extractor;

import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.braintribe.cfg.Configurable;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.logging.Logger;
import com.braintribe.web.servlet.auth.Constants;
import com.braintribe.web.servlet.auth.cookie.Cookies;

public class ExistingSessionFromCookieProvider implements ExistingSessionWebCredentialProvider {
	private static final Logger logger = Logger.getLogger(ExistingSessionFromCookieProvider.class);
	
	private Function<HttpServletRequest, String> sessionCookieNameProvider = r -> Constants.COOKIE_SESSIONID;

	@Configurable
	public void setSessionCookieNameProvider(Function<HttpServletRequest, String> sessionCookieNameProvider) {
		this.sessionCookieNameProvider = sessionCookieNameProvider;
	}
	
	@Override
	public Maybe<String> findSessionId(HttpServletRequest request) {
		String cookieName = sessionCookieNameProvider.apply(request);
		
		String sessionId  = Optional.ofNullable(Cookies.findCookie(request, cookieName)).map(Cookie::getValue).orElse(null);
		
		if (sessionId == null || sessionId.isEmpty())
			return Reasons.build(NotFound.T).text("HTTP cookie " + cookieName + " not present")
					.toMaybe();
		
		logger.debug(() -> "Found session cookie with name [" + cookieName + "]");

		return Maybe.complete(sessionId);
	}

}
