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

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * {@link DenotationTransformer} which creates a new instance of type {@code T} from an instance of type {@code S}.
 * 
 * @see DenotationTransformer
 * @see DenotationEnricher
 * 
 * @author peter.gazdik
 */
public interface DenotationMorpher<S extends GenericEntity, T extends GenericEntity> extends DenotationTransformer<S, T> {

	/** Tries to turn given instance of type {@link #sourceType()} into an instance of type {@link #targetType()}. */
	Maybe<T> morph(DenotationTransformationContext context, S denotation);

	@Override
	default String describeYourself() {
		return "[" + name() + "]: " + sourceType().getTypeSignature() + " ==> " + targetType().getTypeSignature() + "";
	}

	@FunctionalInterface
	public interface MorphFunction<S extends GenericEntity, T extends GenericEntity> {
		Maybe<T> morph(DenotationTransformationContext context, S denotation);
	}

	public static <S extends GenericEntity, T extends GenericEntity> DenotationMorpher<S, T> create(String name, EntityType<S> sourceType,
			EntityType<T> targetType, MorphFunction<S, T> morphFunction) {

		nonNull(name, "name");
		nonNull(sourceType, "sourceType");
		nonNull(targetType, "targetType");
		nonNull(morphFunction, "morphFunction");

		return new DenotationMorpher<S, T>() {

			// @formatter:off
			@Override public String name() { return name; }
			@Override public EntityType<S> sourceType() { return sourceType; }
			@Override public EntityType<T> targetType() { return targetType; }
			// @formatter:on

			@Override
			public Maybe<T> morph(DenotationTransformationContext context, S denotation) {
				return morphFunction.morph(context, denotation);
			}

		};
	}

}
