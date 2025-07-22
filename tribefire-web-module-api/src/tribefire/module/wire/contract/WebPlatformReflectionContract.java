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
package tribefire.module.wire.contract;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.module.api.EnvironmentDenotations;

/**
 * @author peter.gazdik
 */
public interface WebPlatformReflectionContract extends PlatformReflectionContract {

	EnvironmentDenotations environmentDenotations();
	
	/**
	 * Reads a configuration based on the EntityType. An instance of that type is resolved by deriving a filename from the entityType like this:
	 * 
	 * MyExampleConfiguration -> my-example-configuration.yaml
	 * 
	 * If the file is present in the conf folder it will be parsed with the given type as inferred root type. The parsing supports entity initializers, placeholder and their evaluation. The resulting
	 * entity will be returned. If the file is not present a default instance will be created and returned.
	 */
	<C extends GenericEntity> Maybe<C> readConfig(EntityType<C> configType);

}
