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

import javax.websocket.Endpoint;

import com.braintribe.cfg.Required;
import com.braintribe.web.api.registry.WebsocketEndpointRegistration;

public class ConfigurableWebsocketEndpointRegistration extends ConfigurableRegistration implements WebsocketEndpointRegistration {

	protected String path;
	protected Endpoint endpoint;
	
	@Required
	public void setPath(String path) {
		this.path = path;
	}
	
	@Required
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	
	/* builder methods */

	public ConfigurableWebsocketEndpointRegistration path(String path) {
		setPath(path);
		return this;
	}

	public ConfigurableWebsocketEndpointRegistration instance(Endpoint instance) {
		setEndpoint(instance);
		return this;
	}
}
