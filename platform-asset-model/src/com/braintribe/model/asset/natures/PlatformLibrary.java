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

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * A library aggregating asset, which marks it's dependencies as direct dependencies of the {@link TribefirePlatform}.
 * <p>
 * Explanation: A platform setup consists of the main (platform) classpath, and one classpath per module. In case of
 * modules, only it's models and gm-api dependencies are guaranteed to be projected onto the main classpath, while other
 * libraries are usually put to the module's classpath(s). If you, however, want to force a certain library to the main classpath, you can do so
 * 
 * <p>
 * NOTE that this asset is only relevant if there is a {@link TribefirePlatform} present in your setup.
 */
public interface PlatformLibrary extends PlatformAssetNature, SupportsNonAssetDeps {

	final EntityType<PlatformLibrary> T = EntityTypes.T(PlatformLibrary.class);

}
