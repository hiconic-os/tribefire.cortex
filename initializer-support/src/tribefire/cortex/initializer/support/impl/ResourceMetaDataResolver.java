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
package tribefire.cortex.initializer.support.impl;

import static java.util.Collections.emptyMap;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.braintribe.cfg.Required;
import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.logging.Logger;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.resource.ResourceMetaDataTools;

import tribefire.module.model.resource.ModuleSource;
import tribefire.module.wire.contract.ModuleResourcesContract;

/**
 * @author peter.gazdik
 */
public class ResourceMetaDataResolver {

	private ModuleResourcesContract moduleResources;
	private Marshaller yamlMarshaller;
	private String moduleName;

	private volatile ConcurrentMap<String, Resource> resourceIndex;
	private ReentrantLock resourceIndexLock = new ReentrantLock();

	private static final Logger log = Logger.getLogger(ResourceMetaDataResolver.class);

	@Required
	public void setModuleResources(ModuleResourcesContract moduleResources) {
		this.moduleResources = moduleResources;
	}

	@Required
	public void setYamlMarshaller(Marshaller yamlMarshaller) {
		this.yamlMarshaller = yamlMarshaller;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Resource resolve(String path) {
		if (resourceIndex == null)
			indexResources();

		return resourceIndex.computeIfAbsent(path, this::computeFor);
	}

	private void indexResources() {
		if (resourceIndex == null) {
			resourceIndexLock.lock();
			try {
				if (resourceIndex == null) {
					resourceIndex = new ConcurrentHashMap<>(resolveIndex());
				}
			} finally {
				resourceIndexLock.unlock();
			}
		}
	}

	private Map<String, Resource> resolveIndex() {
		File indexFile = moduleResources.resource(ModuleSource.INDEX_FILE_NAME).asFile();

		if (indexFile.exists())
			try {
				return (Map<String, Resource>) FileTools.read(indexFile).fromInputStream(yamlMarshaller::unmarshall);

			} catch (RuntimeException e) {
				log.warn("Error while parsing module resources index. Will calculate resource meta-data dynamically, on demand. File: "
						+ indexFile.getAbsolutePath(), e);
			}

		return emptyMap();
	}

	private Resource computeFor(String path) {
		File file = moduleResources.resource(path).asFile();
		if (!file.exists())
			throw new IllegalArgumentException("No module resource found for relative path: " + path);

		return ResourceMetaDataTools.fileToResource(moduleName, file);
	}

}
