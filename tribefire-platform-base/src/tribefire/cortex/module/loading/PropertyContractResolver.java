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
package tribefire.cortex.module.loading;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.wire.api.space.ContractResolution;
import com.braintribe.wire.api.space.ContractSpaceResolver;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.properties.PropertyLookups;

import tribefire.module.wire.contract.PropertyLookupContract;

public class PropertyContractResolver implements ContractSpaceResolver {

	private boolean suppressDecryption;

	public void setSuppressDecryption(boolean suppressDecryption) {
		this.suppressDecryption = suppressDecryption;
	}
	
	@Override
	public ContractResolution resolveContractSpace(Class<? extends WireSpace> contractSpaceClass) {
		if (PropertyLookupContract.class.isAssignableFrom(contractSpaceClass))
			return f -> PropertyLookups.create(contractSpaceClass, TribefireRuntime::getProperty, suppressDecryption);
		else
			return null;
	}
}
