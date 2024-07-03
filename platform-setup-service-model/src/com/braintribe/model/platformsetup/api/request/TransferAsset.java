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
import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformsetup.api.response.AssetResource;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * <p>
 * Basic interface for transferring assets to several kind of repos (e.g. local maven, remote maven or git). <br/>
 * The type of transfer is defined by {@code transferOperation}. Example transfer options are <i>deploy</i> or
 * <i>install</i>.
 * 
 */
public interface TransferAsset extends PlatformAssetRequest, AccessDataRequest {

	EntityType<TransferAsset> T = EntityTypes.T(TransferAsset.class);

	@Mandatory
	PlatformAsset getAsset();
	void setAsset(PlatformAsset asset);

	String getTransferOperation();
	void setTransferOperation(String transferOperation);

	@Override
	EvalContext<AssetResource> eval(Evaluator<ServiceRequest> evaluator);

}
