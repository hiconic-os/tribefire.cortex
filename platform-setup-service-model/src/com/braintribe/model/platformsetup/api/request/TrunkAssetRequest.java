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

import java.util.Set;

import com.braintribe.model.accessapi.AccessDataRequest;
import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface TrunkAssetRequest extends PlatformAssetRequest, AccessDataRequest {

	EntityType<TrunkAssetRequest> T = EntityTypes.T(TrunkAssetRequest.class);
	
	public static final String asset = "asset";
	public static final String transferOperation = "transferOperation";
	public static final String roles = "roles";
	
	@Mandatory
	PlatformAsset getAsset();
	void setAsset(PlatformAsset asset);
	
	String getTransferOperation();
	void setTransferOperation(String transferOperation);
	
	Set<String> getRoles();
	void setRoles(Set<String> roles);

}
