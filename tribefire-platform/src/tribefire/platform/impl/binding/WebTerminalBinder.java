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
package tribefire.platform.impl.binding;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.extensiondeployment.WebTerminal;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.DeploymentException;
import com.braintribe.model.processing.deployment.api.DirectComponentBinder;
import com.braintribe.model.processing.deployment.api.MutableDeploymentContext;
import com.braintribe.model.processing.deployment.api.UndeploymentContext;
import com.braintribe.servlet.FilterServlet;

import tribefire.platform.impl.web.ComponentDispatcherServlet;

public class WebTerminalBinder implements DirectComponentBinder<WebTerminal, HttpServlet> {

	private ComponentDispatcherServlet dispatcherServlet;
	private Filter authFilter;

	@Required
	public void setDispatcherServlet(ComponentDispatcherServlet dispatcherServlet) {
		this.dispatcherServlet = dispatcherServlet;
	}

	@Configurable
	public void setAuthFilter(Filter authFilter) {
		this.authFilter = authFilter;
	}

	@Override
	public EntityType<WebTerminal> componentType() {
		return WebTerminal.T;
	}

	@Override
	public HttpServlet bind(MutableDeploymentContext<WebTerminal, HttpServlet> context) throws DeploymentException {
		HttpServlet httpServlet = context.getInstanceToBeBound();
		WebTerminal webTerminal = context.getDeployable();

		if (authFilter != null)
			httpServlet = applyFilter(httpServlet, authFilter);

		dispatcherServlet.registerAndInitServlet(webTerminal, httpServlet);

		return httpServlet;
	}

	private static HttpServlet applyFilter(HttpServlet httpServlet, Filter filter) {
		FilterServlet filterServlet = new FilterServlet();
		filterServlet.setDelegate(httpServlet);
		filterServlet.setFilter(filter);

		return filterServlet;
	}

	@Override
	public void unbind(UndeploymentContext<WebTerminal, HttpServlet> context) {
		dispatcherServlet.unregisterAndDestroy(context.getDeployable());
	}

	@Override
	public Class<?>[] componentInterfaces() {
		return new Class<?>[] { javax.servlet.Servlet.class };
	}

}
