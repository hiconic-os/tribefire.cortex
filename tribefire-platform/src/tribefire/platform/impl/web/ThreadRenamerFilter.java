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
package tribefire.platform.impl.web;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;
import com.braintribe.logging.ThreadRenamer;

public class ThreadRenamerFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ThreadRenamerFilter.class);

	private ThreadRenamer threadRenamer = ThreadRenamer.NO_OP;
	private boolean addFullUrl;

	@Configurable
	public void setThreadRenamer(ThreadRenamer threadRenamer) {
		Objects.requireNonNull(threadRenamer, "threadRenamer cannot be set to null");
		this.threadRenamer = threadRenamer;
	}

	@Configurable
	public void setAddFullUrl(boolean addFullUrl) {
		this.addFullUrl = addFullUrl;
	}

	@Override
	public void destroy() {
		/* Intentionally left empty */
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		threadRenamer.push(() -> name(request));
		try {
			filterChain.doFilter(request, response);
		} finally {
			threadRenamer.pop();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		/* Intentionally left empty */
	}

	private String name(ServletRequest request) {
		return "from(" + url(request) + ")";
	}

	private String url(ServletRequest request) {
		try {
			if (addFullUrl) {
				return ((HttpServletRequest) request).getRequestURL().toString();
			}
			return ((HttpServletRequest) request).getRequestURI();
		} catch (Throwable t) {
			logger.error("Failed to obtain an URL from " + request, t);
			return "<unknown>";
		}
	}

}
