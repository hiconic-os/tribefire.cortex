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
package tribefire.cortex.manipulation.conversion;

import static com.braintribe.utils.SysPrint.spOut;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.processing.manipulation.parser.api.MutableGmmlParserConfiguration;
import com.braintribe.model.processing.manipulation.parser.impl.Gmml;
import com.braintribe.model.processing.manipulation.parser.impl.ManipulationParser;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.lcd.NullSafe;

import tribefire.cortex.manipulation.conversion.beans.EntityBean;
import tribefire.cortex.manipulation.conversion.code.CodeWriterParams;
import tribefire.cortex.manipulation.conversion.code.InitializerCodeWriter;
import tribefire.cortex.manipulation.conversion.code.InitializerSources;
import tribefire.cortex.manipulation.conversion.code.JscPool;
import tribefire.cortex.sourcewriter.JavaSourceClass;

/**
 * @author peter.gazdik
 */
public class GmmlToInitializerMain {

	public static final String[] initializers = { //
			"workbench", //

			// Processed
			// "audit-wb", //
			// "auth", //
			// "auth-wb", //
			// "cortex", //
			// "cortex-wb", //
			// "setup-wb", //
			// "user-sessions-wb", //
			// "user-statistics-wb", //
	};

	// ##########################################
	// ## . . . . . . . . Main . . . . . . . . ##
	// ##########################################

	public static void main(String[] args) {
		new GmmlToInitializerMain().run();
	}

	// ##########################################

	private void run() {
		for (String base : initializers)
			run(base);
	}

	private void run(String base) {
		spOut(base);

		InitContext context = new InitContext(base);
		CodeWriterParams params = newParams(context, base);
		InitializerSources sources = InitializerCodeWriter.writeInitializer(params);

		FileTools.write(context.outInitializerFile).string(sources.initializerSpace);
		FileTools.write(context.outLookupFile).string(sources.lookupContract);

		spOut("\tInitializer: " + printClassFile(context.outInitializerFile));
		spOut("\tLookup: " + context.outLookupFile.getAbsolutePath());
	}

	private CodeWriterParams newParams(InitContext context, String base) {
		Manipulation manipulation = parse(context);

		CodeWriterParams params = new CodeWriterParams();
		params.initializerPackage = context.initializerPackage;
		params.spacePrefix = context.spacePrefix;
		params.manipulation = manipulation;

		if (base.endsWith("-wb") || base.equals("workbench"))
			params.allowedRootTypeFilter = bean -> isWbRootBean(bean, params.jscPool);

		return params;
	}

	private boolean isWbRootBean(EntityBean<?> bean, JscPool jscPool) {
		JavaSourceClass jsc = bean.jsc;
		return jsc == jscPool.folderJsc || jsc == jscPool.wbPerscpectiveJsc;
	}

	private String printClassFile(File f) {
		return f.getParentFile().getAbsolutePath() + "\n\t\t(" + f.getName() + ":20)";
	}

	private Manipulation parse(InitContext context) {
		MutableGmmlParserConfiguration config = Gmml.configuration();
		config.setParseSingleBlock(true);

		spOut("\tParsing: " + context.input.getName());
		try (InputStream in = new FileInputStream(context.input)) {
			return ManipulationParser.parse(in, "UTF-8", config);

		} catch (IOException e) {
			throw new RuntimeException("Could not read input file: " + context.input.getAbsolutePath(), e);
		}
	}

	private static class InitContext {
		public final String initializerPackage;
		public final String spacePrefix;

		public final File input;

		public final File outInitializerFile;
		public final File outLookupFile;

		public InitContext(String base) {
			String baseSnake = base.replace("-", "_");
			String basePascal = toPascalCase(base, "-");

			this.initializerPackage = "tribefire.cortex.assets." + baseSnake + ".initializer.wire";
			this.spacePrefix = basePascal;

			this.input = new File("input/" + base + "-initial-priming.data.man");

			File folder = new File("C:/Peter/Work/git/tribefire.cortex.assets/" + base + "-initializer/src/tribefire/cortex/assets/" + baseSnake
					+ "/initializer/wire");

			this.outInitializerFile = new File(folder, "space/" + spacePrefix + "InitializerSpace.java");
			this.outLookupFile = new File(folder, "contract/" + spacePrefix + "LookupContract.java");
		}
	}

	/**
	 * Transforms the provided string to pascal case. <br>
	 * <br>
	 * 
	 * E.g: foo-bar -> FooBar (when delimiter is specified as '-')
	 */
	public static String toPascalCase(String originalString, String delimiterRegex) {
		NullSafe.nonNull(originalString, "value");
		NullSafe.nonNull(delimiterRegex, "delimiter");

		return Stream.of(originalString.split(delimiterRegex)) //
				.map(StringTools::capitalize) //
				.collect(Collectors.joining(""));

	}

}
