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

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformsetup.api.data.AssetNature;
import com.braintribe.model.platformsetup.api.response.AssetCollection;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Returns a {@link AssetCollection} based on the provided filter criteria of
 * this request.
 * 
 * @author christina.wilpernig
 */
public interface GetAssets extends PlatformAssetRequest {

	EntityType<GetAssets> T = EntityTypes.T(GetAssets.class);
	
	@Override
	EvalContext<AssetCollection> eval(Evaluator<ServiceRequest> evaluator);

	@Override
	@Initializer("'setup'")
	String getDomainId();
	
	Set<AssetNature> getNature();
	void setNature(Set<AssetNature> nature);
	
	boolean getSetupAssets();
	void setSetupAssets(boolean setupAssets);
	
	@Initializer("true")
	Boolean getEffective();
	void setEffective(Boolean effective);
	
	Set<String> getRepoOrigin();
	void setRepoOrigin(Set<String> repoOrigin);
	
	Set<String> getGroupId();
	void setGroupId(Set<String> groupId);
	
	Set<String> getName();
	void setName(Set<String> name);

//	String getDependencyDepth();
//	void setDependencyDepth(String dependencyDepth);
	
}
