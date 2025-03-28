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
package com.braintribe.web.api.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;

public class SetCharacterEncodingFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SetCharacterEncodingFilter.class);

	protected String encoding = null;
	protected boolean ignore = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Enumeration<String> paramNames = filterConfig.getInitParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = filterConfig.getInitParameter(paramName);
			if (paramName.equalsIgnoreCase("encoding")) {
				this.setEncoding(paramValue);
			} else if (paramName.equalsIgnoreCase("ignore")) {
				this.setIgnore(Boolean.parseBoolean(paramValue));
			} else {
				logger.warn("Unknown parameter name: "+paramName+" with value "+paramValue);
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if ((ignore) || (request.getCharacterEncoding() == null)) {
			if (this.encoding != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Setting character encoding "+this.encoding+" in request.");
				}
				request.setCharacterEncoding(this.encoding);
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		//Nothing to do
	}

	@Configurable
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	@Configurable
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

}
