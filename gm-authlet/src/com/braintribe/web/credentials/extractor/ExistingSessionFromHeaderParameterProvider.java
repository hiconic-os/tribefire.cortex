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
import com.braintribe.web.servlet.auth.Constants;

public class ExistingSessionFromHeaderParameterProvider implements ExistingSessionWebCredentialProvider {

	private static final Maybe<String> NOT_FOUND = Reasons.build(NotFound.T)
			.text("HTTP header parameter " + Constants.HEADER_PARAM_SESSIONID + " not present").toMaybe();

	@Override
	public Maybe<String> findSessionId(HttpServletRequest request) {
		String sessionId = request.getHeader(Constants.HEADER_PARAM_SESSIONID);

		if (sessionId == null || sessionId.isEmpty())
			return NOT_FOUND;

		return Maybe.complete(sessionId);
	}

}
