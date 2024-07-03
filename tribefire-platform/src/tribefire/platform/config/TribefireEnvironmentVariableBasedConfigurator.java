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
package tribefire.platform.config;

import com.braintribe.config.configurator.Configurator;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.utils.StringTools;

import tribefire.platform.config.url.AbstractEnvironmentVariableBasedConfigurator;

/**
 * A tribefire {@link Configurator} for environment variables based configured denotations.
 */
public class TribefireEnvironmentVariableBasedConfigurator extends AbstractEnvironmentVariableBasedConfigurator {

	@Override
	protected String getEnvironmentVariableName() {
		String varName = TribefireRuntime.getProperty(TribefireRuntime.ENVIRONMENT_CONFIGURATION_INJECTION_ENVVARIABLE);
		if (StringTools.isEmpty(varName)) {
			varName = "TRIBEFIRE_CONFIGURATION_INJECTION_JSON";
		}
		return varName;
	}
	
}
