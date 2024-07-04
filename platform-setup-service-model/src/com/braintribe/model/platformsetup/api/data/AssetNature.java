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
package com.braintribe.model.platformsetup.api.data;

import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * Represents all available and supported {@link PlatformAssetNature natures}.
 * 
 * @author christina.wilpernig
 */
public enum AssetNature implements EnumBase {
	AssetAggregator,
	ContainerProjection,
	MarkdownDocumentation,
	MarkdownDocumentationConfig,
	ModelPriming,
	PlatformLibrary,
	RuntimeProperties,
	TribefireModule,
	PrimingModule,
	ManipulationPriming,
	ScriptPriming,
	ResourcePriming,
	TribefireWebPlatform;
	
	// TODO tomcatRuntime?
	
	public static final EnumType<AssetNature> T = EnumTypes.T(AssetNature.class);

	@Override
	public EnumType<AssetNature> type() {
		return T;
	}

}
