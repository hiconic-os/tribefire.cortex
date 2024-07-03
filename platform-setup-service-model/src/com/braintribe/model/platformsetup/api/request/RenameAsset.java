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
package com.braintribe.model.platformsetup.api.request;

import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface RenameAsset extends PlatformAssetRequest {

	EntityType<RenameAsset> T = EntityTypes.T(RenameAsset.class);

	@Mandatory
	PlatformAsset getAsset();
	void setAsset(PlatformAsset asset);
	
	String getName();
	void setName(String name);

	String getGroupId();
	void setGroupId(String groupId);
	
	String getVersion();
	void setVersion(String version);
	
}
