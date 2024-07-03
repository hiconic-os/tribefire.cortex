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
package com.braintribe.model.extensiondeployment.meta;

import com.braintribe.model.extensiondeployment.BinaryPersistence;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;
import com.braintribe.model.resource.source.ResourceSource;

/**
 * Specifies which {@link BinaryPersistence} should be used to store resources with given {@link ResourceSource}.
 * <p>
 * For example, we can configure a different persistence for file-system source and different for blob source (i.e. one stored in the DB), by
 * configuring this MD on the corresponding {@link ResourceSource} types.
 */
public interface PersistResourceWith extends EntityTypeMetaData {

	EntityType<PersistResourceWith> T = EntityTypes.T(PersistResourceWith.class);

	@Mandatory
	BinaryPersistence getPersistence();
	void setPersistence(BinaryPersistence value);

}