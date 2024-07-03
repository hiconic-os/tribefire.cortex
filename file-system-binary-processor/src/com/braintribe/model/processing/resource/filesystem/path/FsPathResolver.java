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

import java.nio.file.Path;
import java.util.function.Function;

import com.braintribe.model.resource.source.FileSystemSource;

/**
 * @author peter.gazdik
 */
public interface FsPathResolver {

	Path resolveDomainPath(String domainId);

	default Path resolveSourcePath(Path basePath, FileSystemSource source) {
		return resolveSourcePath(basePath, source.getPath());
	}

	Path resolveSourcePath(Path basePath, String sourcePath);

	default Path resolveSourcePathForDomain(String domainId, FileSystemSource source) {
		return resolveSourcePathForDomain(domainId, source.getPath());
	}

	default Path resolveSourcePathForDomain(String domainId, String sourcePath) {
		Path domainpath = resolveDomainPath(domainId);
		return resolveSourcePath(domainpath, sourcePath);
	}

	// ##################################################################
	// ## . . . . . . . . Resolver for a single domain . . . . . . . . ##
	// ##################################################################

	default Function<String, Path> pathResolverForDomain(String domainId) {
		return sourcePath -> resolveSourcePathForDomain(domainId, sourcePath);
	}

}
