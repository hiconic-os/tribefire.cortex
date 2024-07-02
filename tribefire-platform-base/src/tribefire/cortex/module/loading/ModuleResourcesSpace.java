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
package tribefire.cortex.module.loading;

import java.io.File;
import java.nio.file.Path;

import tribefire.descriptor.model.ModuleDescriptor;
import tribefire.module.api.ResourceHandle;
import tribefire.module.api.ResourceMetaDataResolver;
import tribefire.module.wire.contract.ModuleResourcesContract;
import tribefire.platform.base.ResourceMetaDataResolverImpl;
import tribefire.platform.base.ResourcesBuilding;

/**
 * @author peter.gazdik
 */
public class ModuleResourcesSpace implements ModuleResourcesContract {

	private final Path moduleResourcesPath;
	private final ClassLoader moduleClassLoader;
	private final ResourceMetaDataResolver resourceMetaDataResolver;

	public ModuleResourcesSpace(ModuleDescriptor moduleDescriptor, File moduleBaseFolder, ClassLoader moduleClassLoader) {
		this.moduleResourcesPath = ModuleLoaderHelper.resolveResourcesPath(moduleDescriptor.getArtifactId(), moduleBaseFolder);
		this.moduleClassLoader = moduleClassLoader;
		this.resourceMetaDataResolver = new ResourceMetaDataResolverImpl(this, moduleDescriptor.name());
	}

	@Override
	public ResourceHandle resource(String path) {
		return ResourcesBuilding.PathResourcesBuilder.create(moduleResourcesPath, path);
	}

	@Override
	public ResourceHandle classpathResource(String path) {
		return ResourcesBuilding.ClasspathResourcesBuilder.create(path, moduleClassLoader);
	}

	@Override
	public ResourceMetaDataResolver resourceMetaDataResolver() {
		return resourceMetaDataResolver;
	}

}
