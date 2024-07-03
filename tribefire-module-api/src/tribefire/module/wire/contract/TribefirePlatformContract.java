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
 * This contract offers everything that is available to a module from the core tribefire platform.
 * <p>
 * This can be used as the only import for platform contracts inside your space, and all other contracts available for given platform can be
 * discovered from here. Once you find which of the contracts offered here you want to use, you can of course import them directly, like this:
 * 
 * <pre>
 * &#64;Import
 * DeloymentContract deployment
 * </pre>
 * 
 * <p>
 * See {@link TribefireModuleContract} for information about what other contracts are available for given module.
 * 
 * @see TribefireModuleContract
 * @see ModuleReflectionContract
 * @see ModuleResourcesContract
 * 
 * @author peter.gazdik
 */
public interface TribefirePlatformContract extends WireSpace {

	// Please keep the contracts in alphabetical order

	ServiceBindersContract binders();

	DeploymentContract deployment();

	HardwiredDeployablesContract hardwiredDeployables();

	HardwiredExpertsContract hardwiredExperts();

	ModelApiContract modelApi();

	PlatformReflectionContract platformReflection();

	RequestProcessingContract requestProcessing();

	PlatformResourcesContract resources();

	SecurityContract security();

	ThreadingContract threading();

}
