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
package tribefire.platform.impl.configuration.denotrans;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.junit.Before;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

import tribefire.module.api.DenotationEnricher;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationMorpher;
import tribefire.module.api.DenotationTransformationContext;

/**
 * @author peter.gazdik
 */
public class AbstractDenotransTest {

	protected DenotationTransformerRegistryImpl transformerRegistry;

	@Before
	public void setupDenoTrans() {
		transformerRegistry = new DenotationTransformerRegistryImpl();
	}

	protected void registerDummy(EntityType<?> source, EntityType<?> target) {
		registerMorpher(dummyMorpher(source, target));
	}

	protected void registerMorpher(DenotationMorpher<?, ?> morpher) {
		transformerRegistry.registerMorpher(morpher);
	}

	protected void registerEnricher(DenotationEnricher<?> enricher) {
		transformerRegistry.registerEnricher(enricher);
	}

	protected static <S extends GenericEntity> DenotationEnricher<S> directEnricher( //
			String name, EntityType<S> type, BiConsumer<DenotationTransformationContext, ? super S> c) {

		return DenotationEnricher.create(name, type, (ctx, e) -> {
			c.accept(ctx, e);

			return DenotationEnrichmentResult.allDone(e, null);
		});
	}

	protected static <S extends GenericEntity, T extends GenericEntity> DenotationMorpher<S, T> directMorpher( //
			EntityType<S> sourceType, EntityType<T> targetType, BiFunction<DenotationTransformationContext, S, T> f) {

		return DenotationMorpher.create(sourceType.getShortName() + "_TO_" + targetType.getShortName(), sourceType, targetType,
				(ctx, e) -> Maybe.complete(f.apply(ctx, e)));
	}
	protected static <S extends GenericEntity, T extends GenericEntity> DenotationMorpher<S, T> dummyMorpher( //
			EntityType<S> sourceType, EntityType<T> targetType) {

		return DenotationMorpher.create(sourceType.getShortName() + "_TO_" + targetType.getShortName(), sourceType, targetType,
				(ctx, e) -> Maybe.complete(targetType.create()));
	}

}
