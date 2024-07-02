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
package com.braintribe.model.processing.resource.filesystem.path;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.resource.filesystem.FileSystemBinaryProcessor;

/**
 * @author peter.gazdik
 */
public abstract class AbstractFsPathResolver implements FsPathResolver {

	private static final Logger log = Logger.getLogger(AbstractFsPathResolver.class);

	private Function<String, String> pathResolver;

	@Configurable
	public void setPathResolver(Function<String, String> pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Override
	public Path resolveSourcePath(Path basePath, String sourcePath) {
		if (pathResolver != null)
			sourcePath = pathResolver.apply(sourcePath);

		Path relativeSourcePath = Paths.get(sourcePath);

		if (relativeSourcePath.isAbsolute())
			log.warn("Path resolver got absolute path to be resolved: '" + relativeSourcePath
					+ "'. This might be not allowed any more in future versions.");

		Path finalSourcePath = basePath.resolve(relativeSourcePath).normalize();

		if (!finalSourcePath.startsWith(basePath)) {
			// TODO: NOR 2.Dec, 2020: This exception needs to be enabled ASAP as soon as adx configuration issues are solved
			// throw new IllegalArgumentException("source path '" + sourcePath + "' is resolved to '" + finalSourcePath
			// + "' which is not a child of base path '" + basePath + "'.");
		}

		return finalSourcePath;
	}

}
