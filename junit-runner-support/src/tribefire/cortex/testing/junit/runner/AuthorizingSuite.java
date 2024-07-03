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

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.braintribe.thread.api.ThreadContextScoping;

public class AuthorizingSuite extends Suite {
	
	public AuthorizingSuite(RunnerBuilder runnerBuilder, Class<?>[] classes, ThreadContextScoping tcs) throws InitializationError {
		super(runnerBuilder, classes);
		passTcsToChildren(tcs);
	}
	
	private void passTcsToChildren(ThreadContextScoping tcs) {
		for (Runner runner : getChildren())
			if (runner instanceof ThreadContextScopingAware)
				((ThreadContextScopingAware) runner).setThreadContextScoping(tcs);
	}
}
