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
package tribefire.platform.wire.space.module;

import com.braintribe.web.api.registry.FilterConfiguration;

import tribefire.module.api.WebRegistryConfiguration;
import tribefire.platform.impl.module.DelegatingConfigurableWebRegistry;
import tribefire.platform.wire.space.system.servlets.WebRegistrySpace;

public class WebRegistryConfigurationImpl extends DelegatingConfigurableWebRegistry implements WebRegistryConfiguration {

	private final WebRegistrySpace webRegistry;

	public WebRegistryConfigurationImpl(WebRegistrySpace webRegistry) {
		super(webRegistry.moduleWebRegistry());
		this.webRegistry = webRegistry;
	}
	
	@Override
	public FilterConfiguration lenientAuthFilter() {
		return webRegistry.lenientAuthFilterConfiguration();
	}
	
	@Override
	public FilterConfiguration loginRedirectingAuthFilter() {
		return webRegistry.strictAuthFilterConfiguration();
	}
	
	@Override
	public FilterConfiguration compressionFilter() {
		return webRegistry.compressionFilter();
	}
	
	@Override
	public FilterConfiguration threadRenamingFilter() {
		return webRegistry.threadRenamingFilter();
	}

	@Override
	public FilterConfiguration strictAuthFilter() {
		return webRegistry.strictAuthFilterConfiguration();
	}

}
