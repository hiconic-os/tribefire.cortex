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
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;

import com.braintribe.devrock.mc.core.declared.commons.HashComparators;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.essential.PartIdentification;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.manipulation.parser.api.MutableGmmlManipulatorParserConfiguration;
import com.braintribe.model.processing.manipulation.parser.api.ParseResponse;
import com.braintribe.model.processing.manipulation.parser.impl.Gmml;
import com.braintribe.model.processing.manipulation.parser.impl.ManipulatorParser;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;

import tribefire.cortex.asset.resolving.ng.api.PlatformAssetResolvingConstants;

public class DependencyDecoding implements PlatformAssetResolvingConstants {
	private static PartIdentification dependencyPartTuple = PartIdentification.parse(PART_IDENTIFIER_DEPENDENCY_MAN);
	private static MutableGmmlManipulatorParserConfiguration manipulationParserConfig;

	public static Map<String, Object> decodeDependency(AnalysisDependency dependency) {
		Map<String, Object> info = dependency.getOrigin().getParts().entrySet().stream()
				.filter(e -> HashComparators.partIdentification.compare(dependencyPartTuple, e.getKey())) //
				.map(Map.Entry::getValue) //
				.map(DependencyDecoding::decodeMap) //
				.findFirst() //
				.orElse(Collections.emptyMap()); //

		return info;
	}

	/**
	 * load the {@link PlatformAssetNature} from a {@link VirtualPart}
	 * 
	 * @param virtualPart - the {@link VirtualPart}
	 * @return - the {@link PlatformAssetNature} contained
	 */
	private static Map<String, Object> decodeMap(String payload) {
		try (Reader reader = new StringReader(payload)) {
			return loadNatureFromReader(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}

	private static Map<String, Object> loadNatureFromReader(Reader reader) {
		BasicManagedGmSession entityManager = new BasicManagedGmSession();
		ParseResponse response = null;
		response = ManipulatorParser.parse(reader, entityManager, getManipulationParserConfig());

		return response.variables;
	}

	private static MutableGmmlManipulatorParserConfiguration getManipulationParserConfig() {
		if (manipulationParserConfig == null) {
			manipulationParserConfig = Gmml.manipulatorConfiguration();
		}
		return manipulationParserConfig;
	}

}
