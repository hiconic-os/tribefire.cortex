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
package com.braintribe.web.impl.registry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import com.braintribe.cfg.Required;
import com.braintribe.web.api.registry.MultipartConfig;
import com.braintribe.web.api.registry.ServletRegistration;

public class ConfigurableServletRegistration extends ConfigurableDynamicRegistration implements ServletRegistration {

	protected String name;
	protected Servlet servlet;
	private Class<? extends Servlet> servletClass;
	protected List<String> mappings = Collections.emptyList();
	protected int loadOnStartup;
	protected MultipartConfig configurableMultipartConfig;
	protected String runAsRole;

	@Override
	public String getName() {
		return name;
	}

	@Required
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Servlet getServlet() {
		return servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	public void setServletClass(Class<? extends Servlet> servletClass) {
		this.servletClass = servletClass;
	}

	@Override
	public Class<? extends Servlet> getServletClass() {
		return servletClass;
	}

	@Override
	public List<String> getMappings() {
		return mappings;
	}

	public void setMappings(List<String> mappings) {
		this.mappings = mappings;
	}

	@Override
	public String[] getMappingsArray() {
		return this.mappings.toArray(new String[this.mappings.size()]);
	}

	@Override
	public int getLoadOnStartup() {
		return loadOnStartup;
	}

	public void setLoadOnStartup(int loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
	}

	@Override
	public MultipartConfig getMultipartConfig() {
		return configurableMultipartConfig;
	}

	public void setMultipartConfig(MultipartConfig configurableMultipartConfig) {
		this.configurableMultipartConfig = configurableMultipartConfig;
	}

	@Override
	public String getRunAsRole() {
		return runAsRole;
	}

	public void setRunAsRole(String runAsRole) {
		this.runAsRole = runAsRole;
	}

	/* builder methods */

	public ConfigurableServletRegistration name(String filterName) {
		setName(filterName);
		return this;
	}

	public ConfigurableServletRegistration instance(Servlet instance) {
		setServlet(instance);
		return this;
	}

	public ConfigurableServletRegistration type(Class<? extends Servlet> type) {
		setServletClass(type);
		return this;
	}

	public ConfigurableServletRegistration pattern(String urlPattern) {
		setMappings(Arrays.asList(urlPattern));
		return this;
	}

	public ConfigurableServletRegistration patterns(String... urlPatterns) {
		setMappings(Arrays.asList(urlPatterns));
		return this;
	}

	public ConfigurableServletRegistration initParams(Map<String, String> initParams) {
		setInitParameters(initParams);
		return this;
	}

	public ConfigurableServletRegistration multipart() {
		setMultipartConfig(new ConfigurableMultipartConfig());
		return this;
	}

	public ConfigurableServletRegistration multipart(MultipartConfig multipartConfig) {
		setMultipartConfig(multipartConfig);
		return this;
	}

	public ConfigurableServletRegistration order(int order) {
		setOrder(order);
		return this;
	}

	/* // builder methods */

}
