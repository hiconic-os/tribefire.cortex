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

import java.util.Collections;
import java.util.Set;

import com.braintribe.ve.api.VirtualEnvironment;
import com.braintribe.ve.impl.StandardEnvironment;

import tribefire.cortex.asset.resolving.ng.impl.AbstractPlatformAssetResolvingContext;

public class AssetResolvingContext extends AbstractPlatformAssetResolvingContext {

	@Override
	public VirtualEnvironment getVirtualEnvironment() {
		return StandardEnvironment.INSTANCE;
	}

	@Override
	public boolean isRuntime() {
		return true;
	}

	@Override
	public boolean isDesigntime() {
		return false;
	}

	@Override
	public String getStage() {
		return "test";
	}

	@Override
	public Set<String> getTags() {
		return Collections.emptySet();
	}

}
