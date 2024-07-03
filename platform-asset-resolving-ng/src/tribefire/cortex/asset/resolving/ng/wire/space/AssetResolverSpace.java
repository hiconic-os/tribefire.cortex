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
package tribefire.cortex.asset.resolving.ng.wire.space;

import com.braintribe.devrock.mc.core.wirings.resolver.contract.ArtifactDataResolverContract;
import com.braintribe.devrock.mc.core.wirings.transitive.contract.TransitiveResolverContract;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.asset.resolving.ng.api.AssetDependencyResolver;
import tribefire.cortex.asset.resolving.ng.impl.PlatformAssetResolver;
import tribefire.cortex.asset.resolving.ng.wire.contract.AssetResolverContract;

@Managed
public class AssetResolverSpace implements AssetResolverContract {
	@Import
	private ArtifactDataResolverContract artifactDataResolver;

	@Import
	private TransitiveResolverContract transitiveResolver;
	
	@Override
	public AssetDependencyResolver assetDependencyResolver() {
		PlatformAssetResolver bean = new PlatformAssetResolver();
		bean.setArtifactPartResolver(artifactDataResolver.artifactResolver());
		bean.setTransitiveDependencyResolver(transitiveResolver.transitiveDependencyResolver());
		bean.setRepositoryReflection(artifactDataResolver.repositoryReflection());
		return bean;
	}

	@Override
	public TransitiveResolverContract transitiveResolverContract() {
		return transitiveResolver;
	}
}
