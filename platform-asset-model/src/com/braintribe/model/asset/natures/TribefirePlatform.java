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
package com.braintribe.model.asset.natures;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Tribefire platform is a virtual layer on top of the underlying runtime environment, which provides and normalized
 * interface for binding data and functionality from tribefire modules, and maps the communication over
 * environment-specific interfaces to standard tribefire APIs used by the modules.
 * <p>
 * For example, a runtime environment can be a web server (or a command line interface or anything else), the
 * environment-specific interface could be REST, and the platform is responsible for mapping the REST calls to the
 * DDSA/ServiceProcessors, IncrementalAccesses and other components bound from modules.
 * 
 * @see TribefireWebPlatform
 * @see TribefireModule
 * @see PlatformLibrary
 */
@Abstract
public interface TribefirePlatform extends PlatformAssetNature {

	EntityType<TribefirePlatform> T = EntityTypes.T(TribefirePlatform.class);

}
