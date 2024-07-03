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
package com.braintribe.model.deployment;

import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface Module extends HasName {

	EntityType<Module> T = EntityTypes.T(Module.class);

	String PLATFORM_MODULE_GLOBAL_ID = "module://platform";

	static String moduleGlobalId(String groupId, String artifactId) {
		return moduleGlobalId(groupId + ":" + artifactId);
	}

	static String moduleGlobalId(String moduleName) {
		return "module://" + moduleName;
	}

	String container = "container";
	String bindsWireContracts = "bindsWireContracts";
	String bindsHardwired = "bindsHardwired";
	String bindsInitializers = "bindsInitializers";
	String bindsDeployables = "bindsDeployables";

	/**
	 * The name is the artifactId of given module.
	 * <p>
	 * For the technical name containing groupId use {@link #moduleName()}.
	 */
	@Override
	java.lang.String getName();

	String getGroupId();
	void setGroupId(String groupId);

	boolean getBindsWireContracts();
	void setBindsWireContracts(boolean bindsWireContracts);

	boolean getBindsHardwired();
	void setBindsHardwired(boolean bindsHardwired);

	boolean getBindsInitializers();
	void setBindsInitializers(boolean bindsInitializers);

	boolean getBindsDeployables();
	void setBindsDeployables(boolean bindsDeployables);

	default boolean isPlatformModule() {
		return PLATFORM_MODULE_GLOBAL_ID.equals(getGlobalId());
	}

	/** Module name in the format "${groupId}:${artifactId}" */
	default String moduleName() {
		return getGroupId() + ":" + getName();
	}
}
