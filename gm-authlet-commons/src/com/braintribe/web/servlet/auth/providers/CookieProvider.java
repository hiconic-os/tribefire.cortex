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
package com.braintribe.web.servlet.auth.providers;

import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;

public class CookieProvider implements Function<HttpServletRequest, Cookie>{
	
	private String cookieName;
	
	public CookieProvider() {
		
	}
	public CookieProvider(String cookieName) {
		this.cookieName = cookieName;
	}
	
	@Configurable @Required
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	
	
	@Override
	public Cookie apply(HttpServletRequest request) throws RuntimeException {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}

}
