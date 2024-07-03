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
package tribefire.module.wire.contract;

import com.braintribe.model.dcsadeployment.DcsaSharedStorage;
import com.braintribe.model.leadershipdeployment.LeadershipManager;
import com.braintribe.model.lockingdeployment.Locking;
import com.braintribe.model.messagingdeployment.Messaging;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.transport.messaging.api.MessagingConnectionProvider;
import com.braintribe.wire.api.space.WireSpace;

/**
 * @author peter.gazdik
 */
public interface ClusterBindersContract extends WireSpace {

	ComponentBinder<Messaging, MessagingConnectionProvider<?>> messaging();

	ComponentBinder<Locking, com.braintribe.model.processing.lock.api.Locking> locking();

	ComponentBinder<LeadershipManager, tribefire.cortex.leadership.api.LeadershipManager> leadershipManager();

	ComponentBinder<DcsaSharedStorage, com.braintribe.model.access.collaboration.distributed.api.DcsaSharedStorage> dcsaSharedStorage();

}
