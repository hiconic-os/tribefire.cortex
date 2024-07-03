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
package com.braintribe.model.processing.aspect.crypto.interceptor;

import com.braintribe.model.processing.aop.api.context.AroundContext;
import com.braintribe.model.processing.aop.api.interceptor.AroundInterceptor;
import com.braintribe.model.processing.aop.api.interceptor.InterceptionException;

/**
 * <p>
 * A {@link AroundInterceptor} which wraps the given {@link AroundContext}(s) as specialized
 * {@link CryptoInterceptorProcessor}(s).
 * 
 *
 * @param <I>
 *            {@link AroundInterceptor} standard input.
 * @param <O>
 *            {@link AroundInterceptor} standard output.
 * @param <C>
 *            The specialized {@link CryptoInterceptorProcessor} implementation will process.
 */
public abstract class CryptoInterceptor<I, O, C extends CryptoInterceptorProcessor<O>> implements AroundInterceptor<I, O> {

	protected CryptoInterceptorConfiguration cryptoInterceptorConfiguration;

	public CryptoInterceptor(CryptoInterceptorConfiguration cryptoInterceptorConfiguration) {
		this.cryptoInterceptorConfiguration = cryptoInterceptorConfiguration;
	}

	protected abstract C createCryptoInterceptorContext(AroundContext<I, O> context) throws InterceptionException;

	@Override
	public O run(AroundContext<I, O> context) throws InterceptionException {

		validateContext(context);

		C cryptoInterceptorContext = createCryptoInterceptorContext(context);

		return cryptoInterceptorContext.proceed();

	}

	protected void validateContext(AroundContext<I, O> context) throws InterceptionException {

		if (context == null) {
			throw new InterceptionException("Invalid null context");
		}

		// A hook for future validations

	}

}
