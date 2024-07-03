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
package tribefire.module.api;

import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.processing.meta.configured.ConfigurationModelBuilder;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.module.wire.contract.ModelApiContract;

/**
 * Straight forward, a factory which can be used to create new configuration models given their name and version information and their dependencies.
 * <p>
 * Configuration model is a model that does not introduce any new types but only aggregates other models (as dependencies) and potentially contains
 * {@link MetaData}.
 * 
 * @see ModelApiContract#newConfigurationModelFactory(ManagedGmSession)
 * 
 * @author peter.gazdik
 */
public interface ConfigurationModelFactory {

	/** Returns a {@link ConfigurationModelBuilder} to build a new model with a standard name, i.e. "$groupId:$artifactId". */
	ConfigurationModelBuilder create(String groupId, String artifactId, String version);

	/** Returns a {@link ConfigurationModelBuilder} to build a new model with given name. */
	ConfigurationModelBuilder create(String name, String version);

}
