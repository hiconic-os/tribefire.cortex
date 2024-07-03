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

/**
 * This contract binds everything that is available to a module from the core tribefire platform.
 * <p>
 * This can be used as the only import within the module, but of course any of the underlying contracts can be imported directly.
 * 
 * @see TribefireModuleContract
 * 
 * @author peter.gazdik
 */
public interface TribefireWebPlatformContract extends TribefirePlatformContract {

	// Please keep the contracts in alphabetical order !!!
	@Override
	WebPlatformBindersContract binders();

	@Override
	WebPlatformHardwiredDeployablesContract hardwiredDeployables();

	@Override
	WebPlatformHardwiredExpertsContract hardwiredExperts();

	@Override
	WebPlatformReflectionContract platformReflection();

	@Override
	WebPlatformResourcesContract resources();

	ClusterContract cluster();

	CryptoContract crypto();

	HttpContract http();

	MasterUserAuthContextContract masterUserAuthContext();

	WebPlatformMarshallingContract marshalling();

	MessagingContract messaging();

	RequestUserRelatedContract requestUserRelated();

	ResourceProcessingContract resourceProcessing();

	ServletsContract servlets();

	SystemToolsContract systemTools();

	SystemUserRelatedContract systemUserRelated();

	TopologyContract topology();

	TribefireConnectionsContract tribefireConnections();

	WorkerContract worker();

}
