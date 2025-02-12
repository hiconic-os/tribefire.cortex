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
package com.braintribe.model.processing.cortex.priming;

import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static java.util.Collections.emptyList;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.model.access.collaboration.persistence.DirectGmmlInitializer;
import com.braintribe.model.access.collaboration.persistence.SimpleGmmlInitializer;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializer;
import com.braintribe.utils.lcd.StringTools;

/**
 * @author peter.gazdik
 */
public class TfEnvCsaPriming {

	private static final String DEFAULT_ACCESS = "cortex";

	public static List<PersistenceInitializer> getEnvironmentInitializersFor(String accessId) {
		return getEnvironmentInitializersFor(accessId, false);
	}

	public static List<PersistenceInitializer> getEnvironmentInitializersFor(String accessId, boolean preInit) {
		return getEnvironmentInitializerObjectsFor(accessId, preInit).stream() //
				.map(TfEnvCsaPriming::gmmlInitializerFor) //
				.collect(Collectors.toList());
	}

	private static PersistenceInitializer gmmlInitializerFor(Object gmmlSource) {
		if (gmmlSource instanceof File)
			return gmmlInitializerForFile((File) gmmlSource);
		else
			return gmmlInitializerForEnvVar((String) gmmlSource);
	}

	private static PersistenceInitializer gmmlInitializerForFile(File gmmlFile) {
		SimpleGmmlInitializer result = new SimpleGmmlInitializer();
		result.setStageName(extractStageName(gmmlFile));
		result.setGmmlFile(gmmlFile);

		return result;
	}

	private static String extractStageName(File gmmlFile) {
		String stageName = gmmlFile.getParent();
		if (stageName == null)
			throw new IllegalStateException("GMML file has no parent: " + gmmlFile.getPath());

		return stageName;
	}

	private static PersistenceInitializer gmmlInitializerForEnvVar(String varName) {
		DirectGmmlInitializer result = new DirectGmmlInitializer();
		result.setStageName("env:" + varName);
		result.setDataManSupplier(() -> TribefireRuntime.getProperty(varName));

		return result;
	}

	// Next method has package visibility so we can test it.

	/**
	 * Returns absolute files for file-based initializers, and Strings for environment variable based initializers - in
	 * which case the Strings are the names of the variables whose values are GMML scripts.
	 */
	/* package */ static List<Object> getEnvironmentInitializerObjectsFor(String accessId, boolean preInit) {
		return getEnvironmentInitializerObjectsFor(accessId, getEnvironmentManipulationPrimingPropertyName(preInit));
	}

	private static List<Object> getEnvironmentInitializerObjectsFor(String accessId, String primingPropertyName) {
		String primingExpression = TribefireRuntime.getProperty(primingPropertyName);

		if (StringTools.isEmpty(primingExpression))
			return emptyList();

		String[] entries = primingExpression.split(",");

		List<Object> result = newList();

		for (String entry : entries) {
			String[] splitEntry = splitEntry(entry, primingExpression, primingPropertyName);

			if (matchesAccessId(accessId, splitEntry[1]))
				result.add(createInitializerForEntry(splitEntry[0]));
		}

		return result;
	}

	private static boolean matchesAccessId(String accessId, String pattern) {
		if (pattern.startsWith("pattern:"))
			return accessId.matches(pattern.substring("pattern:".length()));
		else
			return accessId.equals(pattern);
	}

	private static Object createInitializerForEntry(String envPrimingValue) {
		if (envPrimingValue.toUpperCase().startsWith("ENV:"))
			return envPrimingValue.substring("ENV:".length());
		else
			return createAbsoluteGmmlFileFor(envPrimingValue);
	}

	private static String[] splitEntry(String entry, String primingExpression, String primingPropertyName) {
		String[] splitEntry = entry.split(">");
		switch (splitEntry.length) {
			case 1:
				return new String[] { entry, DEFAULT_ACCESS };
			case 2:
				return splitEntry;
			default:
				throw new IllegalArgumentException("Illegal GMML file entry (configured as '" + primingPropertyName + "' property) - '" + entry
						+ "'. Full property value:" + primingExpression);
		}
	}

	private static File createAbsoluteGmmlFileFor(String absoluteOrRelativeGmmlFileName) {
		File file = new File(absoluteOrRelativeGmmlFileName);

		return file.isAbsolute() ? file : new File(TribefireRuntime.getConfigurationDir(), absoluteOrRelativeGmmlFileName);
	}

	private static String getEnvironmentManipulationPrimingPropertyName(boolean preInit) {
		return preInit ? TribefireRuntime.ENVIRONMENT_MANIPULATION_PRIMING_PREINIT : TribefireRuntime.ENVIRONMENT_MANIPULATION_PRIMING;
	}
}
