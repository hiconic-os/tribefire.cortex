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
package tribefire.cortex.testing.junit.runners;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.braintribe.testing.category.SpecialEnvironment;

import tribefire.cortex.testing.junit.ActualTest;
import tribefire.cortex.testing.junit.runner.AuthorizingParallelParameterized;

/**
 * This unit test only tests the parallelization but not the authorization of the corresponding thread. There is a
 * dedicated integration test for that.
 *
 * @author Neidhart.Orlich
 *
 */
@Category({SpecialEnvironment.class, ActualTest.class}) // does not work with BtAntTasks as it doesn't support custom runners
@RunWith(AuthorizingParallelParameterized.class)
public class ParallelParameterizedAuthorizationTest {
	private static final Set<Thread> prevoiusThreads = new HashSet<>();
	private static final Object[][] mockParameters = { { 12, "777" }, { 23, "777" }, { 34, "777" } };
	private int a;
	private String b;

	@Parameters(name = "{index}: {0},{1}")
	public static Iterable<Object[]> data() throws Exception {
		return Arrays.asList(mockParameters);
	}

	public ParallelParameterizedAuthorizationTest(int a, String b) {
		this.a = a;
		this.b = b;

	}

	@Test
	public void test1() throws Exception {
		// As opposed to the AuthorizingParallelRunner, the AuthorizingParallelParameterized will only parallelize
		// groups of test methods
		// So all tests with the same parameters run in the same thread.
		assertThat(prevoiusThreads.add(Thread.currentThread()))
				.as("Executed several tests in the same thread even though they had different parameters: " + Thread.currentThread()).isTrue();
		Thread.sleep(10);
		test();
	}

	@Test
	public void test2() throws Exception {
		test();
	}

	@Test
	public void test3() throws Exception {
		test();
	}

	private void test() throws Exception {
		assertThat(a).isIn(12, 23, 34);
		assertThat(b).isEqualTo("777");
	}

}
