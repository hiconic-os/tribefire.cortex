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
package tribefire.cortex.asset.resolving.ng.api;

import java.util.List;

import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.core.expert.api.DenotationMap;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.cortex.asset.resolving.ng.impl.BasicAssetResolutionContext;

public interface AssetResolutionContext extends DependencySelectorContext {
	ManagedGmSession session();
	DenotationMap<PlatformAssetNature, List<String>> natureParts();
	boolean selectorFiltering();
	boolean includeDocumentation();
	boolean verboseOutput();
	boolean lenient();
	
	static AssetResolutionContextBuilder build() {
		return new BasicAssetResolutionContext();
	}
}
