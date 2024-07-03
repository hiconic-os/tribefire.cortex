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
import com.braintribe.model.generic.reflection.EntityType;

/**
 * This interface simplifies the {@link ComponentBinder} because most of the binder implementations
 * make no actual difference between the componentType and the base denotation type.
 * @author dirk.scheffler
 *
 * @param <D>
 *                 denotation base type and in the same moment component type
 * @param <T>
 *                 type of the instance to be bound
 */
public interface IndirectComponentBinder<C extends Deployable, D extends C, T> extends ComponentBinder<D, T> {

	@Override
	EntityType<C> componentType();
}
