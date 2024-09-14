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
package tribefire.platform.wire.space.cortex.accesses;

import com.braintribe.model.extensiondeployment.HardwiredServiceProcessor;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.access.DispatchingPersistenceProcessor;
import tribefire.platform.wire.space.cortex.deployment.DeploymentSpace;

@Managed
public class AllAccessesCommonsSpace implements WireSpace {

	@Import
	private DeploymentSpace deployment;

	// ###########################################################
	// ## . . . . . Dispatching Persistence Processor . . . . . ##
	// ###########################################################

	public static final String DISPATCHING_PERSISTENCE_PROCESSOR_EXTERNAL_ID = "processor.persistence.dispatching";
	public static final String DISPATCHING_PERSISTENCE_PROCESSOR_GLOBAL_ID = "hardwired:access/" + DISPATCHING_PERSISTENCE_PROCESSOR_EXTERNAL_ID;
	
	@Managed
	public DispatchingPersistenceProcessor dispatchingPersistenceProcessor() {
		DispatchingPersistenceProcessor bean = new DispatchingPersistenceProcessor();
		bean.setDeployRegistry(deployment.registry());

		return bean;
	}

	@Managed
	public HardwiredServiceProcessor dispatchingPersistenceProcessorDeployable() {
		HardwiredServiceProcessor bean = HardwiredServiceProcessor.T.create();
		bean.setName("Hardwired Dispatching Persistence Processor");
		bean.setExternalId(DISPATCHING_PERSISTENCE_PROCESSOR_EXTERNAL_ID);
		bean.setGlobalId(DISPATCHING_PERSISTENCE_PROCESSOR_GLOBAL_ID);

		return bean;
	}

}
