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
package com.braintribe.model.platformsetup.api.response;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformsetup.api.request.TransferAsset;
import com.braintribe.model.resource.Resource;

/**
 * Response for {@link TransferAsset}, which previously installed the asset locally or deployed it remotely, but now simply returns a {@link Resource}
 * containing all the artifact parts zipped.
 */
public interface AssetResource extends PlatformAssetResponse {

	EntityType<AssetResource> T = EntityTypes.T(AssetResource.class);

	Resource getResource();
	void setResource(Resource resource);

}
