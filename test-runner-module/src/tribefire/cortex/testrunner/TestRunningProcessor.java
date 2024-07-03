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
package tribefire.cortex.testrunner;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.api.ResourceBuilder;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.archives.Archives;
import com.braintribe.utils.archives.zip.ZipContext;

import tribefire.cortex.testrunner.api.ModuleTestRunner;
import tribefire.cortex.testrunner.api.RunTests;

public class TestRunningProcessor implements ServiceProcessor<RunTests, Resource> {

	private static final Logger log = Logger.getLogger(TestRunningProcessor.class);

	private List<ModuleTestRunner> testRunners;

	private ResourceBuilder resourceBuilder;

	@Required
	public void setTestRunners(List<ModuleTestRunner> testRunners) {
		this.testRunners = testRunners;
	}

	@Required
	public void setResourceBuilder(ResourceBuilder resourceBuilder) {
		this.resourceBuilder = resourceBuilder;
	}

	@Override
	public Resource process(ServiceRequestContext requestContext, RunTests request) {
		File testResultsRoot = newTempDir();

		testRunners.forEach(r -> runTests(r, request, testResultsRoot));

		try (ZipContext zc = Archives.zip().pack(testResultsRoot)) {
			Resource result = resourceBuilder.newResource() //
					.withMimeType("application/zip") //
					.withName("test-results.zip") //
					.usingOutputStream(zc::to);

			return result;

		} finally {
			try {
				FileTools.deleteDirectoryRecursively(testResultsRoot);
			} catch (IOException e) {
				log.warn("Unable to delete directory with test results:" + testResultsRoot.getAbsolutePath(), e);
			}
		}
	}

	private void runTests(ModuleTestRunner testRunner, RunTests request, File testResultsRoot) {
		File testRunnerOutputFolder = new File(testResultsRoot, testRunner.testRunnerSelector());
		testRunner.runTests(request, testRunnerOutputFolder);
	}

	private File newTempDir() {
		try {
			return Files.createTempDirectory(getClass().getSimpleName()).toFile();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
