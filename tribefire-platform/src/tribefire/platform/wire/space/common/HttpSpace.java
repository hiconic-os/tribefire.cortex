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
package tribefire.platform.wire.space.common;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.braintribe.transport.http.HttpClientProvider;
import com.braintribe.transport.ssl.SslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.EasySslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.StrictSslSocketFactoryProvider;
import com.braintribe.web.servlet.auth.cookie.DefaultCookieHandler;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.HttpContract;

@Managed
public class HttpSpace implements HttpContract {

	@Managed
	public HttpClientProvider clientProvider() {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(sslSocketFactoryProvider());
		return bean;
	}

	@Managed
	public HttpClientProvider nonPoolingClientProvider() {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(sslSocketFactoryProvider());
		bean.setPoolTimeToLive(1L);
		return bean;
	}

	@Managed
	public SslSocketFactoryProvider sslSocketFactoryProvider() {
		SslSocketFactoryProvider bean = TribefireRuntime.getAcceptSslCertificates()?
				new EasySslSocketFactoryProvider():
				new StrictSslSocketFactoryProvider();
				
		return bean;
	}

	@Override
	@Managed
	public DefaultCookieHandler cookieHandler() {
		DefaultCookieHandler bean = new DefaultCookieHandler();
		bean.setCookieDomain(TribefireRuntime.getCookieDomain());
		bean.setCookiePath(TribefireRuntime.getCookiePath());
		bean.setCookieHttpOnly(TribefireRuntime.getCookieHttpOnly()); // DEVCX-208: The Control Center cannot access the cookie anymore if this is true
		bean.setAddCookie(TribefireRuntime.getCookieEnabled());
		return bean;
	}
}
