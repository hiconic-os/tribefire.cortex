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

import com.braintribe.model.deployment.Deployable;

/**
 * <p>
 * A {@link DeployRegistry}-bound listener of deployment operations.
 * 
 * @author dirk.scheffler
 */
public interface DeployRegistryListener {
	
	void onDeploy(Deployable deployable, DeployedUnit deployedUnit);
	
	void onUndeploy(Deployable deployable, DeployedUnit deployedUnit);

}
