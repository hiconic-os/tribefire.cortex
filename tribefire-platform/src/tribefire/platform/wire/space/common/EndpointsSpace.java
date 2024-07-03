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
package tribefire.platform.wire.space.common;

import java.net.URI;
import java.net.URL;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class EndpointsSpace implements WireSpace {

	@Managed
	public URL servicesUrl() {
		try {
			String servicesUrl = TribefireRuntime.getServicesUrl();
			URL bean = new URI(servicesUrl).toURL();
			return bean;

		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Failed to obtain the services URL");
		}
	}

	@Managed
	public URL rpcUrl() {
		URL bean = resolveServicesUrl("rpc");
		return bean;
	}

	@Managed
	public URL streamingUrl() {
		URL bean = resolveServicesUrl("streaming");
		return bean;
	}

	private URL resolveServicesUrl(String path) {
		URL servicesUrl = servicesUrl();
		return resolveUrl(servicesUrl.toString(), path);
	}

	private URL resolveUrl(String base, String path) {
		try {
			return new URI(base + "/" + path).normalize().toURL();
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Failed to resolve " + path + " against " + base);
		}
	}

}
