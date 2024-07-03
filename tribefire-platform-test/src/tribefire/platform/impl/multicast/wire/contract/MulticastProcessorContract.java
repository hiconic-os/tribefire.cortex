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
package tribefire.platform.impl.multicast.wire.contract;

import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.transport.messaging.api.MessagingSessionProvider;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.multicast.TestMulticastConsumer;
import tribefire.platform.impl.topology.CartridgeLiveInstances;

public interface MulticastProcessorContract extends WireSpace {

	Long DEFAULT_TIMEOUT = 3000L;
	Long REQUEST_TIMEOUT = 2000L;

	InstanceId ADDRESSEE_A = instanceId("a", null);
	InstanceId ADDRESSEE_B = instanceId("b", null);

	InstanceId INSTANCE_A1 = instanceId(ADDRESSEE_A.getApplicationId(), "1");
	InstanceId INSTANCE_A2 = instanceId(ADDRESSEE_A.getApplicationId(), "2");
	InstanceId INSTANCE_B1 = instanceId(ADDRESSEE_B.getApplicationId(), "1");
	InstanceId INSTANCE_B2 = instanceId(ADDRESSEE_B.getApplicationId(), "2");

	InstanceId[] ALL_INSTANCES = { INSTANCE_A1, INSTANCE_A2, INSTANCE_B1, INSTANCE_B2 };

	CartridgeLiveInstances liveInstances();

	TestMulticastConsumer consumer(InstanceId instanceId);

	MessagingSessionProvider sessionProvider(InstanceId instanceId);

	static InstanceId instanceId(String appId, String nodeId) {
		InstanceId id = InstanceId.T.create();
		id.setApplicationId(appId);
		id.setNodeId(nodeId);
		return id;
	}
	
	Evaluator<ServiceRequest> evaluator();

}
