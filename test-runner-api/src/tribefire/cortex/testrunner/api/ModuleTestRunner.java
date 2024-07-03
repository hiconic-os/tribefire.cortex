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
package tribefire.cortex.testrunner.api;

import java.io.File;

/**
 * Runner for tests of a single tribefire module. 
 * <p>
 * A test runner is able to find and run its tests and print its resulting reports to a folder. Multiple runners may
 * coexist, run in parallel and write their results to the same folder.
 *
 * @author Neidhart.Orlich
 * @see #testRunnerSelector()
 */
public interface ModuleTestRunner {

	/**
	 * Runs all tests that belong to this test runner and writes the results to provided folder. Note that a unique file
	 * name/path has to be ensured as the same folder may be used by other test runners which might run concurrently.
	 * @param testResultsRoot
	 *            root directory where test results should be stored.
	 */
	void runTests(RunTests request, File testResultsRoot);

	/**
	 * A selector to identify a single test runner or a group of test runners. It may be used to enable/disable this
	 * test runner from an outside configuration and will also determine the sub-folder in the test results .zip file
	 * where test reports are written to. Thus it must be also a valid child pathname according to
	 * {@link File#File(File, String)}
	 */
	String testRunnerSelector();

}
