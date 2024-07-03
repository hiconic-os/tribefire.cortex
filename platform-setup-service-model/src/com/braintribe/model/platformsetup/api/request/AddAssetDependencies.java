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

import java.util.List;
import java.util.Set;

import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.asset.selector.DependencySelector;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface AddAssetDependencies extends PlatformAssetRequest {

	EntityType<AddAssetDependencies> T = EntityTypes.T(AddAssetDependencies.class);

	PlatformAsset getDepender();
	void setDepender(PlatformAsset depender);
	
	List<PlatformAsset> getDependencies();
	void setDependencies(List<PlatformAsset> dependencies);
	
	boolean getRemoveRedundantDepsOfDepender();
	void setRemoveRedundantDepsOfDepender(boolean removeRedundantDepsOfDepender);
	
	boolean getAsGlobalSetupCandidate();
	void setAsGlobalSetupCandidate(boolean asGlobalSetupCandidate);
	
	boolean getAsDesigntimeOnly();
	void setAsDesigntimeOnly(boolean asDesigntimeOnly);
	
	boolean getAsRuntimeOnly();
	void setAsRuntimeOnly(boolean asRuntimeOnly);
	
	Set<String> getForStages();
	void setForStages(Set<String> forStages);
	
	DependencySelector getCustomSelector();
	void setCustomSelector(DependencySelector customSelector);
	
}
