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
package com.braintribe.model.processing.session.wire.common.space;

import com.braintribe.model.processing.session.wire.common.contract.RemoteAuthenticationContract;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.securityservice.credentials.UserPasswordCredentials;
import com.braintribe.model.securityservice.credentials.identification.UserNameIdentification;
import com.braintribe.wire.api.annotation.Managed;

/**
 * Please always import {@link RemoteAuthenticationContract} instead of this space directly because credentials and url
 * are configured from the outside.
 *
 * @author Neidhart.Orlich
 *
 */
@Managed
public class RemoteAuthenticationSpace implements RemoteAuthenticationContract {
	private final Credentials credentials;
	private final String url;

	public static final RemoteAuthenticationSpace CORTEX_LOCALHOST = with("http://localhost:8080/tribefire-services", "cortex", "cortex");

	public RemoteAuthenticationSpace(Credentials credentials, String url) {
		this.credentials = credentials;
		this.url = url;
	}

	public static RemoteAuthenticationSpace with(String url, String username, String password) {
		UserNameIdentification cortexUserIdentification = UserNameIdentification.T.create();
		cortexUserIdentification.setUserName(username);

		UserPasswordCredentials cortexCredentials = UserPasswordCredentials.T.create();
		cortexCredentials.setPassword(password);
		cortexCredentials.setUserIdentification(cortexUserIdentification);

		return new RemoteAuthenticationSpace(cortexCredentials, url);
	}

	@Override
	public Credentials credentials() {
		return credentials;
	}

	@Override
	public String tfServicesUrl() {
		return url;
	}

}
