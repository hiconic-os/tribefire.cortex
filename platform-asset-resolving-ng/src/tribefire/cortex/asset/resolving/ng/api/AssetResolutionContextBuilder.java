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
import java.util.Set;

import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.core.expert.api.DenotationMap;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

public interface AssetResolutionContextBuilder {
	AssetResolutionContextBuilder session(ManagedGmSession session);
	AssetResolutionContextBuilder natureParts(DenotationMap<PlatformAssetNature, List<String>> natureParts);
	AssetResolutionContextBuilder selectorFiltering(boolean selectorFiltering);
	AssetResolutionContextBuilder includeDocumentation(boolean includeDocument);
	AssetResolutionContextBuilder verboseOutput(boolean verboseOutput);
	AssetResolutionContextBuilder lenient(boolean lenient);
	AssetResolutionContextBuilder runtime(boolean runtime);
	AssetResolutionContextBuilder designtime(boolean designtime);
	AssetResolutionContextBuilder stage(String stage);
	AssetResolutionContextBuilder tags(Set<String> tags);

	AssetResolutionContext done();
}
