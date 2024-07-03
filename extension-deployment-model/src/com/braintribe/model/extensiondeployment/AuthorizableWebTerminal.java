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

import java.util.Set;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Description("Denotes a web terminal that is optionally authorized.")
public interface AuthorizableWebTerminal extends WebTerminal {

	EntityType<AuthorizableWebTerminal> T = EntityTypes.T(AuthorizableWebTerminal.class);
	
	String roles = "roles";
	
	@Description("Configures the roles that are allowed to access the web terminal. If empty any role is accepted.")
	Set<String> getRoles();
	void setRoles(Set<String> roles);
}
