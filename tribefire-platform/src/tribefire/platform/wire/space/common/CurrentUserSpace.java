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
package tribefire.platform.wire.space.common;

import java.util.Map;
import java.util.function.Supplier;

import com.braintribe.model.processing.securityservice.commons.provider.SessionIdFromUserSessionProvider;
import com.braintribe.model.processing.service.common.context.UserSessionStack;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.rpc.GmWebRpcClientMetaDataProvider;

@Managed
public class CurrentUserSpace implements WireSpace {
	
	public Supplier<UserSession> userSessionProvider() {
		return userSessionStack();
	}

	@Managed
	public UserSessionStack userSessionStack() {
		UserSessionStack bean = new UserSessionStack();
		
		return bean;
	}

	@Managed
	public Supplier<String> userSessionIdProvider() {
		SessionIdFromUserSessionProvider bean = new SessionIdFromUserSessionProvider();
		bean.setUserSessionProvider(userSessionStack());
		return bean;
	}

	@Managed
	public Supplier<Map<String, Object>> clientMetaDataProvider() {
		GmWebRpcClientMetaDataProvider bean = new GmWebRpcClientMetaDataProvider();
		bean.setSessionIdProvider(userSessionIdProvider());
		bean.setIncludeNdc(true);
		bean.setIncludeNodeId(true);
		bean.setIncludeThreadName(true);
		return bean;
	}

}
