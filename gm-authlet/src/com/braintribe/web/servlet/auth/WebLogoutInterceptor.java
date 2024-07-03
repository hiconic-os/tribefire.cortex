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
package com.braintribe.web.servlet.auth;

import com.braintribe.cfg.Required;
import com.braintribe.model.processing.service.api.ServicePostProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.util.servlet.HttpServletArguments;
import com.braintribe.util.servlet.HttpServletArgumentsAttribute;

public class WebLogoutInterceptor implements ServicePostProcessor<Object> {
	private CookieHandler cookieHandler;

	@Required
	public void setCookieHandler(CookieHandler cookieHandler) {
		this.cookieHandler = cookieHandler;
	}

	@Override
	public Object process(ServiceRequestContext requestContext, Object response) {
		String sessionId = requestContext.getRequestorSessionId();

		if (sessionId == null)
			return response;

		requestContext.findAttribute(HttpServletArgumentsAttribute.class).ifPresent(a -> clearCookie(sessionId, a));
		return response;
	}

	private void clearCookie(String sessionId, HttpServletArguments servletArguments) {
		cookieHandler.invalidateCookie(servletArguments.getRequest(), servletArguments.getResponse(), sessionId);
	}
}
