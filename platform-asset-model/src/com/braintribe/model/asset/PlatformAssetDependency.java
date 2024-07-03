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
package com.braintribe.model.asset;

import com.braintribe.model.asset.selector.DependencySelector;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface PlatformAssetDependency extends GenericEntity {

	EntityType<PlatformAssetDependency> T = EntityTypes.T(PlatformAssetDependency.class);
	
	public static final String asset = "asset";
	public static final String selector = "selector";
	public static final String isGlobalSetupCandidate = "isGlobalSetupCandidate";
	
	PlatformAsset getAsset();
	void setAsset(PlatformAsset asset);
	
	DependencySelector getSelector();
	void setSelector(DependencySelector selector);
	
	boolean getIsGlobalSetupCandidate();
	void setIsGlobalSetupCandidate(boolean isGlobalSetupCandidate);
	
	boolean getSkipped();
	void setSkipped(boolean skipped);
	
}
