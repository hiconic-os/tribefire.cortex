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

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Description("Denotes a tomcat realm that uses tribefire authentication as delegate for tomcat authentication.")
public interface TfRestRealm extends TomcatAuthenticationRealm {
	EntityType<TfRestRealm> T = EntityTypes.T(TfRestRealm.class);
	
	@Description("The tribefire-services url of the tribefire to which the authentications of this realm should be delegated.")
	String getTfsUrl();
	void setTfsUrl(String tfsUrl);
	
	@Description("The name of the role in the tribefire delegate that represents the tomcat roles: manager-gui, tomcat, manager-script.")
	@Initializer("'tf-admin'")
	String getFullAccessAlias();
	void setFullAccessAlias(String fullAccessAlias);
}
