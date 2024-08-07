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
package com.braintribe.model.processing.sp.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.sp.api.ReflectiveStateChangeProcessorRule;
import com.braintribe.model.processing.sp.api.StateChangeProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessorMapping;
import com.braintribe.model.processing.sp.api.StateChangeProcessorMatch;
import com.braintribe.model.processing.sp.api.StateChangeProcessorSelectorContext;
import com.braintribe.model.processing.sp.commons.selector.ManipulationSelectorEvaluator;
import com.braintribe.model.stateprocessing.api.SelectiveStateChangeProcessorAddressing;

public class ConfigurableStateChangeProcessorRule implements ReflectiveStateChangeProcessorRule {
	private String ruleId;
	private List<StateChangeProcessorMapping> processorMappings;
	private Map<String, StateChangeProcessorMapping> processorMappingByProcessorId;
	
	@Configurable @Required
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@Configurable @Required
	public void setProcessorMappings( List<StateChangeProcessorMapping> processorMappings) {
		this.processorMappings = processorMappings;
		processorMappingByProcessorId = new HashMap<String, StateChangeProcessorMapping>();
		for (StateChangeProcessorMapping mapping : processorMappings) {
			processorMappingByProcessorId.put( mapping.getProcessorAddressing().getProcessorId(), mapping);
		}	
	}
	
	@Override
	public String getRuleId() {
		return ruleId;
	}

	@Override
	public StateChangeProcessor<? extends GenericEntity, ? extends GenericEntity> getStateChangeProcessor( String processorId) {
		@SuppressWarnings("unchecked")
		StateChangeProcessor<GenericEntity, GenericEntity> processor = (StateChangeProcessor<GenericEntity, GenericEntity>) processorMappingByProcessorId.get( processorId);
		return processor;
	}

	@Override
	public List<StateChangeProcessorMatch> matches(StateChangeProcessorSelectorContext context) {
		
			
		List<StateChangeProcessorMatch> matches = new ArrayList<StateChangeProcessorMatch>();
		for (StateChangeProcessorMapping mapping : processorMappings) {
			SelectiveStateChangeProcessorAddressing addressing = mapping.getProcessorAddressing();
			if (ManipulationSelectorEvaluator.matches(context, addressing.getManipulationSelector())) {
				String processorId = addressing.getProcessorId();
				@SuppressWarnings("unchecked")
				StateChangeProcessor<GenericEntity, GenericEntity> stateChangeProcessor = (StateChangeProcessor<GenericEntity, GenericEntity>) mapping.getProcessor();								
				StateChangeProcessorMatch match = new StateChangeProcessorMatchImpl( processorId, stateChangeProcessor);
				matches.add(match);
			}
		}
		
		return matches;
	}

	
	@Override
	public List<StateChangeProcessorMapping> getProcessorMappings() {	
		return processorMappings;
	}
}
