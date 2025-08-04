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

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.security.service.config.OpenUserSessionConfiguration;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.braintribe.transport.http.HttpClientProvider;
import com.braintribe.transport.ssl.SslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.EasySslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.StrictSslSocketFactoryProvider;
import com.braintribe.web.servlet.auth.cookie.DefaultCookieHandler;
import com.braintribe.web.servlet.auth.providers.OpenUserSessionConfigurationProviderImpl;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.HttpContract;
import tribefire.platform.wire.space.module.WebPlatformReflectionSpace;

@Managed
public class HttpSpace implements HttpContract {
	
	@Import
	private WebPlatformReflectionSpace webPlatformReflection;

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
		SslSocketFactoryProvider bean = TribefireRuntime.getAcceptSslCertificates() ? new EasySslSocketFactoryProvider()
				: new StrictSslSocketFactoryProvider();

		return bean;
	}
	
	@Override
	@Managed
	public DefaultCookieHandler cookieHandler() {
		DefaultCookieHandler bean = new DefaultCookieHandler();
		bean.setCookieDomain(TribefireRuntime.getCookieDomain());
		bean.setCookiePath(TribefireRuntime.getCookiePath());
		// DEVCX-208: The Control Center cannot access the cookie anymore if the cookie is not reachable via JavaScript.
		bean.setAddCookie(TribefireRuntime.getCookieEnabled());
		bean.setEntryPointProvider(openUserSessionConfigurationProvider()::findEntryPoint);
		return bean;
	}
	
	@Managed
	@Override
	public OpenUserSessionConfigurationProviderImpl openUserSessionConfigurationProvider() {
		OpenUserSessionConfigurationProviderImpl bean = new OpenUserSessionConfigurationProviderImpl();
		bean.setOpenUserSessionConfiguration(webPlatformReflection.readConfig(OpenUserSessionConfiguration.T).get());
		return bean;
	}
	
	@Override
	public Function<HttpServletRequest, String> sessionCookieNameSupplier() {
		return openUserSessionConfigurationProvider()::getCookieName;
	}
}
