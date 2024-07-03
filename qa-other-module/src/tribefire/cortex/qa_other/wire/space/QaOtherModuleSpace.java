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
package tribefire.cortex.qa_other.wire.space;

import static tribefire.cortex.qa.CortexQaCommons.createServiceProcessor;
import static tribefire.cortex.qa.CortexQaCommons.otherModuleName;

import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.model.MainAndOtherSerp;
import tribefire.cortex.qa.CortexQaCommons;
import tribefire.module.api.InitializerBindingBuilder;
import tribefire.module.wire.contract.ServiceBindersContract;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefirePlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class QaOtherModuleSpace implements TribefireModuleContract {

	@Import
	private TribefirePlatformContract tfPlatform;


	//
	// Initializers
	//

	@Override
	public void bindInitializers(InitializerBindingBuilder bindings) {
		bindings.bind(this::createServiceProcessors);
	}

	private void createServiceProcessors(PersistenceInitializationContext context) {
		ManagedGmSession session = context.getSession();

		createServiceProcessor(session, MainAndOtherSerp.T, otherModuleName);
	}

	//
	// Deployables
	//

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		ServiceBindersContract binders = tfPlatform.binders();

		bindings.bind(MainAndOtherSerp.T).component(binders.serviceProcessor())
				.expertSupplier(CortexQaCommons.qaServiceExpertSupplier(MainAndOtherSerp.T));
	}

}
