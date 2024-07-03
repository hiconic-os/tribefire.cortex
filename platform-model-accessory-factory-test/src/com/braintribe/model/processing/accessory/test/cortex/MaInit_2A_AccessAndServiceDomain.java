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
package com.braintribe.model.processing.accessory.test.cortex;

import static com.braintribe.model.generic.reflection.Model.modelGlobalId;

import com.braintribe.model.accessdeployment.HardwiredAccess;
import com.braintribe.model.accessdeployment.HardwiredCollaborativeAccess;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.prompt.Name;
import com.braintribe.model.meta.selector.AccessTypeSelector;
import com.braintribe.model.processing.accessory.test.custom.model.CustomEntity;
import com.braintribe.model.processing.itw.analysis.JavaTypeAnalysis;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.service.domain.ServiceDomain;

/**
 * @author peter.gazdik
 */
public class MaInit_2A_AccessAndServiceDomain extends SimplePersistenceInitializer implements MaTestConstants {

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		ManagedGmSession session = context.getSession();

		HardwiredAccess cortex = session.create(HardwiredCollaborativeAccess.T, "hardwired:cortex");
		cortex.setMetaModel(session.getEntityByGlobalId(modelGlobalId(CORTEX_MODEL_NAME)));
		cortex.setServiceModel(session.getEntityByGlobalId(modelGlobalId(CORTEX_SERVICE_MODEL_NAME)));
		cortex.setExternalId(MaTestConstants.CORTEX_ACCESS_EXTERNAL_ID);
		cortex.setAutoDeploy(true);

		GmMetaModel customModel = configuredCustomAccessModel(session);

		HardwiredAccess custom = session.create(HardwiredAccess.T, CUSTOM_ACCESS_GLOBAL_ID);
		custom.setMetaModel(customModel);
		custom.setExternalId(MaTestConstants.CUSTOM_ACCESS_EXTERNAL_ID);
		custom.setAutoDeploy(true);

		ServiceDomain sd = session.create(ServiceDomain.T, CUSTOM_SERVICE_DOMAIN_GLOBL_ID);
		sd.setServiceModel(customModel);
		sd.setExternalId(MaTestConstants.CUSTOM_SERVICE_DOMAIN_EXTERNAL_ID);

		ServiceDomain emptySd = session.create(ServiceDomain.T, EMPTY_SERVICE_DOMAIN_GLOBL_ID);
		emptySd.setExternalId(MaTestConstants.EMPTY_SERVICE_DOMAIN_EXTERNAL_ID);
	}

	private GmMetaModel configuredCustomAccessModel(ManagedGmSession session) {
		GmMetaModel model = session.getEntityByGlobalId(modelGlobalId(CUSTOM_MODEL_NAME));

		ModelMetaDataEditor mdEditor = BasicModelMetaDataEditor.create(model).withSession(session).done();

		mdEditor.onEntityType(CustomEntity.T) //
				.addMetaData(specialNameForAccessTypeMd(session));

		return model;
	}

	private Name specialNameForAccessTypeMd(ManagedGmSession session) {
		GmEntityType hwAccessGmType = session.findEntityByGlobalId(JavaTypeAnalysis.typeGlobalId(HardwiredAccess.class.getName()));

		AccessTypeSelector selector = session.create(AccessTypeSelector.T);
		selector.setAccessType(hwAccessGmType);

		Name result = session.create(Name.T, "md:name.with.selector");
		result.setSelector(selector);

		return result;
	}

}
