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

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.module.api.DenotationEnricher.EnrichFunction;
import tribefire.module.api.DenotationMorpher.MorphFunction;

/**
 * A registry for {@link DenotationTransformer}s.
 * 
 * @author peter.gazdik
 */
public interface DenotationTransformerRegistry {

	/**
	 * Registers a {@link DenotationMorpher} via {@link #registerMorpher(String, EntityType, EntityType, MorphFunction)}, but setting the name to
	 * "Standard_SourceType_To_TargetType" where "SourceType" and "TargetType" are {@link EntityType#getShortName() short names} of given source and
	 * target types.
	 */
	default <S extends GenericEntity, T extends GenericEntity> void registerStandardMorpher( //
			EntityType<S> sourceType, EntityType<T> targetType, MorphFunction<S, T> transformFunction) {

		String name = "Standard_" + sourceType.getShortName() + "_TO_" + targetType.getShortName();

		registerMorpher(name, sourceType, targetType, transformFunction);
	}

	/** Registers a {@link DenotationMorpher} created by {@link DenotationMorpher#create}. */
	default <S extends GenericEntity, T extends GenericEntity> void registerMorpher( //
			String name, EntityType<S> sourceType, EntityType<T> targetType, MorphFunction<S, T> transformFunction) {

		registerMorpher(DenotationMorpher.create(name, sourceType, targetType, transformFunction));
	}

	void registerMorpher(DenotationMorpher<?, ?> morpher);

	/** Registers a {@link DenotationEnricher} created by {@link DenotationEnricher#create}. */
	default <E extends GenericEntity> void registerEnricher(String name, EntityType<E> type, EnrichFunction<E> enrichFunction) {
		registerEnricher(DenotationEnricher.create(name, type, enrichFunction));
	}

	void registerEnricher(DenotationEnricher<?> enricher);
}
