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
package com.braintribe.model.extensiondeployment;

import java.util.Map;

import com.braintribe.model.generic.annotation.meta.DeployableComponent;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@DeployableComponent
public interface WebTerminal extends Terminal {

	EntityType<WebTerminal> T = EntityTypes.T(WebTerminal.class);

	@Mandatory
	String getPathIdentifier();
	void setPathIdentifier(String pathIdentifier);

	/**
	 * @deprecated too generic - model your own generic or expressive properties in your own derivation
	 */
	@Deprecated
	Map<String, String> getProperties();
	@Deprecated
	void setProperties(Map<String, String> value);

}
