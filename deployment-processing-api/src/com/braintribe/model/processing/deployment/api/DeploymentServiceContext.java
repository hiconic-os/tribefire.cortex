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
package com.braintribe.model.processing.deployment.api;

import java.util.List;

import com.braintribe.model.deployment.Deployable;

/**
 * The context for {@link DeploymentService}'s operations.
 * 
 *
 */
public interface DeploymentServiceContext {

	List<Deployable> deployables();

	default boolean areDeployablesFullyFetched() {
		return false;
	}
	
	/**
	 * Callback method which is called by the {@link DeploymentService} when all deployables are internally marked
	 * to be in deployment but just before the actual deployments.
	 */
	default void deploymentStarted() {
		// NO OP
	}
	
	/**
	 * Callback method which is called by the {@link DeploymentService} immediately after the deployment of given
	 * deployable is started.
	 * 
	 * @param deployable
	 *            the deployable whose deployment has just started
	 */
	default void started(Deployable deployable) {
		// NO OP
	}

	/**
	 * Callback method which is called by the {@link DeploymentService} immediately after the deployment of given
	 * deployable is succeeded.
	 * 
	 * @param deployable
	 *            the successfully deployed deployable
	 */
	default void succeeded(Deployable deployable) {
		// NO OP		
	}

	/**
	 * Callback method which is called by the {@link DeploymentService} immediately after the deployment of given
	 * deployable is failed.
	 * 
	 * @param deployable
	 *            the deployable whose deployment failed
	 * @param failure
	 *            the error that happened during the deployment attempt
	 */
	default void failed(Deployable deployable, Throwable failure) {
		// NO OP		
	}

}
