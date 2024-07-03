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
package com.braintribe.model.processing.web.cors;

import java.util.Collections;
import java.util.List;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.cortex.deployment.cors.CorsConfiguration;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.sp.api.AfterStateChangeContext;
import com.braintribe.model.processing.sp.api.StateChangeProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessorException;
import com.braintribe.model.processing.sp.api.StateChangeProcessorMatch;
import com.braintribe.model.processing.sp.api.StateChangeProcessorRule;
import com.braintribe.model.processing.sp.api.StateChangeProcessorSelectorContext;
import com.braintribe.model.processing.sp.api.StateChangeProcessors;
import com.braintribe.model.stateprocessing.api.StateChangeProcessorCapabilities;
import com.braintribe.web.cors.handler.BasicCorsHandler;

public class CortexCorsStateChangeProcessor
		implements StateChangeProcessorRule, StateChangeProcessor<GenericEntity, GenericEntity>, StateChangeProcessorMatch {

	private BasicCorsHandler cortexCorsHandler;

	private String corsConfigPropertyName = "corsConfiguration";

	private static final Logger log = Logger.getLogger(CortexCorsStateChangeProcessor.class);

	@Required
	@Configurable
	public void setCortexCorsHandler(BasicCorsHandler cortexCorsHandler) {
		this.cortexCorsHandler = cortexCorsHandler;
	}

	@Override
	public String getProcessorId() {
		return this.getClass().getSimpleName();
	}

	@Override
	public StateChangeProcessor<? extends GenericEntity, ? extends GenericEntity> getStateChangeProcessor() {
		return this;
	}
	
	@Override
	public StateChangeProcessorCapabilities getCapabilities() {
		return StateChangeProcessors.afterOnlyCapabilities();
	}

	@Override
	public void onAfterStateChange(AfterStateChangeContext<GenericEntity> context, GenericEntity customContext) throws StateChangeProcessorException {

		if (log.isTraceEnabled()) {
			log.trace("CortexCorsStateChangeProcessor.onAfterStateChange() with context: " + context + " customContext: " + customContext);
		}

		CorsConfiguration corsConfiguration = null;
		EntityType<?> entityType = context.getEntityType();

		if (entityType == CorsConfiguration.T) {

			corsConfiguration = (CorsConfiguration) context.getProcessEntity();

			if (!isCurrentCorsConfiguration(context.getSession(), corsConfiguration)) {
				return;
			}
		} else if (entityType == CortexConfiguration.T) {

			corsConfiguration = ((CortexConfiguration)context.getProcessEntity()).getCorsConfiguration();

		}

		cortexCorsHandler.setConfiguration(corsConfiguration);

		if (log.isDebugEnabled()) {
			log.debug("State change processor updated cortex CORS handler with just updated CORS configuration " + corsConfiguration);
		}

	}

	@Override
	public List<StateChangeProcessorMatch> matches(StateChangeProcessorSelectorContext context) {

		if (context == null || context.getEntityType() == null) {
			return Collections.emptyList();
		}

		if (context.getEntityType() == CorsConfiguration.T) {
			return Collections.<StateChangeProcessorMatch> singletonList(this);
		}

		if (context.getEntityType() == CortexConfiguration.T && context.getProperty() != null
				&& corsConfigPropertyName.equals(context.getProperty().getName())) {
			return Collections.<StateChangeProcessorMatch> singletonList(this);
		}

		return Collections.emptyList();
	}

	private boolean isCurrentCorsConfiguration(PersistenceGmSession gmSession, CorsConfiguration corsConfiguration)
			throws StateChangeProcessorException {

		CortexConfiguration currentCortexConfig = null;
		try {
			currentCortexConfig = gmSession.query().entity(CortexConfiguration.T, "singleton").find();
		} catch (GmSessionException e) {
			throw new StateChangeProcessorException("Failed to query cortex configuration singleton: " + e.getMessage(), e);
		}

		if (currentCortexConfig == null) {
			throw new StateChangeProcessorException("Cortex configuration singleton not found");
		}

		return (currentCortexConfig.getCorsConfiguration() != null && currentCortexConfig.getCorsConfiguration().equals(corsConfiguration));

	}

	@Override
	public String getRuleId() {
		return getProcessorId();
	}

	@Override
	public StateChangeProcessor<? extends GenericEntity, ? extends GenericEntity> getStateChangeProcessor(String processorId) {

		return this;
	}

}
