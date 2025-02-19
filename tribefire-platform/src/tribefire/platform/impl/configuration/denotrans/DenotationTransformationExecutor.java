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

import static com.braintribe.utils.lcd.CollectionTools2.isEmpty;
import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static com.braintribe.utils.lcd.NullSafe.nonNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.cfg.Required;
import com.braintribe.exception.Exceptions;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.module.api.DenotationMorpher;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.DenotationTransformer;
import tribefire.module.api.DenotationTransformerRegistry;

/**
 * Transforms given denotation into a targtType, using information from {@link DenotationTransformerRegistry} internally.
 * 
 * @see DenotationTransformer
 * 
 * @author peter.gazdik
 */
public class DenotationTransformationExecutor {

	private DenotationTransformerRegistryImpl transformerRegistry;

	private static final Logger log = Logger.getLogger(DenotationTransformationExecutor.class);

	@Required
	public void setTransformerRegistry(DenotationTransformerRegistryImpl transformerRegistry) {
		this.transformerRegistry = transformerRegistry;
	}

	public <T extends GenericEntity> Maybe<T> transform(String denotationId, ManagedGmSession session, GenericEntity entity,
			EntityType<T> targetType) {

		nonNull(denotationId, "denotationId");
		nonNull(session, "session");
		nonNull(entity, "entity");
		nonNull(targetType, "targetType");

		return transform(new SessionBasedDenotationTransformationContext(denotationId, session), entity, targetType);
	}

	private <T extends GenericEntity> Maybe<T> transform(DenotationTransformationContext context, GenericEntity entity, EntityType<T> targetType) {
		return new TransformationExecution(context, targetType).run(entity);
	}

	private class TransformationExecution {

		private final EntityType<?> targetType;
		private final DenotationTransformationContext context;

		private Maybe<List<DtStep>> maybeSteps;
		private List<DtStep> steps;

		private final List<GenericEntity> entityEvolution = newList();

		public TransformationExecution(DenotationTransformationContext context, EntityType<?> targetType) {
			this.context = context;
			this.targetType = targetType;
		}

		public <T extends GenericEntity> Maybe<T> run(GenericEntity entity) {
			try {
				Maybe<T> result = tryRun(entity);

				if (!result.isSatisfied())
					return Reason.create(writeFailedProtocol(), result.<Reason> whyUnsatisfied()).asMaybe();

				logProtocol();

				return result;

			} catch (Exception e) {
				throw Exceptions.unchecked(e, writeFailedProtocol());
			}
		}

		private void logProtocol() {
			log.debug("Denotation transformation succeeded:" + writeProtocol());
		}

		private <T extends GenericEntity> Maybe<T> tryRun(GenericEntity entity) {
			entityEvolution.add(entity);

			maybeSteps = transformerRegistry.resolveTransformationPipeline(entity.entityType(), targetType);
			if (maybeSteps.isUnsatisfied())
				return maybeSteps.cast();

			steps = maybeSteps.get();
			if (steps.isEmpty())
				return Maybe.complete((T) entity);

			Maybe<GenericEntity> maybeTransformed = null;
			for (DtStep dtStep : steps) {
				maybeTransformed = dtStep.apply(context, entity);
				if (maybeTransformed.isEmpty())
					return maybeTransformed.cast();

				entity = maybeTransformed.get();

				if (dtStep instanceof DtMetamorphosisStep)
					entityEvolution.add(entity);
			}

			return maybeTransformed.cast();
		}

		private String writeFailedProtocol() {
			return "DENOTATION TRANFORMATION FAILED: " + writeProtocol();
		}

		private String writeProtocol() {
			Iterator<GenericEntity> entityIt = entityEvolution.iterator();

			StringBuilder sb = new StringBuilder();
			sb.append("\n    DenotationId:");
			sb.append(context.denotationId());
			sb.append("\n    Input:\n        ");
			sb.append(entityIt.next());
			sb.append("\n    Target Type:\n        ");
			sb.append(targetType.getTypeSignature());

			if (isEmpty(steps))
				return sb.toString();

			List<DenotationMorpher<?, ?>> morphers = steps.stream() //
					.filter(t -> t instanceof DtMetamorphosisStep) //
					.map(m -> ((DtMetamorphosisStep) m).morpher) //
					.collect(Collectors.toList());

			if (morphers.isEmpty())
				return sb.toString();

			sb.append("\n    Applied Morphers:");
			for (DenotationMorpher<?, ?> m : morphers) {
				if (!entityIt.hasNext())
					sb.append("\n    Remaining Morphers:");

				sb.append("\n        ");
				sb.append(m.name());

				if (entityIt.hasNext()) {
					sb.append(" => ");
					sb.append(entityIt.next().entityType().getTypeSignature());
				}
			}

			return sb.toString();
		}

	}

	private static class SessionBasedDenotationTransformationContext implements DenotationTransformationContext {

		private final String denotationId;
		private final ManagedGmSession session;

		public SessionBasedDenotationTransformationContext(String denotationId, ManagedGmSession session) {
			this.denotationId = denotationId;
			this.session = session;
		}
		// @formatter:off
		@Override public String denotationId() { return denotationId; }
		@Override public <T extends GenericEntity> T create(EntityType<T> entityType) { return session.create(entityType); }
		@Override public <T extends GenericEntity> T createRaw(EntityType<T> entityType) { return session.createRaw(entityType); }
		@Override public <T extends GenericEntity> T findEntityByGlobalId(String globalId) { return session.findEntityByGlobalId(globalId); }
		// @formatter:on
	}

}
