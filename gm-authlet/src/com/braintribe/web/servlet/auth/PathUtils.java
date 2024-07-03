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
package com.braintribe.web.servlet.auth;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.logging.Logger;

public class PathUtils {

	public static String buildCurrentRelativePath(HttpServletRequest request, Logger logger) {
		String requestURI = request.getRequestURI();
		String pathInfo = request.getPathInfo();
		logger.trace(() -> "Build relative base path based on request URI: "+request.getRequestURI()+" and pathInfo: "+pathInfo);

		
		String basePath = requestURI;
		if (pathInfo != null) {
			basePath = requestURI.substring(0, requestURI.length()-pathInfo.length());
		}
		String result = basePath;
		logger.trace(() -> "Calculated relative base path to: "+result);
		return result;
	}

	public static String buildCurrentServicesPath(HttpServletRequest request, Logger logger) {
		return buildCurrentServicesPath(request, request.getServletPath(), logger);
	}
	
	public static String buildCurrentServicesPath(HttpServletRequest request, String pathSuffix, Logger logger) {
		String basePath = buildCurrentRelativePath(request, logger);
		String servicesPath = basePath.substring(0, basePath.length()-pathSuffix.length());
		logger.trace(() -> "Calculated servicePath to: "+servicesPath);
		return servicesPath;
	}

}
