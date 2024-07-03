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
package com.braintribe.model.processing.securityservice.basic;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.UnsupportedOperation;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.core.expert.impl.PolymorphicDenotationMap;
import com.braintribe.model.processing.service.api.ReasonedServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentialsResponse;
import com.braintribe.model.securityservice.credentials.Credentials;

public class HardwiredDispatchingAuthenticator
		implements ReasonedServiceProcessor<AuthenticateCredentials, AuthenticateCredentialsResponse> {

	private PolymorphicDenotationMap<Credentials, ReasonedServiceProcessor<AuthenticateCredentials, ? extends AuthenticateCredentialsResponse>> authenticators = new PolymorphicDenotationMap<>();

	public <T extends Credentials> void registerAuthenticator(EntityType<T> type,
			ReasonedServiceProcessor<AuthenticateCredentials, ? extends AuthenticateCredentialsResponse> authenticator) {
		authenticators.put(type, authenticator);
	}

	@Override
	public Maybe<? extends AuthenticateCredentialsResponse> processReasoned(ServiceRequestContext context, AuthenticateCredentials request) {
		Credentials credentials = request.getCredentials();

		if (credentials == null)
			return Reasons.build(InvalidArgument.T).text("AuthenticateCredentials.credentials must not be null").toMaybe();

		ReasonedServiceProcessor<AuthenticateCredentials, ? extends AuthenticateCredentialsResponse> authenticator = authenticators.find(credentials);

		if (authenticator == null)
			return Reasons.build(InvalidCredentials.T).text("Invalid Credentials") //
					.cause(Reasons.build(UnsupportedOperation.T).text("Credential type " + credentials.type().getTypeSignature() + " not supported.")
							.toReason())
					.toMaybe();

		return authenticator.processReasoned(context, request);
	}
}
