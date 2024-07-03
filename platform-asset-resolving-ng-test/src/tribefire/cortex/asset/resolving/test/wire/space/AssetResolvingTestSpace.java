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
package tribefire.cortex.asset.resolving.test.wire.space;

import static com.braintribe.wire.api.scope.InstanceConfiguration.currentInstance;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import com.braintribe.devrock.mc.core.wirings.configuration.contract.RepositoryConfigurationContract;
import com.braintribe.devrock.repolet.launcher.Launcher;
import com.braintribe.utils.FileTools;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;

import tribefire.cortex.asset.resolving.ng.api.AssetDependencyResolver;
import tribefire.cortex.asset.resolving.ng.wire.contract.AssetResolverContract;
import tribefire.cortex.asset.resolving.test.wire.contract.AssetResolvingTestContract;

@Managed
public class AssetResolvingTestSpace implements AssetResolvingTestContract {
	@Import
	private RepoConfigSpace repoConfig;
	
	@Import
	private AssetResolverContract assetResolver;
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		launcher();
	}
	
	@Override
	public RepositoryConfigurationContract repositoryConfigurationContract() {
		return repoConfig;
	}
	
	public File temporaryDataFolder() {
		File bean = new File("temp-data");
		
		if (bean.exists()) {
			try {
				FileTools.deleteDirectoryRecursively(bean);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}	
		}

		bean.mkdirs();
		
		return bean;
	}
	
	public File localRepositoryFolder() {
		return new File(temporaryDataFolder(), "local-repository");
	}
	
	public String repoUrl() {
		return launcher().getLaunchedRepolets().get("repo");
	}
	
	public AssetDependencyResolver assetResolver() {
		return assetResolver.assetDependencyResolver();
	}
	
	@Managed
	private Launcher launcher() {
		Launcher launcher = Launcher.build().repolet().name("repo").filesystem().filesystem(new File("res/repo")).close().close().done();
		
		currentInstance().onDestroy(launcher::shutdown);
		
		launcher.launch();
		
		return launcher;
	}
}
