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
package tribefire.cortex.asset.resolving.ng.impl;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.generic.reflection.ConfigurableCloningContext;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.version.Version;

public class PlatformAssetSolution implements Comparable<PlatformAssetSolution> {
	public AnalysisArtifact solution;
	public PlatformAssetNature nature;
	public PlatformAsset asset;	
	public List<AnalysisDependency> filteredDependencies = new ArrayList<>();
	public List<PlatformAssetSolution> dependencies = new ArrayList<>();
	
	public PlatformAssetSolution(AnalysisArtifact solution, PlatformAssetNature nature, ManagedGmSession session) {
		this.solution = solution;
		Version version = solution.getOrigin().getVersion();
		
		String id = solution.asString();
		
		this.nature = nature;
		
		this.asset = session.create(PlatformAsset.T);
		
		this.asset.setGlobalId(this.asset.entityType().getShortName() + ":" + id);
		this.asset.setNature(this.nature);
		this.asset.setGroupId(solution.getGroupId());
		this.asset.setName(solution.getArtifactId());
		
		Version majorMinorVersion = Version.create(version.getMajor(), version.getMinor());
		String majorMinorVersionStr = majorMinorVersion.asString();
		String versionStr = version.asString();
		
		String revisionStr = versionStr.substring(majorMinorVersionStr.length());
		
		if (revisionStr.startsWith("."))
			revisionStr = revisionStr.substring(1);
		else
			revisionStr = "0" + revisionStr;
		
		this.asset.setVersion(majorMinorVersionStr);
		ConfigurableCloningContext.build().supplyRawCloneWith(session);
		this.asset.setResolvedRevision(revisionStr);
	}
	
	@Override
	public int compareTo(PlatformAssetSolution o) {
		AnalysisArtifact o1 = this.solution;
		AnalysisArtifact o2 = o.solution;
		return Integer.compare(o1.getDependencyOrder(), o2.getDependencyOrder());		
	}
	
	@Override
	public String toString() {
		return solution.asString();
	}
}
