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

import com.braintribe.model.accessapi.AccessDataRequest;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Basic request type for defining lightweight asset requests.
 * <ul>
 * <li>The {@code accessId} defines the current domain where the request is being triggered (e.g. <i>cortex, workbench, ...</i>)
 * <li>The {@code transferOperation} is optional. There the way of automatic publishing an asset can be configured (local install, deploy, ...)
 * </ul>
 */
@Abstract
public interface TrunkAssetRequestForAccess extends AccessDataRequest {

	EntityType<TrunkAssetRequestForAccess> T = EntityTypes.T(TrunkAssetRequestForAccess.class);

	String ACCESS_ID_SETUP = "access.setup";

	@Override
	default String domainId() {
		return ACCESS_ID_SETUP;
	}

	@Mandatory
	String getAccessId();
	void setAccessId(String accessId);

	String getTransferOperation();
	void setTransferOperation(String transferOperation);

}
