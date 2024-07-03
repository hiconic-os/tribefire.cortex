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
package com.braintribe.model.stateprocessing.api;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface OnBeforeStateChangeProcessingResponse extends AbstractStateChangeProcessingResponse {

	EntityType<OnBeforeStateChangeProcessingResponse> T = EntityTypes.T(OnBeforeStateChangeProcessingResponse.class);
	public GenericEntity getCustomContext();
	public void setCustomContext( GenericEntity customContext);
	
	void setCapabilitiesOverride(StateChangeProcessorCapabilities capabilities);
	StateChangeProcessorCapabilities getCapabilitiesOverride();

}
