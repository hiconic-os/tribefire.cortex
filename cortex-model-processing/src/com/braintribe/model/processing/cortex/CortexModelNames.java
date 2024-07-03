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
package com.braintribe.model.processing.cortex;

import com.braintribe.gm._ServicePersistenceModel_;
import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.processing.cortex.priming.CortexModelsPersistenceInitializer;

import tribefire.cortex._TribefirePackagedCortexModel_;
import tribefire.cortex._TribefirePackagedCortexServiceModel_;
import tribefire.cortex._TribefireSyncModel_;
import tribefire.cortex._WorkbenchModel_;

/**
 * Names for cortex-related models.
 * <p>
 * All these models have standard globalId, i.e. it can be derived from the model name here via {@link Model#modelGlobalId(String)}.
 * 
 * @author peter.gazdik
 */
public interface CortexModelNames {

	/** Container for all classpath models that should be put into cortex as data. */
	String TF_SYNC_MODEL_NAME = _TribefireSyncModel_.reflection.name();
	/** Contains all classpath models that should be included in the cortex' model. This is a subset of {@link #TF_SYNC_MODEL_NAME sync model}. */
	String TF_PACKAGED_CORTEX_MODEL_NAME = _TribefirePackagedCortexModel_.reflection.name();
	/** Actual cortex' model. This model is generated dynamically by {@link CortexModelsPersistenceInitializer} */
	String TF_CORTEX_MODEL_NAME = "tribefire.cortex:tribefire-cortex-model";

	/** Contains all cp models that should be included in the cortex' service model. This is a subset of {@link #TF_SYNC_MODEL_NAME sync model}. */
	String TF_PACKAGED_CORTEX_SERVICE_MODEL_NAME = _TribefirePackagedCortexServiceModel_.reflection.name();
	/** Actual cortex' service model. This model is generated dynamically by {@link CortexModelsPersistenceInitializer} */
	String TF_CORTEX_SERVICE_MODEL_NAME = "tribefire.cortex:tribefire-cortex-service-model";

	/** Contains all cp models that should be included in the cortex workbench model. This is a subset of {@link #TF_SYNC_MODEL_NAME sync model}. */
	String TF_PACKAGED_CORTEX_WORKBENCH_MODEL_NAME = _WorkbenchModel_.reflection.name();
	/** Actual cortex' workbench model. This model is generated dynamically by {@link CortexModelsPersistenceInitializer} */
	String TF_CORTEX_WORKBENCH_MODEL_NAME = "tribefire.cortex:tribefire-cortex-workbench-model";

	String TF_SERVICE_PERSISTENCE_MODEL_NAME = _ServicePersistenceModel_.reflection.name();

}
