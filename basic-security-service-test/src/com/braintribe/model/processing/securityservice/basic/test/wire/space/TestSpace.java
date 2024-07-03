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
import com.braintribe.gm.service.wire.common.contract.ServiceProcessingConfigurationContract;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.securityservice.basic.SecurityServiceProcessor;
import com.braintribe.model.processing.securityservice.basic.test.common.AccessDataInitializer;
import com.braintribe.model.processing.securityservice.basic.test.common.TestConfig;
import com.braintribe.model.processing.securityservice.basic.test.wire.contract.TestContract;
import com.braintribe.model.processing.securityservice.basic.test.wire.space.access.AuthAccessSpace;
import com.braintribe.model.processing.securityservice.basic.test.wire.space.access.ClientsAccessSpace;
import com.braintribe.model.processing.securityservice.basic.test.wire.space.access.UserSessionsAccessSpace;
import com.braintribe.model.processing.service.api.ServiceAroundProcessor;
import com.braintribe.model.processing.service.api.aspect.IsTrustedAspect;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.SecurityRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;

@Managed
public class TestSpace implements TestContract {

	@Import
	private MetaSpace meta;

	@Import
	private AuthAccessSpace authAccess;

	@Import
	private UserSessionsAccessSpace userSessionsAccess;

	@Import
	private ClientsAccessSpace clientsAccess;

	@Import
	private SecurityServiceSpace securityService;

	@Import
	private AuthExpertsSpace authExperts;

	@Import
	private ServiceProcessingConfigurationContract serviceProcessingConfiguration;

	@Import
	private CommonServiceProcessingContract commonServiceProcessing;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		serviceProcessingConfiguration.registerServiceConfigurer(this::configureServices);
	}

	private void configureServices(ConfigurableDispatchingServiceProcessor bean) {
		bean.register(SecurityRequest.T, securityService.service());
		bean.register(AuthenticateCredentials.T, securityService.authenticator());
		bean.registerInterceptor("trust-state").register(trustStateInterceptor());
	}

	@Managed
	@Override
	public TestConfig testConfig() {
		TestConfig bean = new TestConfig();
		bean.setGeneratingUserSessionAccesses(false);
		bean.setEnableLongRunning(false);
		bean.setEnableExpiration(false);

		return bean;
	}

	@Managed
	@Override
	public AccessDataInitializer dataInitializer() {
		AccessDataInitializer bean = new AccessDataInitializer();
		bean.setAuthGmSession(authAccess.lowLevelSession());
		bean.setClientsGmSession(clientsAccess.lowLevelSession());
		return bean;
	}

	@Override
	public PersistenceGmSession authGmSession() {
		return authAccess.lowLevelSession();
	}

	@Override
	public PersistenceGmSession userSessionsGmSession() {
		return userSessionsAccess.lowLevelSession();
	}

	@Override
	public SecurityServiceProcessor securityServiceProcessor() {
		return securityService.service();
	}

	@Override
	public Evaluator<ServiceRequest> requestEvaluator() {
		return commonServiceProcessing.evaluator();
	}

	@Override
	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}

	private boolean trusted = false;

	@Managed
	private ServiceAroundProcessor<ServiceRequest, Object> trustStateInterceptor() {
		return (c, r, p) -> p.proceed(c.derive().set(IsTrustedAspect.class, trusted).build(), r);
	}

}
