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

import static tribefire.platform.wire.space.security.services.UserSessionServiceSpace.USER_SESSION_SERVICE_ID;
import static tribefire.platform.wire.space.security.services.UserSessionServiceSpace.USER_SESSION_SERVICE_NAME;

import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InvalidArgument;

import tribefire.cortex.model.deployment.usersession.service.UserSessionService;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.SimpleDenotationEnricher;
import tribefire.platform.wire.space.security.services.UserSessionServiceSpace;

/**
 * @author peter.gazdik
 */
public class UserSessionServiceEnricher extends SimpleDenotationEnricher<UserSessionService> {

	public UserSessionServiceEnricher() {
		super(UserSessionService.T);
	}

	@Override
	public DenotationEnrichmentResult<UserSessionService> enrich(DenotationTransformationContext context, UserSessionService denotation) {
		if (!USER_SESSION_SERVICE_ID.equals(context.denotationId()))
			return DenotationEnrichmentResult.error( //
					Reasons.build(InvalidArgument.T).text("Unexpected denotationId for AccessUserSessionService. Expected: ["
							+ USER_SESSION_SERVICE_ID + "], actual: [" + context.denotationId() + "]. Instance: " + denotation).toReason());

		fill(denotation);

		return DenotationEnrichmentResult.allDone(denotation,
				"Configured externalId to [" + UserSessionServiceSpace.USER_SESSION_SERVICE_ID + "] and name to [" + USER_SESSION_SERVICE_NAME + "]");
	}

	public static void fill(UserSessionService denotation) {
		denotation.setGlobalId(USER_SESSION_SERVICE_ID);
		denotation.setExternalId(USER_SESSION_SERVICE_ID);
		denotation.setName(USER_SESSION_SERVICE_NAME);
	}

}
