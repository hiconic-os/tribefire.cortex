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

import com.braintribe.wire.api.space.WireSpace;

/**
 * Provides the names of the messaging destinations used by the platform messaging.
 */
public interface MessagingDestinationsContract extends WireSpace {

	String multicastRequestTopicName();

	String multicastResponseTopicName();

	String trustedRequestQueueName();

	String trustedResponseTopicName();

	String heartbeatTopicName();

	String unlockTopicName();

	String dblBroadcastTopicName();

	String remoteToDblTopicName();

	String remoteToDblQueueName();

	String prefixName(String name);

}
