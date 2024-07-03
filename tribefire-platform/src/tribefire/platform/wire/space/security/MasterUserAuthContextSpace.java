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
package tribefire.platform.wire.space.security;

import java.util.function.Supplier;

import com.braintribe.model.processing.securityservice.commons.provider.SessionIdFromUserSessionProvider;
import com.braintribe.model.processing.securityservice.commons.provider.StaticUserSessionHolder;
import com.braintribe.model.processing.securityservice.commons.scope.StandardUserSessionScoping;
import com.braintribe.model.user.User;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.MasterUserAuthContextContract;
import tribefire.platform.wire.space.rpc.RpcSpace;
import tribefire.platform.wire.space.security.services.UserSessionServiceSpace;

@Managed
public class MasterUserAuthContextSpace implements MasterUserAuthContextContract {

	@Import
	private RpcSpace rpc;

	@Import
	private UserSessionServiceSpace userSessionsService;

	@Import
	private CurrentUserAuthContextSpace currentUserAuthContext;

	@Managed
	public User user() {
		User bean = User.T.create();
		bean.setId("master");
		bean.setName("master");
		return bean;
	}

	@Override
	@Managed
	public StandardUserSessionScoping userSessionScoping() {
		StandardUserSessionScoping bean = new StandardUserSessionScoping();
		bean.setRequestEvaluator(rpc.serviceRequestEvaluator());
		bean.setDefaultUserSessionSupplier(userSessionProvider());
		bean.setUserSessionStack(currentUserAuthContext.userSessionStack());
		return bean;
	}

	@Managed
	public StaticUserSessionHolder userSessionProvider() {
		StaticUserSessionHolder bean = new StaticUserSessionHolder();
		bean.setUserSession(userSessionsService.internalUserSession(user()));
		return bean;
	}

	@Managed
	public Supplier<String> userSessionIdProvider() {
		SessionIdFromUserSessionProvider bean = new SessionIdFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionProvider());
		return bean;
	}

}
