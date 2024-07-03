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
package com.braintribe.model.processing.aspect.crypto.interceptor.manipulation;

import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.processing.aop.api.context.AroundContext;
import com.braintribe.model.processing.aspect.crypto.interceptor.CryptoInterceptor;
import com.braintribe.model.processing.aspect.crypto.interceptor.CryptoInterceptorConfiguration;

/**
 * <p>
 * A {@link CryptoInterceptor} which processes {@link CryptoManipulationInterceptorProcessor} instances.
 * 
 */
public class CryptoManipulationInterceptor extends CryptoInterceptor<ManipulationRequest, ManipulationResponse, CryptoManipulationInterceptorProcessor> {

	public CryptoManipulationInterceptor(CryptoInterceptorConfiguration cryptoInterceptorConfiguration) {
		super(cryptoInterceptorConfiguration);
	}

	@Override
	protected CryptoManipulationInterceptorProcessor createCryptoInterceptorContext(AroundContext<ManipulationRequest, ManipulationResponse> context) {
		return new CryptoManipulationInterceptorProcessor(cryptoInterceptorConfiguration, context);
	}

}
