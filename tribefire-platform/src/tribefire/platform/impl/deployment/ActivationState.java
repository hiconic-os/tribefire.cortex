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

/**
 * <p>
 * The possible activation states of a cartridge instance.
 * 
 */
public enum ActivationState {

	/**
	 * No activation occurred
	 */
	inactive,

	/**
	 * No activation occurred for the instance as it was unauthorized
	 */
	unauthorized,

	/**
	 * The instance is activated
	 */
	activated,

	/**
	 * The instance is currently being activated
	 */
	activating,

	/**
	 * The instance is deactivated
	 */
	deactivated,

	/**
	 * The instance is currently being deactivated
	 */
	deactivating;

}
