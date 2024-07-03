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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.consumable.Part;
import com.braintribe.model.artifact.essential.PartIdentification;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.manipulation.parser.api.MutableGmmlManipulatorParserConfiguration;
import com.braintribe.model.processing.manipulation.parser.api.ParseResponse;
import com.braintribe.model.processing.manipulation.parser.impl.Gmml;
import com.braintribe.model.processing.manipulation.parser.impl.ManipulatorParser;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;
import com.braintribe.model.resource.Resource;

import tribefire.cortex.asset.resolving.ng.api.PlatformAssetResolvingConstants;

public class PlatformAssetNatureLoading implements PlatformAssetResolvingConstants  {
	private static PartIdentification assetPartTuple = PartIdentification.parse(PART_IDENTIFIER_ASSET_MAN);
	private static MutableGmmlManipulatorParserConfiguration manipulationParserConfig;
	
	public PlatformAssetNature findNature(AnalysisArtifact solution) {
		Part part = solution.getParts().get(assetPartTuple.asString());
		
		if (part == null)
			return null;
		
		return loadNatureFromResource(part.getResource());
	}

	public PlatformAssetNature loadNatureFromResource(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.openStream(), "UTF-8")) {
			return loadNatureFromReader(reader); 
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}
	
	private PlatformAssetNature loadNatureFromReader(Reader reader) {
		BasicManagedGmSession entityManager = new BasicManagedGmSession();
		ParseResponse response = null;
		response = ManipulatorParser.parse( reader, entityManager, getManipulationParserConfig());
		
		Object natureCandidate = response.variables.get("$nature");
		// if $nature isn't set, the we use $natureType
		if (natureCandidate == null) {
			Object natureTypeCandidate = response.variables.get("$natureType");
			// $natureType must be set, so complain otherwise 
			if (natureTypeCandidate == null) {
				throw new IllegalStateException("neither $nature nor $natureType was given but one is at least required");	
			}
			// must be an entity type (actually a MetaData)
			if (natureTypeCandidate instanceof EntityType) {
				@SuppressWarnings("rawtypes")
				EntityType entityType = (EntityType) natureTypeCandidate;
				natureCandidate = entityType.create();
				
			}
			else {
				throw new IllegalStateException("processing the virtual part yielded an unexpected value for the $natureType variable: " + natureTypeCandidate);		
			}
		}
		// nothing was retrieved 
		if (!(natureCandidate instanceof PlatformAssetNature)) {
			throw new IllegalStateException("processing the virtual part yielded an unexpected value for the nature: " + natureCandidate);	
		}

		return (PlatformAssetNature) natureCandidate;
	}

	private static MutableGmmlManipulatorParserConfiguration getManipulationParserConfig() {
		if (manipulationParserConfig == null) {
			manipulationParserConfig = Gmml.manipulatorConfiguration();
		}
		return manipulationParserConfig;
	}

}
