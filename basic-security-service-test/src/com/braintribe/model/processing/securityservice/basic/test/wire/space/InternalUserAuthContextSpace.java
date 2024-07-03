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

import static com.braintribe.wire.api.util.Sets.set;

import java.util.Set;
import java.util.function.Supplier;

import com.braintribe.gm.service.wire.common.contract.CommonServiceProcessingContract;
import com.braintribe.model.processing.securityservice.commons.provider.SessionIdFromUserSessionProvider;
import com.braintribe.model.processing.securityservice.commons.provider.StaticUserSessionHolder;
import com.braintribe.model.processing.securityservice.commons.scope.StandardUserSessionScoping;
import com.braintribe.model.user.Role;
import com.braintribe.model.user.User;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class InternalUserAuthContextSpace implements WireSpace {

	protected static final Set<String> internalRoles = set("tf-internal");

	@Import
	private SecurityServiceSpace securityService;

	@Import
	private UserSessionServiceSpace userSessionsService;

	@Import
	private CurrentUserAuthContextSpace currentUserAuthContext;
	
	@Import
	private CommonServiceProcessingContract commonServiceProcessing;

	@Managed
	public User user() {

		User bean = User.T.create();
		bean.setId("internal");
		bean.setName("internal");

		for (String internalRole : internalRoles) {
			Role tfInternal = Role.T.create();
			tfInternal.setId(internalRole);
			tfInternal.setName(internalRole);
			bean.getRoles().add(tfInternal);
		}


		return bean;

	}

	@Managed
	public StandardUserSessionScoping userSessionScoping() {
		StandardUserSessionScoping bean = new StandardUserSessionScoping();
		bean.setDefaultUserSessionSupplier(userSessionProvider());
		bean.setRequestEvaluator(commonServiceProcessing.evaluator());
		bean.setUserSessionStack(currentUserAuthContext.userSessionStack());
		return bean;
	}

	@Managed
	public StaticUserSessionHolder userSessionProvider() {
		StaticUserSessionHolder bean = new StaticUserSessionHolder();
		bean.setUserSession(userSessionsService.initialUserSession(user()));
		return bean;
	}

	@Managed
	public Supplier<String> userSessionIdProvider() {
		SessionIdFromUserSessionProvider bean = new SessionIdFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionProvider());
		return bean;
	}

	@Managed
	public Supplier<Set<String>> rolesProvider() {
		return () -> {return internalRoles;};
	}
}
