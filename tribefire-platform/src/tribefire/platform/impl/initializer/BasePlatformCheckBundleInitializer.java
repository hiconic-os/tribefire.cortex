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
package tribefire.platform.impl.initializer;

import com.braintribe.model.extensiondeployment.check.CheckBundle;
import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.cortex.model.check.CheckCoverage;
import tribefire.platform.impl.check.BaseConnectivityCheckProcessor;
import tribefire.platform.impl.check.BaseFunctionalityCheckProcessor;
import tribefire.platform.impl.check.BaseVitalityCheckProcessor;
import tribefire.platform.wire.space.system.ChecksSpace;

public class BasePlatformCheckBundleInitializer extends SimplePersistenceInitializer {

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		ManagedGmSession session = context.getSession();

		CheckProcessor functionalityProcessor = session
				.getEntityByGlobalId(ChecksSpace.checkProcessorGlobalId(BaseFunctionalityCheckProcessor.class));

		CheckBundle baseCheckBundleFunctionality = session.create(CheckBundle.T, "398c462e-685d-4286-8b46-fdd46d000dca");
		baseCheckBundleFunctionality.setName("Base Platform Functionality Checks");
		baseCheckBundleFunctionality.setCoverage(CheckCoverage.functional);
		baseCheckBundleFunctionality.getChecks().add(functionalityProcessor);

		CheckProcessor connectivityProcessor = session.getEntityByGlobalId(ChecksSpace.checkProcessorGlobalId(BaseConnectivityCheckProcessor.class));

		CheckBundle baseCheckBundleConnectivity = session.create(CheckBundle.T, "31798088-af54-4467-b33f-5794f9bb573c");
		baseCheckBundleConnectivity.setName("Base Platform Connectivity Checks");
		baseCheckBundleConnectivity.setCoverage(CheckCoverage.connectivity);
		baseCheckBundleConnectivity.getChecks().add(connectivityProcessor);

		CheckProcessor vitalityProcessor = session.getEntityByGlobalId(ChecksSpace.checkProcessorGlobalId(BaseVitalityCheckProcessor.class));

		CheckBundle baseCheckBundleVitality = session.create(CheckBundle.T, "a8a61b8d-d354-4be3-a084-c353e7ae0268");
		baseCheckBundleVitality.setName("Base Platform Vitality Checks");
		baseCheckBundleVitality.setCoverage(CheckCoverage.vitality);
		baseCheckBundleVitality.getChecks().add(vitalityProcessor);

	}

}
