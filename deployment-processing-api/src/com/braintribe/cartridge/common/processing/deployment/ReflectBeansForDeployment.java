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
package com.braintribe.cartridge.common.processing.deployment;

import com.braintribe.wire.api.space.WireSpace;

/**
 * Use this marker interface on your {@link WireSpace}  
 * to support deployment reflection (lookup a BeanHolder for given instance) for all of its beans that are not automatically
 * reflected because of being scoped with the {@link DeploymentScope}
 *  
 * @author Dirk Scheffler
 *
 */
public interface ReflectBeansForDeployment {
	// intentionally left blank as this is a marker interface
}
