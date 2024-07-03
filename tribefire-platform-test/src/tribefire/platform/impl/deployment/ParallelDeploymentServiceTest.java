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
package tribefire.platform.impl.deployment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.processing.deployment.api.DeploymentService;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;

import tribefire.platform.impl.deployment.wire.contract.ParallelDeploymentServiceContract;

public class ParallelDeploymentServiceTest {

	private List<Deployable> deployables;

	@Before
	public void setup() {

		deployables = createDeployables();

	}

	private List<Deployable> createDeployables() {
		List<Deployable> deployables = new ArrayList<>();

		TestPrefixedIdGenerator generator = TestPrefixedIdGenerator.T.create();
		generator.setExternalId("generator.id");
		generator.setTestPrefix("somePrefix");
		
		
		TestWorker worker = TestWorker.T.create();
		worker.setExternalId("worker.id");
		worker.setGenerator(generator);
		

		deployables.add(worker);
		deployables.add(generator);
		
		return deployables;
	}

	@Test
	public void test() throws Exception {

		WireContext<ParallelDeploymentServiceContract> context = Wire
				.contextWithStandardContractBinding(ParallelDeploymentServiceContract.class).build();

		ParallelDeploymentServiceContract contract = context.contract();
		PersistenceGmSession session = contract.sessionProvider().get();

		TestDeployContext deployContext = new TestDeployContext();
		deployContext.setSession(session);
		deployContext.setDeployables(deployables);

		DeploymentService service = contract.service();

		service.deploy(deployContext);
		
		
		
		BlockingQueue<String> testQueue = contract.testQueue();
		
		
		Assertions.assertThat(testQueue.take()).isNotEqualTo("test-failed");
		Assertions.assertThat(testQueue.take()).isNotEqualTo("test-failed");
		Assertions.assertThat(testQueue.take()).isNotEqualTo("test-failed");

		service.undeploy(deployContext);
		
	}

//	private Future<Boolean> waitForAndCheckDeployables(ConfigurableDeploymentServiceContract contract, List<Deployable> deployables) {
//
//		Consumer<String> inDeploymentBlocker = contract.inDeploymentBlocker();
//
//		return Executors.newSingleThreadExecutor().submit(() -> {
//			deployables.forEach(d -> inDeploymentBlocker.accept(d.getExternalId()));
//			DeployRegistry deployRegistry = contract.deployRegistry();
//			
//			for (Deployable d : deployables) {
//				DeployedUnit unit = deployRegistry.resolve(d);
//				
//				TestPrefixedIdGeneratorExpert idGenerator = unit.findComponent(TestPrefixedIdGenerator.T);
////				System.out.println(idGenerator.get());
//
//			}
//
//			return true;
//		});
//
//	}

}
