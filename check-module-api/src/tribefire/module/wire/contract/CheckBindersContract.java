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

import com.braintribe.model.accessapi.AccessRequest;
import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.extensiondeployment.check.ParameterizedCheckProcessor;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.wire.api.space.WireSpace;

/**
 * @author peter.gazdik
 */
public interface CheckBindersContract extends WireSpace {

	ComponentBinder<CheckProcessor, com.braintribe.model.processing.check.api.CheckProcessor> checkProcessor();

	ComponentBinder<ParameterizedCheckProcessor, com.braintribe.model.processing.check.api.ParameterizedCheckProcessor<?>> parameterizedCheckProcessor();

	ComponentBinder<ParameterizedCheckProcessor, com.braintribe.model.processing.check.api.ParameterizedAccessCheckProcessor<? extends AccessRequest>> parameterizedAccessCheckProcessor();

}
