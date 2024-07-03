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
package com.braintribe.model.processing.deployment.utils;

import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;


/**
 * Provides an instance of {@link GmMetaModel} with given name from given {@link PersistenceGmSession}.
 */
public class QueryingModelProvider implements Supplier<GmMetaModel> {
	private static final Logger logger = Logger.getLogger(QueryingModelProvider.class);
	
	private Supplier<PersistenceGmSession> sessionProvider;
	private String modelName;
	private GmMetaModel metaModel;
	
	private boolean ensureModelTypes;
	private boolean cacheModel = true;
	
	@Configurable @Required
	public void setSessionProvider(Supplier<PersistenceGmSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Configurable @Required
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	@Configurable
	public void setEnsureModelTypes(boolean ensureModelTypes) {
		this.ensureModelTypes = ensureModelTypes;
	}

	public void setCacheModel(boolean cacheModel) {
		this.cacheModel = cacheModel;
	}
	
	@Override
	public GmMetaModel get() throws RuntimeException {
		if (!cacheModel || metaModel == null) {
			metaModel = getMetaModel();
		}

		return metaModel;
	}

	private GmMetaModel getMetaModel() throws RuntimeException {
		try {
			EntityQuery eq = EntityQueryBuilder.from(GmMetaModel.class).where().property("name").eq(modelName).tc().negation().joker().limit(1).done();
			GmMetaModel model = sessionProvider.get().query().entities(eq).first();
			
			if (model == null) {
				logger.debug("No model found in session for name: "+modelName);
			}
			if (ensureModelTypes) {
				GMF.getTypeReflection().deploy(model);
				logger.debug("Successfully ensured types from model: "+model);
			}
			return model;
			

		} catch (Exception e) {
			throw new RuntimeException("Error while querying GmMetaModel with name: " + modelName, e);
		}
	}
}
