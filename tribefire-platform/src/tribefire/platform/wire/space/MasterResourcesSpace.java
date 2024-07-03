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
package tribefire.platform.wire.space;

import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_CACHE_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_CONFIGURATION_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_DATA_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_REPO_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_SETUP_INFO_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_STORAGE_DIR;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_TMP_DIR;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;

import tribefire.module.wire.contract.WebPlatformResourcesContract;
import tribefire.platform.api.resource.ResourcesBuilder;
import tribefire.platform.wire.space.common.ResourcesBaseSpace;

@Managed
public class MasterResourcesSpace extends ResourcesBaseSpace implements WebPlatformResourcesContract {

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		super.onLoaded();
		builders.put("storage", this::storage);
		builders.put("tmp", this::tmp);
		builders.put("cache", this::cache);
		builders.put("repo", this::repository);
		builders.put("data", this::database);
		builders.put("setup-info", this::setupInfo);
	}

	@Override
	public ResourcesBuilder storage(String url) {
		return PathResourcesBuilder.create(storagePath(), resolve(url));
	}

	@Override
	public ResourcesBuilder tmp(String url) {
		return PathResourcesBuilder.create(tmpPath(), resolve(url));
	}

	@Override
	public ResourcesBuilder cache(String url) {
		return PathResourcesBuilder.create(cachePath(), resolve(url));
	}

	public ResourcesBuilder repository(String url) {
		return PathResourcesBuilder.create(repositoryPath(), resolve(url));
	}

	@Override
	public ResourcesBuilder database(String url) {
		return PathResourcesBuilder.create(databasesPath(), resolve(url));
	}

	public ResourcesBuilder conf(String path) throws UncheckedIOException {
		return PathResourcesBuilder.create(confPath(), resolve(path));
	}

	@Override
	public ResourcesBuilder publicResources(String url) {
		return PathResourcesBuilder.create(publicResourcesPath(), resolve(url));
	}

	public ResourcesBuilder setupInfo(String url) {
		return PathResourcesBuilder.create(setupPath(), resolve(url));
	}

	@Managed
	public Path storagePath() {
		return resolvePath(serverPath(), "../storage", ENVIRONMENT_STORAGE_DIR);
	}

	@Managed
	public Path tmpPath() {
		return resolvePath(storagePath(), "tmp", ENVIRONMENT_TMP_DIR);
	}

	@Managed
	public Path cachePath() {
		return resolvePath(storagePath(), "cache", ENVIRONMENT_CACHE_DIR);
	}

	@Managed
	public Path repositoryPath() {
		return resolvePath(storagePath(), "repository", ENVIRONMENT_REPO_DIR);
	}

	@Managed
	public Path databasesPath() {
		return resolvePath(storagePath(), "databases", ENVIRONMENT_DATA_DIR);
	}

	@Managed
	public Path confPath() {
		return resolvePath(serverPath(), "../conf", ENVIRONMENT_CONFIGURATION_DIR);
	}

	public Path publicResourcesPath() {
		return storagePath().resolve("public-resources");
	}

	@Managed
	public Path setupPath() {
		return resolvePath(storagePath(), "../setup-info", ENVIRONMENT_SETUP_INFO_DIR);
	}

	private Path resolvePath(Path defaultParent, String defaultPath, String propertyName) {
		Path custom = customizedPath(defaultParent, propertyName);
		if (custom != null) {
			return custom;
		}
		return defaultParent.resolve(defaultPath);
	}

	private Path customizedPath(Path parent, String propertyName) {

		String propertyValue = TribefireRuntime.getProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}

		propertyValue = resolve(propertyValue);

		Path customPath = Paths.get(propertyValue);

		if (customPath.isAbsolute()) {
			return customPath;
		}

		return parent.resolve(customPath);

	}

}
