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
package tribefire.platform.wire.space.security.servlets;

import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.linkedMap;
import static com.braintribe.wire.api.util.Sets.set;

import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import com.braintribe.exception.AuthorizationException;
import com.braintribe.exception.LogPreferences;
import com.braintribe.logging.Logger;
import com.braintribe.logging.Logger.LogLevel;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.security.manipulation.IllegalManipulationException;
import com.braintribe.model.processing.securityservice.api.exceptions.SecurityServiceException;
import com.braintribe.model.processing.session.api.managed.NotFoundException;
import com.braintribe.model.processing.web.cors.CortexCorsHandler;
import com.braintribe.servlet.exception.ExceptionFilter;
import com.braintribe.servlet.exception.StandardExceptionHandler;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.lcd.CollectionTools2;
import com.braintribe.web.cors.CorsFilter;
import com.braintribe.web.credentials.extractor.BasicAuthCredentialsProvider;
import com.braintribe.web.credentials.extractor.ExistingSessionFromCookieProvider;
import com.braintribe.web.credentials.extractor.ExistingSessionFromHeaderParameterProvider;
import com.braintribe.web.credentials.extractor.ExistingSessionFromRequestParameterProvider;
import com.braintribe.web.credentials.extractor.JwtCredentialsProvider;
import com.braintribe.web.multipart.api.MalformedMultipartDataException;
import com.braintribe.web.servlet.auth.AuthFilter;
import com.braintribe.web.servlet.auth.AuthServlet;
import com.braintribe.web.servlet.auth.LoginServlet;
import com.braintribe.web.servlet.auth.LogoutServlet;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.contract.ErrorHandlingRuntimePropertiesContract;
import tribefire.platform.wire.space.common.HttpSpace;
import tribefire.platform.wire.space.common.MarshallingSpace;
import tribefire.platform.wire.space.common.MessagingSpace;
import tribefire.platform.wire.space.common.RuntimeSpace;
import tribefire.platform.wire.space.cortex.accesses.CortexAccessSpace;
import tribefire.platform.wire.space.module.WebPlatformReflectionSpace;
import tribefire.platform.wire.space.rpc.RpcSpace;
import tribefire.platform.wire.space.security.AuthContextSpace;
import tribefire.platform.wire.space.security.accesses.AuthAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserSessionsAccessSpace;
import tribefire.platform.wire.space.system.servlets.ServletsSpace;

@Managed
public class SecurityServletSpace implements WireSpace {

	private static final Logger logger = Logger.getLogger(SecurityServletSpace.class);

	// @formatter:off
	@Import	private AuthAccessSpace authAccess;
	@Import	private AuthContextSpace authContext;
	@Import	private CortexAccessSpace cortexAccess;
	@Import	private ErrorHandlingRuntimePropertiesContract errorHandlingRuntimeProperties;
	@Import	private HttpSpace http;
	@Import	private MarshallingSpace marshalling;
	@Import	private RpcSpace rpc;
	@Import	private RuntimeSpace runtime;
	@Import	private ServletsSpace servlets;
	@Import	private UserSessionsAccessSpace userSessionsAccess;
	@Import	protected MessagingSpace messaging;
	@Import private WebPlatformReflectionSpace webPlatformReflection;
	// @formatter:on

	@Managed
	public CorsFilter corsFilter() {
		CorsFilter bean = new CorsFilter();
		bean.setCorsHandler(cortexCorsHandler());
		return bean;
	}

	@Managed
	public CortexCorsHandler cortexCorsHandler() {
		CortexCorsHandler bean = new CortexCorsHandler();
		bean.setGmSessionProvider(cortexAccess::lowLevelSession);
		return bean;
	}

	@Managed
	public AuthFilter authFilterStrict() {
		AuthFilter bean = new AuthFilter();
		bean.setStrict(true);
		configureAuthFilter(bean);
		return bean;
	}

	@Managed
	public AuthFilter authFilterAdminStrict() {
		AuthFilter bean = new AuthFilter();
		bean.setStrict(true);
		bean.setGrantedRoles(set("tf-admin", "tf-locksmith"));
		configureAuthFilter(bean);
		return bean;
	}

	@Managed
	public AuthFilter authFilterLenient() {
		AuthFilter bean = new AuthFilter();
		bean.setStrict(false);
		configureAuthFilter(bean);
		return bean;
	}

	@Managed
	public LoginServlet loginServlet() {
		LoginServlet bean = new LoginServlet();
		return bean;
	}

	@Managed
	public AuthServlet loginAuthServlet() {
		AuthServlet bean = new AuthServlet();
		bean.setCookieHandler(http.cookieHandler());
		bean.setRemoteAddressResolver(servlets.remoteAddressResolver());
		bean.setMarshallerRegistry(marshalling.registry());
		bean.setRequestEvaluator(rpc.systemServiceRequestEvaluator());
		bean.setEntryPointProvider(http.openUserSessionConfigurationProvider()::findEntryPointName);

		return bean;
	}

	@Managed
	public LogoutServlet logoutServlet() {
		LogoutServlet bean = new LogoutServlet();
		bean.setRequestEvaluator(rpc.serviceRequestEvaluator());

		String relativeSignInPath = TribefireRuntime.getProperty(TribefireRuntime.ENVIRONMENT_TRIBEFIRE_WEB_LOGIN_RELATIVE_PATH);
		if (!StringTools.isBlank(relativeSignInPath)) {
			bean.setRelativeLoginPath(relativeSignInPath);
		}

		return bean;
	}

