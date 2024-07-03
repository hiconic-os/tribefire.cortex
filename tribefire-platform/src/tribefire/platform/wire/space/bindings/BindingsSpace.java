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
package tribefire.platform.wire.space.bindings;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class BindingsSpace implements WireSpace, EnvironmentDenotationConstants {

	private static final String ENVIRONMENT_BINDING_SECURITY_CONNECTION = "TRIBEFIRE_BINDING_SECURITY_CONNECTION";

	@Managed
	public String environmentSecurityConnection() {
		String value = TribefireRuntime.getProperty(ENVIRONMENT_BINDING_SECURITY_CONNECTION, environmentSecurityConnection);
		return value;
	}

	
}
