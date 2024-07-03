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

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.sp.api.StateChangeProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessorMapping;
import com.braintribe.model.stateprocessing.api.SelectiveStateChangeProcessorAddressing;

public class ConfigurableStateChangeProcessorMapping implements StateChangeProcessorMapping {
	private StateChangeProcessor<?, ?> stateChangeProcessor;
	private SelectiveStateChangeProcessorAddressing stateChangeProcessorAddressing;
	
	@Configurable @Required
	public void setStateChangeProcessor( StateChangeProcessor<?, ?> stateChangeProcessor) {
		this.stateChangeProcessor = stateChangeProcessor;
	}
	
	@Configurable @Required
	public void setStateChangeProcessorAddressing( SelectiveStateChangeProcessorAddressing stateChangeProcessorAddressing) {
		this.stateChangeProcessorAddressing = stateChangeProcessorAddressing;
	}
	
	@Override
	public StateChangeProcessor<?, ?> getProcessor() {
		return stateChangeProcessor;
	}

	@Override
	public SelectiveStateChangeProcessorAddressing getProcessorAddressing() {
		return stateChangeProcessorAddressing;
	}

}
