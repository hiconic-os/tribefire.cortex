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
package tribefire.cortex.testing.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tribefire.cortex.testing.junit.tests.NotAnActualTest.CRASHING;
import static tribefire.cortex.testing.junit.tests.NotAnActualTest.FAILING;
import static tribefire.cortex.testing.junit.tests.NotAnActualTest.SLOW;
import static tribefire.cortex.testing.junit.tests.NotAnActualTest.SUCCEEDING;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.testing.category.Slow;
import com.braintribe.testing.test.AbstractTest;

import tribefire.cortex.testing.junit.impl.JUnitModuleTestRunner;
import tribefire.cortex.testing.junit.tests.NotAnActualTest;
import tribefire.cortex.testrunner.api.RunTests;

@Category(ActualTest.class)
public class JUnitModuleTestRunnerTest extends AbstractTest {

	private static final RunTests request = RunTests.T.create();

	@Before
	public void prepareTests() {
		NotAnActualTest.runTests.clear();
	}

	@Test
	public void testCategories() {
		JUnitModuleTestRunner testRunner = createTestRunner();
		testRunner.setExcludedCategories(new Class[] { ActualTest.class, Slow.class });

		runTests(testRunner);

		assertThat(NotAnActualTest.runTests).containsExactlyInAnyOrder(CRASHING, FAILING, SUCCEEDING);
	}

	@Test
	public void testAll() {
		JUnitModuleTestRunner testRunner = createTestRunner();
		testRunner.setExcludedCategories(new Class[] { ActualTest.class });

		runTests(testRunner);

		assertThat(NotAnActualTest.runTests).containsExactlyInAnyOrder(CRASHING, FAILING, SUCCEEDING, SLOW);
	}

	@Test
	public void testTimeoutFailed() {
		JUnitModuleTestRunner testRunner = createTestRunner();
		testRunner.setExcludedCategories(new Class[] { ActualTest.class });
		testRunner.setTimeoutInMs(100);

		assertThatThrownBy(() -> runTests(testRunner)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void testTimeoutSucceeded() {
		JUnitModuleTestRunner testRunner = createTestRunner();
		testRunner.setExcludedCategories(new Class[] { ActualTest.class });
		testRunner.setTimeoutInMs(10000);

		runTests(testRunner);

		assertThat(NotAnActualTest.runTests).containsExactlyInAnyOrder(CRASHING, FAILING, SUCCEEDING, SLOW);
	}

	private JUnitModuleTestRunner createTestRunner() {
		JUnitModuleTestRunner jUnitTestRunner = new JUnitModuleTestRunner();
		jUnitTestRunner.setTestClassesSupplier(() -> new Class<?>[] { NotAnActualTest.class });
		return jUnitTestRunner;
	}

	private void runTests(JUnitModuleTestRunner testRunner) {
		assertThat(NotAnActualTest.runTests).isEmpty();

		NotAnActualTest.active = true;
		File reportRootDir = new File("reports");
		reportRootDir.mkdirs();
		testRunner.runTests(request, reportRootDir);
		NotAnActualTest.active = false;

	}
}
