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
package com.braintribe.model.processing.resource.filesystem.path;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.braintribe.cfg.Required;

/**
 * @author peter.gazdik
 */
public class AccessFsPathResolver extends AbstractFsPathResolver {

	private Function<String, Path> accessPathSupplier;

	private final Map<String, Path> accessPaths = new ConcurrentHashMap<>();

	@Required
	public void setAccessPathSupplier(Function<String, Path> accessPathSupplier) {
		this.accessPathSupplier = requireNonNull(accessPathSupplier, "accessPathSupplier must not be null");
	}

	@Override
	public Path resolveDomainPath(String domainId) {
		requireNonNull(domainId, "domainId must not be null");

		return accessPaths.computeIfAbsent(domainId, this::resolvePathUsingSupplier);
	}

	private Path resolvePathUsingSupplier(String domainId) {
		if (accessPathSupplier == null)
			throw new IllegalStateException("No base path is configured for [ " + domainId + " ] and no function for generating one was configured");

		Path path = requireNonNull(accessPathSupplier.apply(domainId),
				() -> "No base path is configured for [ " + domainId + " ] and the function configured for generating one returned null");
		
		return path.normalize();
	}

}
