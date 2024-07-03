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
package com.braintribe.model.accessdeployment.orm.meta;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.FileResource;
import com.braintribe.model.resource.Resource;

public interface NativeOrmFolder extends NativeOrm {

	EntityType<NativeOrmFolder> T = EntityTypes.T(NativeOrmFolder.class);

	String getFolderPath();
	void setFolderPath(String value);

	@Override
	default Set<Resource> resources() {

		String path = getFolderPath();

		if (path == null || path.isEmpty()) {
			throw new IllegalStateException("folder path is not set");
		}

		Path p = Paths.get(path);

		if (!Files.isDirectory(p)) {
			throw new IllegalStateException("folder path is not a directory: " + path);
		}

		Set<Resource> resources = new HashSet<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {

			for (Path entry : stream) {

				String entryPath = entry.toString();

				FileResource resource = FileResource.T.create();
				resource.setPath(entryPath);

				resources.add(resource);

			}

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return resources;

	}

}
