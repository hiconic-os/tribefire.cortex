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
package tribefire.platform.impl.setup;

import java.time.Instant;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.platformsetup.PlatformSetup;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.date.NanoClock;

public class PlatformSetupSupplier implements Supplier<PlatformSetup> {

	private static final Logger logger = Logger.getLogger(PlatformSetupSupplier.class);

	private Supplier<PersistenceGmSession> setupSessionFactory;

	protected boolean initialized = false;
	protected PlatformSetup platformSetup = null;


	protected static TraversingCriterion allTc = TC.create()
			.negation().joker()
			.done();	

	@Override
	public PlatformSetup get() {

		if (initialized) {
			return platformSetup;
		}
		initialized = true;

		logger.debug(() -> "Loading the PlatformSetup");
		Instant start = NanoClock.INSTANCE.instant();

		try {
			PersistenceGmSession session = setupSessionFactory.get();

			EntityQuery query = EntityQueryBuilder.from(PlatformSetup.T).tc(allTc).done();
			platformSetup = session.query().entities(query).first();
		} catch(Exception e) {
			logger.warn(() -> "Error while trying to get the PlatformSetup entity.", e);
		} finally {
			logger.debug(() -> "Loading the PlatformSetup took "+StringTools.prettyPrintDuration(start, true, null)+". Result available: "+(platformSetup != null));
		}

		return platformSetup;
	}

	@Required
	@Configurable
	public void setSetupSessionFactory(Supplier<PersistenceGmSession> setupSessionFactory) {
		this.setupSessionFactory = setupSessionFactory;
	}
}
