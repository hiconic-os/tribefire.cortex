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
package tribefire.cortex.testrunner.wire.space;

import java.util.List;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.testing.user.UserRelatedTestApi;
import tribefire.cortex.testrunner.TestRunningProcessor;
import tribefire.cortex.testrunner.api.ModuleTestRunner;
import tribefire.cortex.testrunner.api.RunTests;
import tribefire.cortex.testrunner.wire.TestRunningContract;
import tribefire.module.api.WireContractBindingBuilder;
import tribefire.module.wire.contract.HardwiredDeployablesContract;
import tribefire.module.wire.contract.RequestUserRelatedContract;
import tribefire.module.wire.contract.ResourceProcessingContract;
import tribefire.module.wire.contract.SystemUserRelatedContract;
import tribefire.module.wire.contract.TribefireModuleContract;

@Managed
public class TestRunnerModuleSpace implements TribefireModuleContract {

	@Import
	private RequestUserRelatedContract requestUserRelated;

	@Import
	private SystemUserRelatedContract systemUserRelated;

	@Import
	private HardwiredDeployablesContract hardwiredDeployables;

	@Import
	private ResourceProcessingContract resourceProcessing;

	@Import
	private TestRunningSpace testRunning;

	@Override
	public void bindWireContracts(WireContractBindingBuilder bindings) {
		bindings.bind(TestRunningContract.class, testRunning);
	}

	@Override
	public void onBeforeBinding() {
		UserRelatedTestApi.systemSessionFactory = systemUserRelated.sessionFactory();
		UserRelatedTestApi.userSessionFactory = requestUserRelated.sessionFactory();
	}

	@Override
	public void bindHardwired() {
		hardwiredDeployables.bindOnExistingServiceDomain("cortex") //
				.serviceProcessor("test.running.processor", "Test Running Processor", RunTests.T, testRunningProcessor());
	}

	@Managed
	private TestRunningProcessor testRunningProcessor() {
		TestRunningProcessor bean = new TestRunningProcessor();
		bean.setResourceBuilder(resourceProcessing.transientResourceBuilder());

		List<ModuleTestRunner> testRunners = testRunning.registry().getTestRunners();
		bean.setTestRunners(testRunners);

		return bean;
	}

}