	protected void configureAuthFilter(AuthFilter authFilter) {
		authFilter.setRequestEvaluator(rpc.systemServiceRequestEvaluator());
		if (logger.isDebugEnabled()) {
			authFilter.setThreadRenamer(runtime.threadRenamer());
		}
		authFilter.setThrowExceptionOnAuthFailure(true);
		authFilter.setEntryPointProvider(http.openUserSessionConfigurationProvider()::findEntryPoint);

		authFilter.addWebCredentialProvider("cookie", existingSessionFromCookieProvider());
		authFilter.addWebCredentialProvider("request-parameter", existingSessionFromRequestParameterProvider());
		authFilter.addWebCredentialProvider("basic", basicAuthCredentialsProvider());
		authFilter.addWebCredentialProvider("jwt", jwtCredentialsProvider());
		authFilter.addWebCredentialProvider("header-parameter", existingSessionFromHeaderParameterProvider());

		String relativeSignInPath = TribefireRuntime.getProperty(TribefireRuntime.ENVIRONMENT_TRIBEFIRE_WEB_LOGIN_RELATIVE_PATH);
		if (!StringTools.isBlank(relativeSignInPath)) {
			authFilter.setRelativeLoginPath(relativeSignInPath);
		}
	}

	@Managed
	private ExistingSessionFromCookieProvider existingSessionFromCookieProvider() {
		ExistingSessionFromCookieProvider bean = new ExistingSessionFromCookieProvider();
		bean.setSessionCookieNameProvider(http.sessionCookieNameSupplier());
		return bean;
	}

	@Managed
	private ExistingSessionFromHeaderParameterProvider existingSessionFromHeaderParameterProvider() {
		return new ExistingSessionFromHeaderParameterProvider();
	}

	@Managed
	private ExistingSessionFromRequestParameterProvider existingSessionFromRequestParameterProvider() {
		return new ExistingSessionFromRequestParameterProvider();
	}

	@Managed
	private BasicAuthCredentialsProvider basicAuthCredentialsProvider() {
		return new BasicAuthCredentialsProvider();
	}

	@Managed
	private JwtCredentialsProvider jwtCredentialsProvider() {
		return new JwtCredentialsProvider();
	}

	@Managed
	public Filter exceptionFilter() {
		ExceptionFilter bean = new ExceptionFilter();
		bean.setExceptionHandlers(CollectionTools2.asSet(standardExceptionHandler()));
		return bean;
	}

	@Managed
	public StandardExceptionHandler standardExceptionHandler() {
		StandardExceptionHandler bean = new StandardExceptionHandler();
		bean.setExceptionExposure(errorHandlingRuntimeProperties.TRIBEFIRE_ERROR_EXCEPTION_EXPOSURE());
		bean.setTracebackIdExposure(errorHandlingRuntimeProperties.TRIBEFIRE_ERROR_TRACEBACKID_EXPOSURE());
		bean.setMarshallerRegistry(marshalling.registry());
		if (errorHandlingRuntimeProperties.TRIBEFIRE_ERROR_HANDLING_PRE_REASON())
			bean.setStatusCodeMap(preReasonExceptionStatusCodeMap());
		else
			bean.setStatusCodeMap(exceptionStatusCodeMap());
		bean.setLogPreferencesMap(exceptionLogPreferencesMap());
		bean.setRemoteAddressResolver(servlets.remoteAddressResolver());
		return bean;
	}

	@Managed
	private Map<Class<? extends Throwable>, Integer> preReasonExceptionStatusCodeMap() {
		//@formatter:off
		return linkedMap(
				entry(IllegalArgumentException.class, HttpServletResponse.SC_BAD_REQUEST),
				entry(UnsupportedOperationException.class, HttpServletResponse.SC_NOT_IMPLEMENTED),
				entry(NotFoundException.class, HttpServletResponse.SC_NOT_FOUND),
				entry(AuthorizationException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(SecurityServiceException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(IllegalManipulationException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(MalformedMultipartDataException.class, HttpServletResponse.SC_BAD_REQUEST)
		);
		//@formatter:on
	}

	@Managed
	private Map<Class<? extends Throwable>, Integer> exceptionStatusCodeMap() {
		//@formatter:off
		return linkedMap(
				entry(AuthorizationException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(SecurityServiceException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(MalformedMultipartDataException.class, HttpServletResponse.SC_BAD_REQUEST)
				);
		//@formatter:on
	}

	@Managed
	private Map<Class<? extends Throwable>, LogPreferences> exceptionLogPreferencesMap() {
		//@formatter:off
		return linkedMap(
				entry(IllegalArgumentException.class, infoLogPreferences()),
				entry(NotFoundException.class, infoLogPreferences()),
				entry(AuthorizationException.class, infoLogPreferences()),
				entry(SecurityServiceException.class, infoLogPreferences()),
				entry(IllegalManipulationException.class, infoLogPreferences()),
				entry(MalformedMultipartDataException.class, infoLogPreferences())
		);
		//@formatter:on
	}

	@Managed
	private LogPreferences infoLogPreferences() {
		LogPreferences bean = new LogPreferences(LogLevel.INFO, false, LogLevel.TRACE);
		return bean;
	}

}
