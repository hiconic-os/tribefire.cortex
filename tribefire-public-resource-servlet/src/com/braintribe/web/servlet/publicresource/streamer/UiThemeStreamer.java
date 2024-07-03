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
package com.braintribe.web.servlet.publicresource.streamer;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.securityservice.api.UserSessionScope;
import com.braintribe.model.processing.securityservice.api.UserSessionScoping;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.web.servlet.publicresource.PublicResourceStreamer;

public class UiThemeStreamer implements PublicResourceStreamer {
	
	private UserSessionScoping userSessionScoping;
	private WorkbenchConfigurationProvider configurationProvider;
	
	@Required
	@Configurable
	public void setConfigurationProvider(WorkbenchConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	@Required
	@Configurable
	public void setUserSessionScoping(UserSessionScoping userSessionScoping) {
		this.userSessionScoping = userSessionScoping;
	}
	
	@Override
	public boolean streamResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String accessId = request.getParameter("accessId");
		if (accessId != null) {
			Resource stylesheet = this.configurationProvider.getCss(accessId);
			if (stylesheet != null) {

				String mimeType = stylesheet.getMimeType();
				if (StringTools.isEmpty(mimeType)) {
					mimeType = "text/css";
				}
				response.setContentType(mimeType);

				UserSessionScope scope = userSessionScoping.forDefaultUser().push();

				InputStream resourceStream = stylesheet.openStream();
				try {
					IOTools.pump(resourceStream, response.getOutputStream());
					return true;
				} finally {
					resourceStream.close();
					scope.pop();
				}
			}
		}		
		
		return false;
	}
}
