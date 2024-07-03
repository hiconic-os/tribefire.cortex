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

import com.braintribe.model.processing.aop.api.interceptor.InterceptionException;

/**
 * <p>
 * An {@link InterceptionException} meant to distinguish exceptions 
 * thrown by {@link CryptoInterceptor}(s) from those thrown by other 
 * {@link com.braintribe.model.processing.aop.api.interceptor.AroundInterceptor} 
 * in the chain.
 * 
 * <p>
 * Therefore {@link InterceptionException} coming from the invocation chain are
 * not to be wrapped in this exception.
 * 
 *
 */
public class CryptoInterceptionException extends InterceptionException {

	private static final long serialVersionUID = 1L;

	public CryptoInterceptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptoInterceptionException(String message) {
		super(message);
	}

	public CryptoInterceptionException(Throwable cause) {
		super(cause);
	}

}
