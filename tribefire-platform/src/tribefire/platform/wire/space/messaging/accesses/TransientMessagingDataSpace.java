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
package tribefire.platform.wire.space.messaging.accesses;

import com.braintribe.gm.marshaller.threshold.ThresholdPersistenceCleanupWorker;
import com.braintribe.model.extensiondeployment.HardwiredWorker;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.contract.MessagingRuntimePropertiesContract;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;
import tribefire.platform.wire.space.cortex.services.WorkerSpace;

@Managed
public class TransientMessagingDataSpace implements WireSpace {
	
	@Import
	private WorkerSpace worker;
	
	@Import
	private GmSessionsSpace gmSession;
	
	@Import
	private MessagingRuntimePropertiesContract messagingRuntimeProperties;
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		worker.manager().deploy(cleanupWorker());
	}
	
	@Managed
	private ThresholdPersistenceCleanupWorker cleanupWorker() {
		ThresholdPersistenceCleanupWorker bean = new ThresholdPersistenceCleanupWorker();
		bean.setWorkerIdentification(workerIdentification());
		bean.setCleanupInterval(messagingRuntimeProperties.TRIBEFIRE_MESSAGING_TRANSIENT_PERSISTENCE_CLEANUP_INTERVAL());
		bean.setResourceTtl(messagingRuntimeProperties.TRIBEFIRE_MESSAGING_TRANSIENT_PERSISTENCE_TTL());
		bean.setSessionFactory(gmSession.systemSessionSupplier(TribefireConstants.ACCESS_TRANSIENT_MESSAGING_DATA));
		return bean;
	}
	
	private HardwiredWorker workerIdentification() {
		HardwiredWorker bean = HardwiredWorker.T.create();
		String id = "hardwired:" + ThresholdPersistenceCleanupWorker.class.getSimpleName();
		bean.setId(id);
		bean.setGlobalId(id);
		return bean;
	}
}
