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
package tribefire.cortex.asset.resolving.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;

import com.braintribe.model.artifact.Dependency;
import com.braintribe.model.artifact.PartTuple;
import com.braintribe.model.artifact.VirtualPart;
import com.braintribe.model.artifact.processing.part.PartTupleProcessor;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.manipulation.parser.api.MutableGmmlManipulatorParserConfiguration;
import com.braintribe.model.processing.manipulation.parser.api.ParseResponse;
import com.braintribe.model.processing.manipulation.parser.impl.Gmml;
import com.braintribe.model.processing.manipulation.parser.impl.ManipulatorParser;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;

import tribefire.cortex.asset.resolving.api.PlatformAssetResolvingConstants;

public class DependencyDecoding implements PlatformAssetResolvingConstants {
	private static PartTuple dependencyPartTuple = PartTupleProcessor.fromString(PART_IDENTIFIER_DEPENDENCY_MAN);
	private static MutableGmmlManipulatorParserConfiguration manipulationParserConfig;
	
	public static Map<String, Object> decodeDependency(Dependency dependency) {
		Map<String, Object> info = dependency.getMetaData().stream()
				.map(m -> {
					if (m instanceof VirtualPart) {
						VirtualPart part = (VirtualPart)m;
						if (PartTupleProcessor.equals(part.getType(), dependencyPartTuple))
							return part;
					}
					return null;
				})
				.filter(p -> p != null)
				.map(DependencyDecoding::loadNatureFromVirtualPart)
				.findFirst().orElse( Collections.emptyMap());
		
		return info;
	}
	
	/**
	 * load the {@link PlatformAssetNature} from a {@link VirtualPart}
	 * @param virtualPart - the {@link VirtualPart}
	 * @return - the {@link PlatformAssetNature} contained
	 */
	private static Map<String, Object> loadNatureFromVirtualPart( VirtualPart virtualPart) {
		String payload = virtualPart.getPayload();
		try (Reader reader = new StringReader(payload)) {
			return loadNatureFromReader(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}

	private static Map<String, Object> loadNatureFromReader(Reader reader) {
		BasicManagedGmSession entityManager = new BasicManagedGmSession();
		ParseResponse response = null;
		response = ManipulatorParser.parse( reader, entityManager, getManipulationParserConfig());
		
		return response.variables;
	}

	private static MutableGmmlManipulatorParserConfiguration getManipulationParserConfig() {
		if (manipulationParserConfig == null) {
			manipulationParserConfig = Gmml.manipulatorConfiguration();
		}
		return manipulationParserConfig;
	}

}
