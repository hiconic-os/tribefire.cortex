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
package tribefire.module.wire.contract;

import java.util.function.Supplier;

import com.braintribe.model.extensiondeployment.HardwiredStateChangeProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessor;

/**
 * Offers methods for binding {@link StateChangeProcessor}s.
 * 
 * @see HardwiredDeployablesContract
 */
public interface HardwiredStateProcessorsContract extends HardwiredDeployablesContract {

	default HardwiredStateChangeProcessor bindStateChangeProcessor(String externalId, String name, StateChangeProcessor<?, ?> stateChangeProcessor) {
		return bindStateChangeProcessor(externalId, name, () -> stateChangeProcessor);
	}

	/** ExternalId convention: hardwired:state-change-processor/${description} (e.g. hardwired:state-change-processor/update-checker) */
	HardwiredStateChangeProcessor bindStateChangeProcessor(String externalId, String name, Supplier<StateChangeProcessor<?, ?>> stateChangeProcessorSuplier);

}
