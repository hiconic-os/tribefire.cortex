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

import com.braintribe.model.extensiondeployment.check.HardwiredCheckProcessor;
import com.braintribe.model.extensiondeployment.check.HardwiredParameterizedCheckProcessor;
import com.braintribe.model.processing.check.api.CheckProcessor;
import com.braintribe.model.processing.check.api.ParameterizedCheckProcessor;

/**
 * Offers methods for binding {@link CheckProcessor}s and {@link ParameterizedCheckProcessor}.
 * 
 * @see HardwiredDeployablesContract
 */
public interface HardwiredCheckProcessorsContract extends HardwiredDeployablesContract {

	default HardwiredCheckProcessor bindCheckProcessor(String externalId, String name, CheckProcessor checkProcessor) {
		return bindCheckProcessor(externalId, name, () -> checkProcessor);
	}

	HardwiredCheckProcessor bindCheckProcessor(String externalId, String name, Supplier<CheckProcessor> checkProcessorSupplier);

	default HardwiredParameterizedCheckProcessor bindParameterizedCheckProcessor( //
			String externalId, String name, ParameterizedCheckProcessor<?> parameterizedCheckProcessor) {
		return bindParameterizedCheckProcessor(externalId, name, () -> parameterizedCheckProcessor);
	}

	HardwiredParameterizedCheckProcessor bindParameterizedCheckProcessor( //
			String externalId, String name, Supplier<ParameterizedCheckProcessor<?>> parameterizedCheckProcessorSupplier);

}
