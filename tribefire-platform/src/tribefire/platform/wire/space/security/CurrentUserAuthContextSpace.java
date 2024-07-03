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

import static com.braintribe.wire.api.util.Lists.list;

import java.util.Set;
import java.util.function.Supplier;

import com.braintribe.model.processing.securityservice.commons.provider.RolesFromUserSessionProvider;
import com.braintribe.model.processing.securityservice.commons.provider.SessionIdFromUserSessionProvider;
import com.braintribe.model.processing.securityservice.commons.provider.UserIpAddressFromUserSessionProvider;
import com.braintribe.model.processing.securityservice.commons.provider.UserNameFromUserSessionProvider;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.thread.api.DeferringThreadContextScoping;
import com.braintribe.thread.impl.ThreadContextScopingImpl;
import com.braintribe.utils.collection.api.MinimalStack;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.common.CurrentUserSpace;
import tribefire.platform.wire.space.rpc.RpcSpace;

@Managed
public class CurrentUserAuthContextSpace implements WireSpace {

	protected InternalUserAuthContextSpace internalUserAuthContext;

	@Import
	private RpcSpace rpc;

	@Import
	private CurrentUserSpace currentUser;

	public Supplier<UserSession> userSessionSupplier() {
		return userSessionStack()::peek;
	}

	public MinimalStack<UserSession> userSessionStack() {
		return currentUser.userSessionStack();
	}
	
	@Managed
	public Supplier<String> userSessionIdProvider() {
		SessionIdFromUserSessionProvider bean = new SessionIdFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionSupplier());
		return bean;
	}

	@Managed
	public Supplier<Set<String>> rolesProvider() {
		RolesFromUserSessionProvider bean = new RolesFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionSupplier());
		return bean;
	}

	@Managed
	public Supplier<String> userNameProvider() {
		UserNameFromUserSessionProvider bean = new UserNameFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionSupplier());
		return bean;
	}

	@Managed
	public Supplier<String> userIpProvider() {
		UserIpAddressFromUserSessionProvider bean = new UserIpAddressFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionSupplier());
		return bean;
	}

	@Managed
	public DeferringThreadContextScoping threadContextScoping() {
		ThreadContextScopingImpl bean = new ThreadContextScopingImpl();
		bean.setScopeSuppliers(list(rpc.serviceRequestContextThreadContextScopeSupplier()));
		return bean;
	}
}
