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
package tribefire.extension.js.model.deployment;

import java.util.List;

import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * The UX module denotation type is used to be linked to a ux component (probably JsUxComponent in the GME world and HxComponent in the Hydrux world).
 * Its property <code>path</code> points to the corresponding <code>index.js</code> file. The path is automatically generated during setup time.
 * 
 * <p>
 * Typical globalId has the following structure: "js-ux-module://${groupId}:${artifactId}".
 * <p>
 * Typical name is "${artifactId}-${version}".
 * <p>
 * Typical path is "js-libraries/${groupId}.${artifactid}-${version}/index.js".
 */
public interface UxModule extends HasName {

	EntityType<UxModule> T = EntityTypes.T(UxModule.class);

	String GLOBAL_ID_PREFIX = "js-ux-module://";

	/** Relative path to index.js file of the artifact denoted by this instance. */
	String getPath();
	void setPath(String path);

	/**
	 * Direct module dependencies of this module, i.e. such dependencies from it's pom.xml which are JsUxModule assets.
	 * <p>
	 * These are guaranteed to be loaded before this module is loaded.
	 */
	List<UxModule> getDependencies();
	void setDependencies(List<UxModule> dependencies);

}
