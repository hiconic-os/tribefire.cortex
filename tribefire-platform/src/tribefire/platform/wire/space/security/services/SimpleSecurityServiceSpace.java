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
package tribefire.platform.wire.space.security.services;

import com.braintribe.model.processing.securityservice.basic.SimpleSecurityServiceProcessor;
import com.braintribe.model.processing.securityservice.basic.WebAuthorizationServiceProcessor;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.common.HttpSpace;

@Managed
public class SimpleSecurityServiceSpace implements WireSpace {
	@Import
	private HttpSpace http;

	@Managed
	public SimpleSecurityServiceProcessor service() {
		SimpleSecurityServiceProcessor bean = new SimpleSecurityServiceProcessor();
		return bean;
	}
	
	@Managed
	public WebAuthorizationServiceProcessor webAuthorizationServiceProcessor() {
		WebAuthorizationServiceProcessor bean = new WebAuthorizationServiceProcessor();
		bean.setCookieHandler(http.cookieHandler());
		return bean;
	}

}
