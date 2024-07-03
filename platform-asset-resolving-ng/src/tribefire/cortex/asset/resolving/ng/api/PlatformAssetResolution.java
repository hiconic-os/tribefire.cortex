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

import java.util.SortedSet;

import com.braintribe.gm.model.reason.Reason;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;

import tribefire.cortex.asset.resolving.ng.impl.PlatformAssetSolution;

public interface PlatformAssetResolution {
	PlatformAssetSolution getSolutionFor(AnalysisArtifact solution);
	
	PlatformAssetSolution getSolutionForName(String name);
	
	PlatformAssetSolution getSolutionFor(CompiledDependencyIdentification dependency);

	SortedSet<PlatformAssetSolution> getSolutions();
	
	AnalysisArtifactResolution artifactResolution();
	
	Reason getFailure();
	
	default boolean hasFailed() { return getFailure() != null; }
}
