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
package tribefire.cortex.module.loading;

import tribefire.descriptor.model.ModuleDescriptor;
import tribefire.module.wire.contract.ModuleReflectionContract;

/**
 * @author peter.gazdik
 */
public class ModuleReflectionSpace implements ModuleReflectionContract {

	private final ModuleDescriptor moduleDescriptor;
	private final ClassLoader moduleClassLoader;

	public ModuleReflectionSpace(ModuleDescriptor moduleDescriptor, ClassLoader moduleClassLoader) {
		this.moduleDescriptor = moduleDescriptor;
		this.moduleClassLoader = moduleClassLoader;
	}

	@Override
	public String artifactId() {
		return moduleDescriptor.getArtifactId();
	}

	@Override
	public String groupId() {
		return moduleDescriptor.getGroupId();
	}

	@Override
	public String version() {
		return moduleDescriptor.getVersion();
	}

	@Override
	public String globalId() {
		return moduleDescriptor.getModuleGlobalId();
	}

	@Override
	public ClassLoader moduleClassLoader() {
		return moduleClassLoader;
	}

}
