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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.web.servlet.auth.AuthFilter;
import com.braintribe.web.servlet.auth.WebCredentialsProvider;
import com.braintribe.web.servlet.home.HomeServlet;
import com.braintribe.web.servlet.home.model.LinkCollection;

import tribefire.cortex.module.loading.PlatformHardwiredExpertsRegistry;
import tribefire.module.wire.contract.WebPlatformHardwiredExpertsContract;

/**
 * @author peter.gazdik
 */
public class WebPlatformHardwiredExpertsRegistry extends PlatformHardwiredExpertsRegistry implements WebPlatformHardwiredExpertsContract {

	private HomeServlet homeServlet;
	private final List<AuthFilter> authFilters = new ArrayList<>();

	@Required
	public void setHomeServlet(HomeServlet homeServlet) {
		this.homeServlet = homeServlet;
	}

	@Configurable
	public void addAuthFilter(AuthFilter authFilter) {
		authFilters.add(authFilter);
	}

	@Override
	public <T extends GenericEntity> void bindLandingPageLinkConfigurer(String groupPattern, EntityType<T> type,
			BiConsumer<T, LinkCollection> configurer) {
		homeServlet.addLinkConfigurer(groupPattern, type, configurer);
	}

	@Override
	public void registerWebCredentialsProvider(String key, WebCredentialsProvider webCredentialsProvider) {
		for (AuthFilter authFilter : authFilters) {
			authFilter.addWebCredentialProvider(key, webCredentialsProvider);
		}
	}

}
