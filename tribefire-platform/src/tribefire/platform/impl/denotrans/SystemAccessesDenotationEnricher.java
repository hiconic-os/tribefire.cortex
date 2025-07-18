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

import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_AUTH;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_AUTH_NAME;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_MODEL_AUTH;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_MODEL_TRANSIENT_MESSAGING_DATA;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_MODEL_USER_SESSIONS;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_MODEL_USER_STATISTICS;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_TRANSIENT_MESSAGING_DATA;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_TRANSIENT_MESSAGING_DATA_NAME;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_USER_SESSIONS;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_USER_SESSIONS_NAME;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_USER_STATISTICS;
import static com.braintribe.model.processing.tfconstants.TribefireConstants.ACCESS_USER_STATISTICS_NAME;
import static tribefire.module.api.DenotationEnrichmentResult.allDone;

import com.braintribe.common.artifact.ArtifactReflection;
import com.braintribe.logging.Logger;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.utils.lcd.StringTools;

import tribefire.cortex._TribefirePlatform_;
import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.PlatformBindIds;
import tribefire.module.api.SimpleDenotationEnricher;
import tribefire.platform.impl.initializer.Edr2ccPostInitializer;

/**
 * Sets name, globalId, externalId and model for known system accesses.
 * 
 * @see Edr2ccPostInitializer
 * 
 * @author peter.gazdik
 */
public class SystemAccessesDenotationEnricher extends SimpleDenotationEnricher<IncrementalAccess> {

	private static final Logger log = Logger.getLogger(SystemAccessesDenotationEnricher.class);

	public SystemAccessesDenotationEnricher() {
		super(IncrementalAccess.T);
	}

	@Override
	public DenotationEnrichmentResult<IncrementalAccess> enrich(DenotationTransformationContext context, IncrementalAccess access) {
		if (access.getExternalId() == null)
			return new SystemAccessesDenotationEnriching(context, access).run();
		else
			return DenotationEnrichmentResult.nothingNowOrEver();

	}

	private static class SystemAccessesDenotationEnriching {

		private final DenotationTransformationContext context;
		private final IncrementalAccess access;

		public SystemAccessesDenotationEnriching(DenotationTransformationContext context, IncrementalAccess access) {
			this.context = context;
			this.access = access;
		}

		public DenotationEnrichmentResult<IncrementalAccess> run() {
			switch (context.denotationId()) {
				case PlatformBindIds.AUTH_DB_BIND_ID:
					return enrich(ACCESS_AUTH, ACCESS_MODEL_AUTH, ACCESS_AUTH_NAME);

				case PlatformBindIds.USER_SESSIONS_DB_BIND_ID:
					return enrich(ACCESS_USER_SESSIONS, ACCESS_MODEL_USER_SESSIONS, ACCESS_USER_SESSIONS_NAME);

				case PlatformBindIds.USER_STATISTICS_DB_BIND_ID:
					return enrich(ACCESS_USER_STATISTICS, ACCESS_MODEL_USER_STATISTICS, ACCESS_USER_STATISTICS_NAME);

				case PlatformBindIds.TRANSIENT_MESSAGING_DATA_DB_BIND_ID:
					return enrich(ACCESS_TRANSIENT_MESSAGING_DATA, ACCESS_MODEL_TRANSIENT_MESSAGING_DATA, ACCESS_TRANSIENT_MESSAGING_DATA_NAME);

				default:
					return DenotationEnrichmentResult.nothingNowOrEver();
			}
		}

		private DenotationEnrichmentResult<IncrementalAccess> enrich(String externalId, String rawModelName, String name) {
			log.info("Edr2cc: Enriching " + name + " Access with externalId: " + externalId + ", rawModel: " + rawModelName);

			access.setGlobalId("edr2cc:access:" + externalId);
			access.setName(name);
			access.setExternalId(externalId);
			access.setMetaModel(rawConfiguredModel(rawModelName));

			return allDone(access, "Configured externalId to [" + externalId + "] and configured model based on raw model [" + rawModelName + "]");
		}

		private GmMetaModel rawConfiguredModel(String rawModelName) {
			ArtifactReflection currentArtifact = _TribefirePlatform_.reflection;

			String name = currentArtifact.groupId() + ":configuration-" + simpleModelName(rawModelName);

			GmMetaModel configuredModel = context.create(GmMetaModel.T);
			configuredModel.setGlobalId(Model.modelGlobalId(name));
			configuredModel.setName(name);
			configuredModel.setVersion(currentArtifact.version());
			configuredModel.getDependencies().add(findModel(rawModelName));

			return configuredModel;
		}

		private GmMetaModel findModel(String rawModelName) {
			return context.getEntityByGlobalId(Model.modelGlobalId(rawModelName));
		}

		private String simpleModelName(String modelName) {
			return StringTools.findSuffix(modelName, ":");
		}

	}
}
