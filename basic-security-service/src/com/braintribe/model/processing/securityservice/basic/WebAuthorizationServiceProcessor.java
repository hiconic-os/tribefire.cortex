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
package com.braintribe.model.processing.securityservice.basic;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InternalError;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.UnsupportedOperation;
import com.braintribe.gm.model.security.reason.AuthenticationFailure;
import com.braintribe.gm.model.security.reason.Forbidden;
import com.braintribe.gm.model.security.reason.MissingSession;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.reflection.ConfigurableCloningContext;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.common.context.UserSessionAspect;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.securityservice.OpenUserSession;
import com.braintribe.model.securityservice.OpenUserSessionResponse;
import com.braintribe.model.securityservice.credentials.UserPasswordCredentials;
import com.braintribe.model.securityservice.web.GetWebAuthorization;
import com.braintribe.model.securityservice.web.UserPassWebAuthenticate;
import com.braintribe.model.securityservice.web.WebAuthorization;
import com.braintribe.model.securityservice.web.WebAuthorizationRequest;
import com.braintribe.model.user.User;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.util.servlet.HttpServletArguments;
import com.braintribe.util.servlet.HttpServletArgumentsAttribute;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.web.servlet.auth.CookieHandler;

/**
 * The service processor that handles all the simplified open user session requests.
 *
 */
public class WebAuthorizationServiceProcessor extends AbstractDispatchingServiceProcessor<WebAuthorizationRequest, WebAuthorization> {
	private static final Logger logger = Logger.getLogger(WebAuthorizationServiceProcessor.class);
	private CookieHandler cookieHandler;

	@Configurable
	@Required
	public void setCookieHandler(CookieHandler cookieHandler) {
		this.cookieHandler = cookieHandler;
	}
	
	@Override
	protected void configureDispatching(DispatchConfiguration<WebAuthorizationRequest, WebAuthorization> dispatching) {
		dispatching.registerReasoned(UserPassWebAuthenticate.T, this::userPassWebAuthenticate);
		dispatching.registerReasoned(GetWebAuthorization.T, this::getWebAuthorization);
	}

	private Maybe<? extends WebAuthorization> userPassWebAuthenticate(ServiceRequestContext requestContext,
			UserPassWebAuthenticate request) {
		HttpServletArguments servletArguments = requestContext.findOrNull(HttpServletArgumentsAttribute.class);
		
		if (servletArguments == null)
			return Reasons.build(UnsupportedOperation.T).text("Operation unsupported via non-HTTP endpoint.").toMaybe();

		Reason validationError = validate(request);
		
		if (validationError != null)
			return validationError.asMaybe();

		
		OpenUserSession openUserSession = OpenUserSession.T.create();
		openUserSession.setLocale(request.getLocale());
		openUserSession.setMetaData(request.getMetaData());
		openUserSession.setCredentials(UserPasswordCredentials.forUserName(request.getUser(), request.getPassword()));
		
		Maybe<? extends OpenUserSessionResponse> responseMaybe = openUserSession.eval(requestContext).getReasoned();
		
		if (responseMaybe.isUnsatisfied()) {
			
			if (responseMaybe.isUnsatisfiedAny(AuthenticationFailure.T, Forbidden.T))
				return responseMaybe.propagateReason();
			
			return InternalError.createTraceback(//
					responseMaybe.whyUnsatisfied(), //
					"Error while authenticating", //
					logger::error //
					).asMaybe();
		}
		
		UserSession userSession = responseMaybe.get().getUserSession();
		
		cookieHandler.ensureCookie(servletArguments.getRequest(), servletArguments.getResponse(), userSession.getSessionId(), request.getStaySignedIn());
		
		return Maybe.complete(buildAuthorization(userSession));
	}

	private WebAuthorization buildAuthorization(UserSession userSession) {
		WebAuthorization authorization = WebAuthorization.T.create();
		authorization.setEffectiveRoles(userSession.getEffectiveRoles());
		
		User user = userSession.getUser();
		
		User clonedUser = user.clone(ConfigurableCloningContext.build().done());
		clonedUser.setPassword(null);
		
		authorization.setUser(clonedUser);
		return authorization;
	}
	
	private Reason validate(UserPassWebAuthenticate request) {
		if (StringTools.isBlank(request.getUser()))
			return Reasons.build(InvalidArgument.T).text("Property user must not be empty or null").toReason();
		
		if (request.getPassword() == null)
			return Reasons.build(InvalidArgument.T).text("Property password must not be null").toReason();
		
		
		return null;
	}

	private Maybe<? extends WebAuthorization> getWebAuthorization(ServiceRequestContext requestContext,
			@SuppressWarnings("unused") GetWebAuthorization request) {
		UserSession userSession = requestContext.findOrNull(UserSessionAspect.class);
		
		if (userSession == null)
			return Reasons.build(MissingSession.T).text("No session found for web authorization").toMaybe();
		
		return Maybe.complete(buildAuthorization(userSession));
	}

}
