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
package tribefire.descriptor.model;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Wrapper for all the information about a module that is contained inside the module's jar file.
 * <p>
 * For now it only contains {@link #getWireModule() wire module} information, which is used for module loading.
 * <p>
 * The information is stored in a file called `packaging-info.yml`
 * 
 * @author peter.gazdik
 */
public interface ModulePackagingInfo extends GenericEntity {

	EntityType<ModulePackagingInfo> T = EntityTypes.T(ModulePackagingInfo.class);

	/** Name of the WireModule class of given tribefire module. */
	String getWireModule();
	void setWireModule(String wireModule);

}
