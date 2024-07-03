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
package tribefire.platform.impl.denotrans;

import static tribefire.platform.wire.space.security.services.UserSessionServiceSpace.CLEANUP_USER_SESSIONS_PROCESSOR_ID;
import static tribefire.platform.wire.space.security.services.UserSessionServiceSpace.CLEANUP_USER_SESSIONS_PROCESSOR_NAME;

import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InvalidArgument;

import tribefire.cortex.model.deployment.usersession.cleanup.CleanupUserSessionsProcessor;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.SimpleDenotationEnricher;

/**
 * @author peter.gazdik
 */
public class CleanupUserSessionsProcessorEnricher extends SimpleDenotationEnricher<CleanupUserSessionsProcessor> {

	public CleanupUserSessionsProcessorEnricher() {
		super(CleanupUserSessionsProcessor.T);
	}

	@Override
	public DenotationEnrichmentResult<CleanupUserSessionsProcessor> enrich(DenotationTransformationContext context,
			CleanupUserSessionsProcessor denotation) {

		if (!CLEANUP_USER_SESSIONS_PROCESSOR_ID.equals(context.denotationId()))
			return DenotationEnrichmentResult.error( //
					Reasons.build(InvalidArgument.T).text("Unexpected denotationId for AccessCleanupUserSessionsProcessor. Expected: ["
							+ CLEANUP_USER_SESSIONS_PROCESSOR_ID + "], actual: [" + context.denotationId() + "]. Instance: " + denotation).toReason());

		fill(denotation);

		return DenotationEnrichmentResult.allDone(denotation,
				"Configured externalId to [" + CLEANUP_USER_SESSIONS_PROCESSOR_ID + "] and name to [" + CLEANUP_USER_SESSIONS_PROCESSOR_NAME + "]");
	}

	public static void fill(CleanupUserSessionsProcessor denotation) {
		denotation.setGlobalId(CLEANUP_USER_SESSIONS_PROCESSOR_ID);
		denotation.setExternalId(CLEANUP_USER_SESSIONS_PROCESSOR_ID);
		denotation.setName(CLEANUP_USER_SESSIONS_PROCESSOR_NAME);
	}

}
