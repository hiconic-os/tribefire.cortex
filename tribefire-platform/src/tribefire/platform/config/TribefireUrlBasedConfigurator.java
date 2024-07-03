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

import tribefire.platform.config.url.AbstractUrlBasedConfigurator;

/**
 * A tribefire {@link Configurator} for URL based configured denotations.
 * 
 * @author gunther.schenk
 */
public class TribefireUrlBasedConfigurator extends AbstractUrlBasedConfigurator {
	
	@Override
	protected String buildDefaultFileName() {
		return "configuration.json";
	}

	@Override
	protected String buildUrlProperty() {
		return TribefireRuntime.ENVIRONMENT_CONFIGURATION_INJECTION_URL;
	}
	
}
