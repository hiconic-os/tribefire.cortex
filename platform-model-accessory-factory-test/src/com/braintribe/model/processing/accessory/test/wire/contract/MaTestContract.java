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
package com.braintribe.model.processing.accessory.test.wire.contract;

import com.braintribe.model.access.smood.collaboration.deployment.CsaDeployedUnit;
import com.braintribe.model.processing.accessory.impl.PlatformModelAccessoryFactory;
import com.braintribe.model.processing.accessory.impl.PmeSupplierFromCortex;
import com.braintribe.model.processing.accessory.test.wire.space.MaTestSpace;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.space.WireSpace;

/**
 * @author peter.gazdik
 */
public interface MaTestContract extends WireSpace {

	PlatformModelAccessoryFactory platformModelAccessoryFactory();
	
	PmeSupplierFromCortex platformModelEssentialsSupplier();
	
	CsaDeployedUnit cortexCsaDu();
	
	static MaTestContract theContract() {
		return Wire.context(MaTestContract.class) //
				.bindContract(MaTestContract.class, MaTestSpace.class) //
				.build() //
				.contract();
	}
	
	
}
