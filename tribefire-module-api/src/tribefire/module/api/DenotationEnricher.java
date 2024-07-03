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
 * {@link DenotationTransformer} which modifies an instance.
 * 
 * @see DenotationTransformer
 * @see DenotationMorpher
 * 
 * @author peter.gazdik
 */
public interface DenotationEnricher<E extends GenericEntity> extends DenotationTransformer<E, E> {

	/**
	 * Tries to enrich given entity and returns a non-null {@link DenotationEnrichmentResult}, which indicates how/if the entity was modified and
	 * whether the enricher should be called again.
	 */
	DenotationEnrichmentResult<E> enrich(DenotationTransformationContext context, E denotation);

	@Override
	default EntityType<E> targetType() {
		return sourceType();
	}

	@Override
	default String describeYourself() {
		return "[" + name() + "]: " + sourceType().getTypeSignature();
	}

	@FunctionalInterface
	public interface EnrichFunction<E extends GenericEntity> {
		DenotationEnrichmentResult<E> enrich(DenotationTransformationContext context, E denotation);
	}

	public static <E extends GenericEntity> DenotationEnricher<E> create(String name, EntityType<E> sourceType,
			EnrichFunction<E> enrichmentFunction) {
		nonNull(name, "name");
		nonNull(sourceType, "sourceType");
		nonNull(enrichmentFunction, "enrichmentFunction");

		return new DenotationEnricher<E>() {

			// @formatter:off
			@Override public String name() { return name; }
			@Override public EntityType<E> sourceType() { return sourceType; }
			// @formatter:on

			@Override
			public DenotationEnrichmentResult<E> enrich(DenotationTransformationContext context, E denotation) {
				return enrichmentFunction.enrich(context, denotation);
			}

		};
	}

}
