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
package tribefire.cortex.testrunner.wire;

import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.testrunner.api.ModuleTestRunner;
import tribefire.cortex.testrunner.api.TestRunnerRegistry;

/**
 * Wire contract to register {@link ModuleTestRunner}s in a {@link TestRunnerRegistry}.
 *
 * @author Neidhart.Orlich
 *
 */
public interface TestRunningContract extends WireSpace {

	public TestRunnerRegistry registry();

}
