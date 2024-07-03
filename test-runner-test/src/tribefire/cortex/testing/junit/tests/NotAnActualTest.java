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
package tribefire.cortex.testing.junit.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.testing.category.SpecialEnvironment;
import com.braintribe.testing.category.VerySlow;

/**
 * This is not an actual test and should never be executed as test. This just simulates a test for the test runners in
 * the actual tests.
 * <p>
 * The {@link SpecialEnvironment} category set on this test prevents this test to be executed in the CI. However it
 * can't be prevented to be executed from other test runners like in eclipse. So please just ignore its failure.
 *
 * @author Neidhart.Orlich
 *
 */
@Category(SpecialEnvironment.class)
public class NotAnActualTest {
	public static final String SUCCEEDING = "succeedingTest";
	public static final String SLOW = "slowTest";
	public static final String FAILING = "failingTest";
	public static final String CRASHING = "crashingTest";

	public static final Collection<String> runTests = new ArrayList<String>();

	public static boolean active;

	@Test
	public void failingTest() {
		if (active) {
			runTests.add(FAILING);
			assertTrue(false);
		}
	}

	@Test
	public void crashingTest() {
		if (active) {
			runTests.add(CRASHING);
			throw new RuntimeException("Intended Exception to simulate a crashing test. Please ignore!");
		}
	}

	@Test
	public void succeedingTest() {
		if (active) {
			runTests.add(SUCCEEDING);
		}
	}

	@Test
	@Category(VerySlow.class)
	public void slowTest() throws Exception {
		if (active) {
			Thread.sleep(500);
			runTests.add(SLOW);
		}
	}
}
