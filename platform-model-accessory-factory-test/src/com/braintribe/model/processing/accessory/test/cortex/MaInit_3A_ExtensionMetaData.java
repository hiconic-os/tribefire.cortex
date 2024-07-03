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

import com.braintribe.model.accessapi.QueryRequest;
import com.braintribe.model.accessdeployment.CollaborativeAccess;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.extensiondeployment.HardwiredBinaryProcessor;
import com.braintribe.model.extensiondeployment.meta.StreamWith;
import com.braintribe.model.extensiondeployment.meta.UploadWith;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.components.AccessModelExtension;
import com.braintribe.model.meta.data.components.ServiceModelExtension;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.resource.Resource;

/**
 * @author peter.gazdik
 */
public class MaInit_3A_ExtensionMetaData extends SimplePersistenceInitializer implements MaTestConstants {

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		ManagedGmSession session = context.getSession();

		GmMetaModel cortexDataModel = session.getEntityByGlobalId(modelGlobalId(CORTEX_MODEL_NAME));
		GmMetaModel accessApiModel = session.getEntityByGlobalId(QueryRequest.T.getModel().globalId());

		GmMetaModel csaBinRetrievalConfModel = csaBinRetrievalConfModel(session);
		GmMetaModel someResourceConfModel = someResourceConfModel(session);

		ModelMetaDataEditor mdEditor = BasicModelMetaDataEditor.create(cortexDataModel).withSession(session).done();

		// Every IncrementalAccess supports queries
		mdEditor.onEntityType(IncrementalAccess.T) //
				.addMetaData(serviceModelExtension(session, accessApiModel));

		// CollaborativeAccess gets a special BinaryRetrieval
		mdEditor.onEntityType(CollaborativeAccess.T) //
				.addMetaData(accessModelExtensions(session, csaBinRetrievalConfModel));

		// CustomAccess doesn't have Resource in his model, this is there just to test it doesn't fail
		mdEditor.onEntityType(IncrementalAccess.T) //
				.addMetaData(accessModelExtensions(session, someResourceConfModel));
	}

	private ServiceModelExtension serviceModelExtension(ManagedGmSession session, GmMetaModel model) {
		ServiceModelExtension result = session.create(ServiceModelExtension.T);
		result.getModels().add(model);

		return result;
	}

	private AccessModelExtension accessModelExtensions(ManagedGmSession session, GmMetaModel model) {
		AccessModelExtension result = session.create(AccessModelExtension.T);
		result.getModels().add(model);

		return result;
	}

	private GmMetaModel csaBinRetrievalConfModel(ManagedGmSession session) {
		HardwiredBinaryProcessor binRetrieval = session.create(HardwiredBinaryProcessor.T);
		binRetrieval.setExternalId(CSA_BINARY_RETRIEVAL_EXTERNAL_ID);

		StreamWith binaryRetrievalMd = session.create(StreamWith.T);
		binaryRetrievalMd.setRetrieval(binRetrieval);

		GmMetaModel resourceModel = session.getEntityByGlobalId(modelGlobalId(Resource.T.getModel().name()));

		GmMetaModel mdModel = session.create(GmMetaModel.T, modelGlobalId(CSA_BIN_RETRIEVAL_CONFIG_MODEL));
		mdModel.setName(CSA_BIN_RETRIEVAL_CONFIG_MODEL);
		mdModel.getDependencies().add(resourceModel);

		ModelMetaDataEditor mdEditor = BasicModelMetaDataEditor.create(mdModel).withSession(session).done();
		mdEditor.onEntityType(Resource.T).addMetaData(binaryRetrievalMd);

		return mdModel;
	}

	private GmMetaModel someResourceConfModel(ManagedGmSession session) {
		UploadWith streamWithMd = session.create(UploadWith.T);

		GmMetaModel resourceModel = session.getEntityByGlobalId(modelGlobalId(Resource.T.getModel().name()));

		GmMetaModel mdModel = session.create(GmMetaModel.T, modelGlobalId(SOME_RESOURCE_CONFIG_MODEL));
		mdModel.setName(SOME_RESOURCE_CONFIG_MODEL);
		mdModel.getDependencies().add(resourceModel);

		ModelMetaDataEditor mdEditor = BasicModelMetaDataEditor.create(mdModel).withSession(session).done();
		mdEditor.onEntityType(Resource.T).addMetaData(streamWithMd);

		return mdModel;

	}

}
