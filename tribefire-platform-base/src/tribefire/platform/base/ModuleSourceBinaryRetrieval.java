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
package tribefire.platform.base;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.function.Function;

import com.braintribe.cfg.Required;
import com.braintribe.model.processing.resource.streaming.AbstractFsBasedBinaryRetriever;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.source.ResourceSource;
import com.braintribe.model.resourceapi.base.BinaryRequest;
import com.braintribe.model.resourceapi.persistence.BinaryPersistenceRequest;
import com.braintribe.model.resourceapi.persistence.DeleteBinary;
import com.braintribe.model.resourceapi.persistence.DeleteBinaryResponse;
import com.braintribe.model.resourceapi.persistence.StoreBinary;
import com.braintribe.model.resourceapi.persistence.StoreBinaryResponse;
import com.braintribe.model.resourceapi.stream.BinaryRetrievalRequest;

import tribefire.module.model.resource.ModuleSource;
import tribefire.module.wire.contract.ModuleResourcesContract;

/**
 * A {@link ServiceProcessor } for {@link BinaryRetrievalRequest} for {@link Resource}s backed by {@link ModuleSource}s.
 * <p>
 * Technically it is a processor for all {@link BinaryRequest}s, but {@link BinaryPersistenceRequest}s throw an {@link UnsupportedOperationException}.
 * 
 * @author peter.gazdik
 */
public class ModuleSourceBinaryRetrieval extends AbstractFsBasedBinaryRetriever {

	private Function<String, ModuleResourcesContract> moduleResourcesContractResolver;

	@Required
	public void setModuleResourcesContractResolver(Function<String, ModuleResourcesContract> moduleResourcesContractResolver) {
		this.moduleResourcesContractResolver = moduleResourcesContractResolver;
	}

	@Override
	protected DeleteBinaryResponse delete(ServiceRequestContext context, DeleteBinary originalRequest) {
		return throwReadOnly("delete");
	}

	@Override
	protected StoreBinaryResponse store(ServiceRequestContext context, StoreBinary request) {
		return throwReadOnly("store");
	}

	private <T> T throwReadOnly(String method) {
		throw new UnsupportedOperationException(
				"Method [ModuleSourceBinaryRetrieval." + method + "] is not supported, module sources are read-only!");
	}

	@Override
	protected Path resolvePathForRetrieval(BinaryRetrievalRequest request) {
		Resource resource = request.getResource();

		ModuleSource source = retrieveModuleSource(resource);

		ModuleResourcesContract resourcesContract = resolveResourcesContract(source);

		return resourcesContract.resource(source.getPath()).asPath();
	}

	private ModuleSource retrieveModuleSource(Resource resource) {
		requireNonNull(resource, "Cannot stream null Resource");

		ResourceSource source = resource.getResourceSource();
		requireNonNull(source, () -> "Cannot stream resource with null source: " + resource);

		if (source instanceof ModuleSource)
			return (ModuleSource) source;
		else
			throw new IllegalStateException(
					"ModuleSourceBinaryRetrieval should only be configured for ModuleSource, not: " + source.entityType().getTypeSignature());
	}

	private ModuleResourcesContract resolveResourcesContract(ModuleSource source) {
		return moduleResourcesContractResolver.apply(source.getModuleName());
	}

}
