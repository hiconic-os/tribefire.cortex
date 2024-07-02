// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.platform.impl.configuration.denotrans;

import static com.braintribe.utils.lcd.CollectionTools2.newDeque;

import java.util.Deque;
import java.util.List;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;

import tribefire.module.api.DenotationEnricher;
import tribefire.module.api.DenotationMorpher;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.DenotationEnrichmentResult;

/**
 * @author peter.gazdik
 */
public abstract class DtStep {

	public abstract Maybe<GenericEntity> apply(DenotationTransformationContext context, GenericEntity instance);

}

/* package */ class DtEnrichmentStep extends DtStep {

	private final Deque<DenotationEnricher<?>> enrichersToCallAgain;
	private int enrichersReady;

	private DenotationEnricher<GenericEntity> currentEnricher;

	public DtEnrichmentStep(List<DenotationEnricher<?>> enrichers) {
		this.enrichersToCallAgain = newDeque(enrichers);
		markEnrichersReady();
	}

	@Override
	public Maybe<GenericEntity> apply(DenotationTransformationContext context, GenericEntity instance) {
		while (enrichersReady > 0) {
			popEnricher();

			DenotationEnrichmentResult<GenericEntity> result = currentEnricher.enrich(context, instance);

			Maybe<GenericEntity> maybeTransformed = result.result();
			if (maybeTransformed != null) {
				if (maybeTransformed.isUnsatisfied())
					return maybeTransformed;

				if (maybeTransformed.get() != instance)
					throw new IllegalStateException("Enricher " + currentEnricher.name() + " has switched the processed instance.\n    Original: "
							+ instance + "\n    Newe: " + maybeTransformed.get());
				
				markEnrichersReady();
			}

			if (result.callAgain())
				enqueueTransformer();

		}

		return Maybe.complete(instance);
	}

	private void popEnricher() {
		enrichersReady--;
		currentEnricher = (DenotationEnricher<GenericEntity>) enrichersToCallAgain.removeFirst();
	}

	private void enqueueTransformer() {
		enrichersToCallAgain.addLast(currentEnricher);
	}

	private void markEnrichersReady() {
		enrichersReady = enrichersToCallAgain.size();
	}

}

/* package */ class DtMetamorphosisStep extends DtStep {

	public final DenotationMorpher<GenericEntity, GenericEntity> morpher;

	public DtMetamorphosisStep(DenotationMorpher<?, ?> morpher) {
		this.morpher = (DenotationMorpher<GenericEntity, GenericEntity>) morpher;
	}

	@Override
	public Maybe<GenericEntity> apply(DenotationTransformationContext context, GenericEntity instance) {
		Maybe<GenericEntity> maybeTransformed = morpher.morph(context, instance);

		if (maybeTransformed.isUnsatisfied())
			return maybeTransformed;

		if (maybeTransformed.get() == null)
			throw new IllegalStateException("Null result returned from transformer: " + morpher.describeYourself());

		return maybeTransformed;
	}

}
