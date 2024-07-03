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
package com.braintribe.model.processing.securityservice.basic.test.wire.space;

import com.braintribe.gm.service.wire.common.contract.CommonServiceProcessingContract;
import com.braintribe.model.processing.securityservice.basic.HardwiredDispatchingAuthenticator;
import com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor;
import com.braintribe.model.securityservice.credentials.ExistingSessionCredentials;
import com.braintribe.model.securityservice.credentials.GrantedCredentials;
import com.braintribe.model.securityservice.credentials.TrustedCredentials;
import com.braintribe.model.securityservice.credentials.UserPasswordCredentials;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class SecurityServiceSpace implements WireSpace {

	private static final String serviceId = "SECURITY";

	@Import
	private AuthExpertsSpace authExperts;

	@Import
	private AuthContextSpace authContext;

	@Import
	private UserSessionServiceSpace userSessionService;

	@Import
	private CommonServiceProcessingContract commonServiceProcessing;

	public String serviceId() {
		return serviceId;
	}

	@Managed
	public SecurityServiceProcessor service() {
		SecurityServiceProcessor bean = new SecurityServiceProcessor();
		bean.setUserSessionService(userSessionService.service());
		bean.setEvaluator(commonServiceProcessing.evaluator());
		bean.setSessionMaxIdleTime(userSessionService.defaultMaxIdleTime());
		bean.setUserSessionService(userSessionService.service());
		bean.setSessionMaxAge(null);
		return bean;
	}

	@Managed
	public HardwiredDispatchingAuthenticator authenticator() {
		HardwiredDispatchingAuthenticator bean = new HardwiredDispatchingAuthenticator();
		bean.registerAuthenticator(UserPasswordCredentials.T, authExperts.userPasswordCredentials());
		bean.registerAuthenticator(GrantedCredentials.T, authExperts.grantedCredentials());
		bean.registerAuthenticator(ExistingSessionCredentials.T, authExperts.existingSessionCredentials());
		bean.registerAuthenticator(TrustedCredentials.T, authExperts.trustedCredentials());
		return bean;
	}
}
