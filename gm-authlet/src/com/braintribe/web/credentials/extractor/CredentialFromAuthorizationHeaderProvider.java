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
package com.braintribe.web.credentials.extractor;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.web.servlet.auth.WebCredentialsProvider;

public interface CredentialFromAuthorizationHeaderProvider extends WebCredentialsProvider {

	Maybe<Credentials> HEADER_NOT_FOUND = Reasons.build(NotFound.T).text("HTTP Authorization header parameter not present").toMaybe();

	@Override
	default Maybe<Credentials> provideCredentials(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || authHeader.isEmpty())
			return HEADER_NOT_FOUND;

		return provideCredentials(authHeader);
	}

	Maybe<Credentials> provideCredentials(String authHeader);

}
