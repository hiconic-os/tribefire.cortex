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
package com.braintribe.model.tomcat.platform;

import java.util.Set;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Description("Represents tomcat's standard realm.")
public interface UserDatabaseRealm extends TomcatAuthenticationRealm {
	EntityType<UserDatabaseRealm> T = EntityTypes.T(UserDatabaseRealm.class);
	
	@Initializer("{'tomcat','manager-gui','manager-script'}")
	Set<String> getRoles();
	void setRoles(Set<String> roles);
	
	Set<TomcatUser> getUsers();
	void setUsers(Set<TomcatUser> users);
}