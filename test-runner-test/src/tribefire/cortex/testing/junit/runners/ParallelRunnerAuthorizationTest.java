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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.braintribe.testing.category.SpecialEnvironment;

import tribefire.cortex.testing.junit.ActualTest;
import tribefire.cortex.testing.junit.runner.AuthorizingParallelRunner;

/**
 * This unit test only tests the parallelization but not the authorization of the corresponding thread. There is a
 * dedicated integration test for that.
 *
 * @author Neidhart.Orlich
 */
@Category({SpecialEnvironment.class, ActualTest.class}) // does not work with BtAntTasks as it doesn't support custom runners
@RunWith(AuthorizingParallelRunner.class)
public class ParallelRunnerAuthorizationTest {
	private static final Set<Thread> previousThreads = new HashSet<>();

	@Test
	public void test1() throws Exception {
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

	@Test
	public void test4() throws Exception {
		test();
	}

	private void test() throws Exception {
		assertThat(previousThreads.add(Thread.currentThread())).as("Executed several tests in the same thread").isTrue();

		Thread.sleep(10);
	}

}
