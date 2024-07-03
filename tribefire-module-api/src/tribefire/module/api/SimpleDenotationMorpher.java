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
package tribefire.module.api;

import static com.braintribe.utils.lcd.NullSafe.nonNull;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * @author peter.gazdik
 */
public abstract class SimpleDenotationMorpher<S extends GenericEntity, T extends GenericEntity> implements DenotationMorpher<S, T> {

	private final EntityType<S> sourceType;
	private final EntityType<T> targetType;

	protected SimpleDenotationMorpher(EntityType<S> sourceType, EntityType<T> targetType) {
		this.sourceType = nonNull(sourceType, "sourceType");
		this.targetType = nonNull(targetType, "targetType");
	}

	// @formatter:off
	@Override public String name() { return getClass().getSimpleName(); }
	@Override public EntityType<S> sourceType() { return sourceType; }
	@Override public EntityType<T> targetType() { return targetType; }
	// @formatter:on

}
