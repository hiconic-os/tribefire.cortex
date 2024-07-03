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

import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.api.ResourceHandle;
import tribefire.module.api.ResourceMetaDataResolver;

/**
 * @author peter.gazdik
 */
public interface ModuleResourcesContract extends WireSpace {

	/**
	 * Looks up a resource inside the module's resources folder. Note that public resources (those inside <tt>Public</tt> folder can be accessed with
	 * {@link PlatformResourcesContract#publicResources(String)}).
	 * <p>
	 * Examples with files brought by a module and how to resolve them:
	 * <table>
	 * <tr>
	 * <td>resources/top-level.txt</td>
	 * <td>{@code this.resource("top-level.txt")}</td>
	 * </tr>
	 * <tr>
	 * <td>resources/sub-folder/file.txt</td>
	 * <td>{@code this.resource("sub-folder/file.txt")}</td>
	 * </tr>
	 * <tr>
	 * <td>resources/Public/public.txt</td>
	 * <td>{@code publicResourcesContract.publicResources("public.txt")}</td>
	 * </tr>
	 * </table>
	 */
	ResourceHandle resource(String path);

	/**
	 * Looks up the resource on module's classpath. Note that this classpath also covers the platform classpath with all the jars promoted from other
	 * modules. Use classpath resources at your own discretion.
	 */
	ResourceHandle classpathResource(String path);

	/**
	 * Returns a module-specific {@link ResourceMetaDataResolver}.
	 */
	ResourceMetaDataResolver resourceMetaDataResolver();
}
