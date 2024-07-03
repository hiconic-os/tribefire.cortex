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

import com.braintribe.model.artifact.compiled.CompiledDependencyIdentification;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;

import tribefire.cortex.asset.resolving.ng.api.AssetDependencyResolver;
import tribefire.cortex.asset.resolving.ng.api.AssetResolutionContext;
import tribefire.cortex.asset.resolving.ng.api.PlatformAssetResolution;
import tribefire.cortex.asset.resolving.ng.impl.PlatformAssetSolution;
import tribefire.cortex.asset.resolving.test.wire.AssetResolvingTestWireModule;
import tribefire.cortex.asset.resolving.test.wire.contract.AssetResolvingTestContract;

public class AssetResolvingTest {
	private WireContext<AssetResolvingTestContract> context;
	private final File yaml = new File( "res/repo-src/repository-skeleton-validation.yml");

	@Before
	public void initialize() {
		context = Wire.context(AssetResolvingTestWireModule.INSTANCE);
	}
	
	@After
	public void dispose() {
		if (context != null)
			context.shutdown();
	}
	
	// This is failing.
	// Also why does a core test depend on an extension??? 
	//@Test
	public void simpleTest() throws Exception {
		AssetDependencyResolver assetResolver = context.contract().assetResolver();
		
		CompiledDependencyIdentification setupDependency = CompiledDependencyIdentification.parse("tribefire.extension.test:asset-test-aggregator#[1.0,1.1)");

		AssetResolutionContext resolutionContext = AssetResolutionContext.build().done();
		
		PlatformAssetResolution assetResolution = assetResolver.resolve(resolutionContext, setupDependency);

		for (PlatformAssetSolution solution: assetResolution.getSolutions()) {
			System.out.println(solution.solution.asString() + " -> " + solution.nature.entityType());
		}

		
		Validator.validate(yaml, assetResolution.getSolutions());
	}
	
}
