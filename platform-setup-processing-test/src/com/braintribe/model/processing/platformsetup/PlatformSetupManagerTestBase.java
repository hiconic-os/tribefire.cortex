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
package com.braintribe.model.processing.platformsetup;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.asset.natures.ManipulationPriming;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class PlatformSetupManagerTestBase {

	protected static final String PFA_TEST_ACCESSID = "test.platformsetup.smood.access";

	// #####################################
	// ## . . . . . . Helpers . . . . . . ##
	// #####################################

	protected static List<PlatformAsset> createAssets(PersistenceGmSession session, int amount) {
		List<PlatformAsset> assets = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			PlatformAsset a = session.create(PlatformAsset.T);
			a.setName("" + i);

			session.commit();

			assets.add(a);
		}

		return assets;
	}

	protected static PlatformAssetNature createNature(PersistenceGmSession session, String accessId) {
		ManipulationPriming m = session.create(ManipulationPriming.T);
		m.setAccessId(accessId);
		return m;
	}

}
