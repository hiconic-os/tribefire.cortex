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

import com.braintribe.crypto.Cryptor;
import com.braintribe.model.meta.data.crypto.PropertyCrypting;
import com.braintribe.model.processing.crypto.provider.CryptorProvider;

/**
 * <p>
 * A set of configurations for {@link CryptoInterceptor}(s).
 * 
 */
public class CryptoInterceptorConfiguration {

	private CryptorProvider<Cryptor, PropertyCrypting> cryptorProvider;
	private boolean cacheCryptorsPerContext = true;

	public CryptorProvider<Cryptor, PropertyCrypting> getCryptorProvider() {
		return cryptorProvider;
	}

	public void setCryptorProvider(CryptorProvider<Cryptor, PropertyCrypting> cryptorProvider) {
		this.cryptorProvider = cryptorProvider;
	}

	public boolean isCacheCryptorsPerContext() {
		return cacheCryptorsPerContext;
	}

	public void setCacheCryptorsPerContext(boolean cacheCryptorsPerContext) {
		this.cacheCryptorsPerContext = cacheCryptorsPerContext;
	}

}
