// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.module.model.resource;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.source.ResourceSource;

/**
 * {@link ResourceSource} that denotes a private resource (file) of a module.
 * <p>
 * As a reminder, private resources are files inside the top level "public" directory within the module's resources directory..
 * 
 * Example:
 * 
 * For "my-module" in the group "my.group" :
 * <ul>
 * <li>my-module/
 * <ul>
 * <li>resources/
 * <ul>
 * <li>image.jpg
 * </ul>
 * </ul>
 * </ul>
 *
 * a resource "image.jpg" could be denoted by the following ModuleSource instance:
 * 
 * <pre>
 * ModuleSource source = ModuleSource.T.create();
 * source.setModuleName("my.group:my-module");
 * source.setPath("image.jpg");
 * </pre>
 */
public interface ModuleSource extends ResourceSource {

	EntityType<ModuleSource> T = EntityTypes.T(ModuleSource.class);

	/**
	 * Name of the file in resources folder that is auto-generated on build. This file contains the meta-data (mime-type, md5, specification) for
	 * given resource.
	 */
	String INDEX_FILE_NAME = "__resource-index__.yml";

	String path = "path";

	/** Name of the module which binds the initializer. Format: ${groupId}:${artifactId} */
	String getModuleName();
	void setModuleName(String moduleId);

	/** Relative path within the module's 'resources' folder. */
	String getPath();
	void setPath(String path);

}
