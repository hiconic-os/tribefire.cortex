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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.security.reason.AuthenticationFailure;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.service.api.ReasonedServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.security.deployment.meta.AuthenticateWith;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentialsResponse;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.utils.lcd.LazyInitialized;

public class MetaDataDispatchingAuthenticator implements ReasonedServiceProcessor<AuthenticateCredentials, AuthenticateCredentialsResponse> {
	private static final Logger log = Logger.getLogger(MetaDataDispatchingAuthenticator.class);

	private LazyInitialized<ModelAccessory> cortexModelAccessorySupplier;
	private ReasonedServiceProcessor<AuthenticateCredentials, AuthenticateCredentialsResponse> defaultDelegate;

	@Required
	public void setCortexModelAccessorySupplier(Supplier<ModelAccessory> cortexModelAccessorySupplier) {
		this.cortexModelAccessorySupplier = new LazyInitialized<>(cortexModelAccessorySupplier);
	}

	@Configurable
	public void setDefaultDelegate(ReasonedServiceProcessor<AuthenticateCredentials, AuthenticateCredentialsResponse> defaultDelegate) {
		this.defaultDelegate = defaultDelegate;
	}

	@Override
	public Maybe<? extends AuthenticateCredentialsResponse> processReasoned(ServiceRequestContext context, AuthenticateCredentials request) {
		Credentials credentials = request.getCredentials();

		if (credentials == null)
			return Reasons.build(InvalidArgument.T).text("AuthenticateCredentials.credentials must not be null").toMaybe();

		List<AuthenticateWith> authenticateWiths = cortexModelAccessorySupplier.get().getCmdResolver().getMetaData().entity(credentials)
				.meta(AuthenticateWith.T).list();

		AuthenticateCredentials authenticateCredentials = AuthenticateCredentials.T.create();
		authenticateCredentials.setProperties(request.getProperties());
		authenticateCredentials.setCredentials(request.getCredentials());

		List<AuthenticationFailure> authProblems = new ArrayList<>(authenticateWiths.size());
		LazyHolder<String> logCorrelationId = LazyHolder.from(() -> UUID.randomUUID().toString());

		if (authenticateWiths.isEmpty() && defaultDelegate != null)
			return defaultDelegate.processReasoned(context, request);

		// TODO: should we somehow collect meaning full masked reasons
		for (AuthenticateWith authenticateWith : authenticateWiths) {
			String processorExternalId = authenticateWith.getProcessor().getExternalId();
			authenticateCredentials.setServiceId(processorExternalId);
			Maybe<? extends AuthenticateCredentialsResponse> maybeResponse = authenticateCredentials.eval(context).getReasoned();

			if (maybeResponse.isSatisfied()) {
				return maybeResponse;
			}

			Reason reason = maybeResponse.whyUnsatisfied();

			if (reason instanceof AuthenticationFailure && reason.type() != AuthenticationFailure.T)
				authProblems.add((AuthenticationFailure) reason);

			log.debug(() -> "Credentials " + credentials + " not authenticated with processor with externalId " + processorExternalId + ". (context="
					+ logCorrelationId.get() + "). Reason: " + reason.stringify());
		}

		switch (authProblems.size()) {
			case 0:
				return Reasons.build(AuthenticationFailure.T)
						.text("Authentication Failure with log correlation (context=" + logCorrelationId.get() + ").").toMaybe();
			case 1:
				return authProblems.get(0).asMaybe();
			default:
				return Reasons.build(AuthenticationFailure.T)
						.text("Authentication Failure with log correlation (context=" + logCorrelationId.get() + ").")
						.enrich(r -> r.getReasons().addAll(authProblems)).toMaybe();
		}
	}
}
