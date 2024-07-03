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
package tribefire.platform.wire.space.security.services;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.securityservice.basic.MetaDataDispatchingAuthenticator;
import com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor;
import com.braintribe.model.processing.securityservice.basic.verification.SameOriginUserSessionVerification;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.securityservice.SecurityRequest;
import com.braintribe.web.servlet.auth.WebLogoutInterceptor;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.bindings.BindingsSpace;
import tribefire.platform.wire.space.common.EnvironmentSpace;
import tribefire.platform.wire.space.common.HttpSpace;
import tribefire.platform.wire.space.common.MarshallingSpace;
import tribefire.platform.wire.space.common.ResourceProcessingSpace;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;
import tribefire.platform.wire.space.cortex.accesses.CortexAccessSpace;
import tribefire.platform.wire.space.rpc.RpcSpace;
import tribefire.platform.wire.space.security.AuthContextSpace;
import tribefire.platform.wire.space.security.AuthenticatorsSpace;
import tribefire.platform.wire.space.security.accesses.AuthAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserStatisticsAccessSpace;

@Managed
public class SecurityServiceSpace implements WireSpace {

	private static final String serviceId = "SECURITY";

	@Import
	private AuthenticatorsSpace authExperts;

	@Import
	private AuthContextSpace authContext;

	@Import
	private UserSessionServiceSpace userSessionService;

	@Import
	private UserStatisticsAccessSpace userStatisticsAccess;

	@Import
	private EnvironmentSpace environment;

	@Import
	private RpcSpace rpc;

	@Import
	protected HttpSpace http;

	@Import
	private MarshallingSpace marshalling;

	@Import
	private CortexAccessSpace cortexAccess;

	@Import
	private BindingsSpace bindings;

	@Import
	private ResourceProcessingSpace resourceProcessing;

	@Import
	protected GmSessionsSpace gmSessions;

	@Import
	private AuthAccessSpace authAccess;

	public String serviceId() {
		return serviceId;
	}

	@Managed
	public WebLogoutInterceptor webLogoutInterceptor() {
		WebLogoutInterceptor bean = new WebLogoutInterceptor();
		bean.setCookieHandler(http.cookieHandler());
		return bean;
	}

	@Managed
	public MetaDataDispatchingAuthenticator authenticator() {
		MetaDataDispatchingAuthenticator bean = new MetaDataDispatchingAuthenticator();
		bean.setCortexModelAccessorySupplier(() -> gmSessions.userModelAccessoryFactory().getForServiceDomain("cortex"));
		return bean;
	}

	@Managed
	public ServiceProcessor<SecurityRequest, Object> securityServiceProcessor() {
		SecurityServiceProcessor bean = new SecurityServiceProcessor();
		bean.setUserSessionService(userSessionService.service());
		bean.setEvaluator(rpc.serviceRequestEvaluator());
		bean.setUserSessionAccessVerificationExperts(asList(sameOriginUserSessionVerification()));
		bean.setSessionMaxIdleTime(userSessionService.defaultMaxIdleTime());
		bean.setAuthGmSessionProvider(authAccess::lowLevelSession);
		// user statistics
		bean.setEnableUserStatistics(statisticsEnabled());
		bean.setUserStatisticsGmSessionProvider(userStatisticsAccess::lowLevelSession);
		return bean;
	}

	private Boolean statisticsEnabled() {
		return environment.property(TribefireRuntime.ENVIRONMENT_USER_SESSIONS_STATISTICS_ENABLED, Boolean.class, Boolean.TRUE);
	}

	@Managed
	private SameOriginUserSessionVerification sameOriginUserSessionVerification() {
		SameOriginUserSessionVerification bean = new SameOriginUserSessionVerification();
		bean.setAllowAccessToUserSessionsWithNoCreationIp(true);
		bean.setIgnoreSourceIpNullValue(true);
		return bean;
	}

}
