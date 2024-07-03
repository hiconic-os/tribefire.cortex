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

import java.util.Arrays;

import java.util.List;

import com.braintribe.common.lcd.Pair;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;
import com.braintribe.model.artifact.essential.ArtifactIdentification;
import com.braintribe.model.version.Version;

public class DependencyManagementTools {
	
	public static final List<String> excludedScopes = Arrays.asList("test", "provided");
	
	public static Pair<String, String> unversionedArtifactIdentification(ArtifactIdentification identification) {
		return new Pair<>(identification.getGroupId(), identification.getArtifactId());
	}

	public static String majorMinorIdentification(ArtifactIdentification identification, Version version) {
		StringBuilder builder = new StringBuilder();
		builder.append(identification.getGroupId());
		builder.append(':');
		builder.append(identification.getArtifactId());
		builder.append('#');
		builder.append(normalizeToMajorMinorIfPossible(version));
		
		return builder.toString();
	}
	
	public static String normalizeToMajorMinorIfPossible(Version version) {
		return version.getMajor() + "." + version.getMinor();
	}

	public static String majorMinorIdentification(AnalysisDependency dependency) {
		return majorMinorIdentification(dependency.getOrigin());
	}
	
	public static String majorMinorIdentification(CompiledDependencyIdentification dependency) {
		Version version = dependency.getVersion().asVersionIntervalList().get(0).lowerBound();
		return majorMinorIdentification(dependency, version);
	}
	
	public static String majorMinorIdentification(AnalysisArtifact solution) {
		Version version = solution.getOrigin().getVersion();
		return majorMinorIdentification(solution, version);
	}
}
