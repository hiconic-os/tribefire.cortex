// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.web.servlet.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

import com.braintribe.cfg.InitializationAware;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.web.servlet.BasicTemplateBasedServlet;

public class LoginServlet extends BasicTemplateBasedServlet implements InitializationAware {

	private static final long serialVersionUID = -3371378397236984055L;
	private static final String signinPageTemplateLocation = "com/braintribe/web/servlet/auth/templates/login.html.vm";
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(LoginServlet.class);
		
	@Override
	public void postConstruct() {
		setTemplateLocation(signinPageTemplateLocation);
	}

	@Override
	protected VelocityContext createContext(HttpServletRequest request,HttpServletResponse repsonse) {
		VelocityContext context = new VelocityContext();
		context.put("continue", request.getParameter(Constants.REQUEST_PARAM_CONTINUE));
		context.put("message", request.getParameter(Constants.REQUEST_PARAM_MESSAGE));
		context.put("messageStatus", request.getParameter(Constants.REQUEST_PARAM_MESSAGESTATUS));
		context.put("tribefireRuntime", TribefireRuntime.class);
		
		if (AuthServlet.offerStaySigned()) {
			context.put("offerStaySigned", Boolean.TRUE);
		} else {
			context.put("offerStaySigned", Boolean.FALSE);
		}
		
		return context;
	}

}
