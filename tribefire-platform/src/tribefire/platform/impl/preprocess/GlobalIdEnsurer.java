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
package tribefire.platform.impl.preprocess;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.InitializationAware;
import com.braintribe.cfg.Required;
import com.braintribe.exception.Exceptions;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;

public class GlobalIdEnsurer implements InitializationAware {
	
	private static final Logger logger = Logger.getLogger(GlobalIdEnsurer.class);
	protected Supplier<PersistenceGmSession> sessionProvider;
	private boolean runOnStartup = false;
	
	public GlobalIdEnsurer() {
		
	}

	@Required @Configurable
	public void setSessionProvider(
			Supplier<PersistenceGmSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	
	@Configurable
	public void setRunOnStartup(boolean runOnStartup) {
		this.runOnStartup = runOnStartup;
	}
	
	@Override
	public void postConstruct() {
		if (runOnStartup) {
			try {
				ensureGlobalIds();
			} catch (Exception e) {
				throw Exceptions.unchecked(e, "Error while ensuring global Ids.");
			}
		}
	}
	
	public void ensureGlobalIds() throws Exception {
		
		if (logger.isDebugEnabled())
			logger.debug("Searching for unset Global Ids in cortex.");
		
		PersistenceGmSession cortexSession = sessionProvider.get();
		EntityQuery query = EntityQueryBuilder.from(GenericEntity.class).where().property(GenericEntity.globalId).eq(null).done();
		List<GenericEntity> result = cortexSession.query().entities(query).list();
		
		int size = result.size();
		
		if (size == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("All instances of HasGlobalId have a globalId set.");
			}
			return;
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Found: "+size+" instances of HasGlobalId that doesn't have a globalId set.");
		
		
		for (GenericEntity hasGlobalId : result) {
			String globalId = UUID.randomUUID().toString();
			hasGlobalId.setGlobalId(globalId);

			if (logger.isTraceEnabled())
				logger.trace("GlobalId value for instance: "+hasGlobalId+" set to: "+globalId);
			
		}

		if (cortexSession.getTransaction().hasManipulations()) {
			cortexSession.commit();
			if (logger.isDebugEnabled())
				logger.debug("Successfully ensured globalId values for: "+size+" instances.");
		}

	}
	
	
}
