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

import static tribefire.module.api.DenotationEnrichmentResult.allDone;
import static tribefire.module.api.PlatformBindIds.isPlatformBindId;

import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;

import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.EnvironmentDenotations;
import tribefire.module.api.SimpleDenotationEnricher;
import tribefire.platform.impl.initializer.Edr2ccPostInitializer;

/**
 * Ensures known platform-relevant {@link Deployable}s from {@link EnvironmentDenotations} are auto-deployed.
 * 
 * @see Edr2ccPostInitializer
 * 
 * @author peter.gazdik
 */
public class SystemDeployablesAutoDeployEnsuringEnricher extends SimpleDenotationEnricher<Deployable> {

	private static final Logger log = Logger.getLogger(SystemDeployablesAutoDeployEnsuringEnricher.class);

	public SystemDeployablesAutoDeployEnsuringEnricher() {
		super(Deployable.T);
	}

	@Override
	public DenotationEnrichmentResult<Deployable> enrich(DenotationTransformationContext context, Deployable deployable) {
		if (deployable.getAutoDeploy() || !isPlatformBindId(context.denotationId()))
			return DenotationEnrichmentResult.nothingNowOrEver();

		log.info("Edr2cc: Setting auto-deploy to true for: " + deployable);
		deployable.setAutoDeploy(true);

		return allDone(deployable, "Set autoDeploy=true for " + deployable.entityType().getShortName() + "[" + deployable.getExternalId() + "]");
	}

}
