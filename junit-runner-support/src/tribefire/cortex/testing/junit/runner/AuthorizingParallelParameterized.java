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
package tribefire.cortex.testing.junit.runner;

import org.junit.runners.Parameterized;

import com.braintribe.thread.api.ThreadContextScoping;

/**
 * An extension of the JUnit {@link Parameterized} runner, which executes the tests for each parameter set concurrently.
 * <p>
 * You can specify the maximum number of parallel test threads using the system property <code>maxParallelTestThreads</code>. If this system property
 * is not specified, the maximum number of test threads will be the number of {@link Runtime#availableProcessors() available processors}.
 * 
 * @see <a href="https://github.com/MichaelTamm/junit-toolbox/blob/master/src/main/java/com/googlecode/junittoolbox/ParallelParameterized.java">JUnit
 *      Toolbox</a>
 */
public class AuthorizingParallelParameterized extends Parameterized implements ThreadContextScopingAware {

	private final AuthorizingParallelScheduler scheduler;

	public AuthorizingParallelParameterized(Class<?> klass) throws Throwable {
		super(klass);
		scheduler = new AuthorizingParallelScheduler();
		setScheduler(scheduler);
	}

	@Override
	public void setThreadContextScoping(ThreadContextScoping tcs) {
		scheduler.tcs = tcs;
	}
}