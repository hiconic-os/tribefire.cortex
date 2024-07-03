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
package tribefire.platform.impl.module;

import java.util.EventListener;
import java.util.List;

import com.braintribe.web.api.registry.ConfigurableWebRegistry;
import com.braintribe.web.api.registry.FilterRegistration;
import com.braintribe.web.api.registry.ServletRegistration;
import com.braintribe.web.api.registry.WebsocketEndpointRegistration;
import com.braintribe.web.impl.registry.ConfigurableFilterRegistration;
import com.braintribe.web.impl.registry.ConfigurableServletRegistration;
import com.braintribe.web.impl.registry.ConfigurableWebsocketEndpointRegistration;

public class DelegatingConfigurableWebRegistry implements ConfigurableWebRegistry {
	private final ConfigurableWebRegistry delegate;
	
	public DelegatingConfigurableWebRegistry(ConfigurableWebRegistry delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public void setListeners(List<EventListener> listeners) {
		delegate.setListeners(listeners);
	}

	@Override
	public void setServlets(List<ServletRegistration> servlets) {
		delegate.setServlets(servlets);
	}

	@Override
	public void setFilters(List<FilterRegistration> filters) {
		delegate.setFilters(filters);
	}

	@Override
	public void setWebsocketEndpoints(List<WebsocketEndpointRegistration> websocketEndpoints) {
		delegate.setWebsocketEndpoints(websocketEndpoints);
	}

	@Override
	public boolean addListener(EventListener eventListener) {
		return delegate.addListener(eventListener);
	}

	@Override
	public boolean addServlet(ServletRegistration registration) {
		return delegate.addServlet(registration);
	}

	@Override
	public boolean addFilter(FilterRegistration registration) {
		return delegate.addFilter(registration);
	}

	@Override
	public boolean addWebsocketEndpoint(WebsocketEndpointRegistration registration) {
		return delegate.addWebsocketEndpoint(registration);
	}

	@Override
	public ConfigurableWebRegistry servlets(ConfigurableServletRegistration... registrations) {
		return delegate.servlets(registrations);
	}

	@Override
	public ConfigurableWebRegistry filters(ConfigurableFilterRegistration... registrations) {
		return delegate.filters(registrations);
	}

	@Override
	public ConfigurableWebRegistry websocketEndpoints(ConfigurableWebsocketEndpointRegistration... registrations) {
		return delegate.websocketEndpoints(registrations);
	}
}
