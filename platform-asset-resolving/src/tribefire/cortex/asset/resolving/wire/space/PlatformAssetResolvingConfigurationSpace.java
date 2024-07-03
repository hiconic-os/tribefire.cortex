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
package tribefire.cortex.asset.resolving.wire.space;

import com.braintribe.ve.api.VirtualEnvironment;
import com.braintribe.ve.impl.StandardEnvironment;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.asset.resolving.wire.contract.PlatformAssetResolvingConfigurationContract;

@Managed
public class PlatformAssetResolvingConfigurationSpace implements PlatformAssetResolvingConfigurationContract {
	@Override
	@Managed
	public VirtualEnvironment virtualEnvironment() {
		return new StandardEnvironment();
	}
}
