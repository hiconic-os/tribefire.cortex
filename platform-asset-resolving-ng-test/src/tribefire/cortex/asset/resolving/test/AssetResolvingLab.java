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
package tribefire.cortex.asset.resolving.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.console.Console;
import com.braintribe.console.ConsoleConfiguration;
import com.braintribe.console.PlainSysoutConsole;
import com.braintribe.devrock.mc.core.commons.ArtifactResolutionUtil;
import com.braintribe.devrock.mc.core.wirings.maven.configuration.MavenConfigurationWireModule;
import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;
import com.braintribe.testing.category.KnownIssue;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;

import tribefire.cortex.asset.resolving.ng.api.AssetDependencyResolver;
import tribefire.cortex.asset.resolving.ng.api.AssetResolutionContext;
import tribefire.cortex.asset.resolving.ng.api.PlatformAssetResolution;
import tribefire.cortex.asset.resolving.ng.impl.PlatformAssetResolver;
import tribefire.cortex.asset.resolving.ng.impl.PlatformAssetSolution;
import tribefire.cortex.asset.resolving.ng.wire.AssetResolverWireModule;
import tribefire.cortex.asset.resolving.ng.wire.contract.AssetResolverContract;
import tribefire.cortex.asset.resolving.test.wire.AssetResolvingTestWireModule;
import tribefire.cortex.asset.resolving.test.wire.contract.AssetResolvingTestContract;

@Category(KnownIssue.class)
public class AssetResolvingLab {
	private WireContext<AssetResolverContract> context;
	private final File yaml = new File( "res/repo-src/repository-skeleton-validation.yml");

	@Before
	public void initialize() {
		context = Wire.context(AssetResolverWireModule.INSTANCE, MavenConfigurationWireModule.INSTANCE);
	}
	
	@After
	public void dispose() {
		if (context != null)
			context.shutdown();
	}
	
	@Test
	public void labTest() throws Exception {
		AssetDependencyResolver assetResolver = context.contract().assetDependencyResolver();
		
		String name = "tribefire.extension.demo:demo-setup#2.0";
		CompiledDependencyIdentification setupDependency = CompiledDependencyIdentification.parseAndRangify(name, true);

		AssetResolutionContext resolutionContext = AssetResolutionContext.build().selectorFiltering(true).done();
		
		PlatformAssetResolution assetResolution = assetResolver.resolve(resolutionContext, setupDependency);

		ConsoleConfiguration.install(new PlainSysoutConsole());
		ArtifactResolutionUtil.printDependencyTree(assetResolution.artifactResolution());
		
		//Validator.validate(yaml, assetResolution.getSolutions());
	}
	
}
