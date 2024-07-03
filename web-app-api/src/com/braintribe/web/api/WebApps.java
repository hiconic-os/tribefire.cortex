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
package com.braintribe.web.api;

import java.io.File;
import java.util.UUID;

import javax.servlet.ServletContext;

public class WebApps {

	private static ServletContext servletContext;
	private static String nodeId;

	public static ServletContext servletContext() {
		return servletContext;
	}

	public static String nodeId() {
		if (nodeId == null) {
			nodeId = UUID.randomUUID().toString();
		}
		return nodeId;
	}

	public static File realPath() {
		String realPath = servletContext().getRealPath("/");
		if (realPath != null) {
			return new File(realPath);
		} else {
			throw new WebAppException("The real path could not be resolved");
		}
	}

	public static String replaceNodeId(String nodeId) {
		String previousNodeId = WebApps.nodeId;
		WebApps.nodeId = nodeId;
		return previousNodeId;
	}

	protected static void publishServletContext(ServletContext context) {
		servletContext = context;
	}

}
