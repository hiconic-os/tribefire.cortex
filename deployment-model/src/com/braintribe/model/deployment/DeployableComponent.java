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


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;
import com.braintribe.model.meta.data.ExplicitPredicate;
import com.braintribe.model.meta.data.ModelSkeletonCompatible;

/**
 * This {@link EntityTypeMetaData} serves as a marker for {@link Deployable} types.
 * 
 * <p>
 * When added to {@link com.braintribe.model.meta.GmEntityType}s representing a {@link Deployable} type, it marks the
 * related {@link Deployable} entity type as a component type, thus it can be used as a key for retrieving components
 * from a deployed unit.
 * 
 * <p>
 * This metadata is resolved in a non-exclusive way which results in multiple components per {@link Deployable}, leading
 * to the need of a bundling deployed unit.
 */
public interface DeployableComponent extends EntityTypeMetaData, ExplicitPredicate, ModelSkeletonCompatible {

	EntityType<DeployableComponent> T = EntityTypes.T(DeployableComponent.class);

}
