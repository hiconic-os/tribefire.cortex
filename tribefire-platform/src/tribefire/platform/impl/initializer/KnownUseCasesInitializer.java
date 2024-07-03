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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.braintribe.model.meta.selector.KnownUseCase;
import com.braintribe.model.meta.selector.UseCaseSelector;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

public class KnownUseCasesInitializer extends SimplePersistenceInitializer {
	
	private final List<KnownUseCase> defaultUseCases = Arrays.asList(KnownUseCase.values());
	private List<String> additionalUserCases = Collections.emptyList();
	
	
	public void setAdditionalUseCase(List<String> additionalUserCases) {
		this.additionalUserCases = additionalUserCases;
	}
	
	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		
		ManagedGmSession session = context.getSession();
		
		for (KnownUseCase useCase : defaultUseCases) {
			String useCaseValue = TribefireRuntime.getProperty(useCase.name(), useCase.getDefaultValue());
			UseCaseSelector useCaseSelector = session.create(UseCaseSelector.T, "selector:useCase/gme."+useCase.name());
			useCaseSelector.setUseCase(useCaseValue);
		}
		
		for (String useCase : additionalUserCases) {

			UseCaseSelector useCaseSelector = session.create(UseCaseSelector.T, "selector:useCase/"+useCase);
			useCaseSelector.setUseCase(useCase);

		}
		
		
	}
	

}
