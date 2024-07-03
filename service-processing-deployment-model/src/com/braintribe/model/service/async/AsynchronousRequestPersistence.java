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
package com.braintribe.model.service.async;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface AsynchronousRequestPersistence extends GenericEntity {

	EntityType<AsynchronousRequestPersistence> T = EntityTypes.T(AsynchronousRequestPersistence.class);

	IncrementalAccess getPersistence();
	void setPersistence(IncrementalAccess persistence);

	@Initializer("enum(com.braintribe.model.service.async.AsynchronousRequestPersistenceStrategy,never)")
	AsynchronousRequestPersistenceStrategy getPersistenceStrategy();
	void setPersistenceStrategy(AsynchronousRequestPersistenceStrategy persistenceStrategy);

	@Initializer("'default'")
	String getDiscriminator();
	void setDiscriminator(String descriminator);

	ResourceKind getPersistResponseResourcesStrategy();
	void setPersistResponseResourcesStrategy(ResourceKind persistResponseResources);
}
