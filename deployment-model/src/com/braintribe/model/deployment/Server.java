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
package com.braintribe.model.deployment;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Represents a remote system, which provides the actual service (i.e. does the work we need) expected from a "remote deployable". A remote deployable
 * is any deployable whose expert does not implement the required service directly, but is just a remote proxy for some external system.
 * <p>
 * The other system might also be example be another tribefire.
 * <p>
 * TODO explain when to use such a heterogeneous architecture.
 * 
 * @author peter.gazdik
 */
@Abstract
public interface Server extends GenericEntity {

    EntityType<Server> T = EntityTypes.T(Server.class);

}