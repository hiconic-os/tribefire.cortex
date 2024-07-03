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
package com.braintribe.model.processing.cortex.priming;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.braintribe.model.access.collaboration.persistence.ModelsPersistenceInitializer;
import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.constraint.CoreModel;
import com.braintribe.model.processing.cortex.CortexModelNames;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.utils.IOTools;

/**
 * This initializer prepares the models inside the cortex access. It puts the entire {@link CortexModelNames#TF_SYNC_MODEL_NAME} into cortex, and also
 * creates new instances for cortex' data, service and workbench models. All three of these are initialized here to depend on the corresponding
 * "packaged" counterpart (which come from classpath). Other initializers might or course extend these three models even more.
 * 
 * @author peter.gazdik
 */
public class CortexModelsPersistenceInitializer extends ModelsPersistenceInitializer implements CortexModelNames {

	private static final String MODULE_MODELS_FILE_NAME = "cortex-models.txt";

	private File storageBase;

	public void setStorageBase(File storageBase) {
		this.storageBase = storageBase;
	}

	@Override
	protected Collection<GmMetaModel> getModels() throws ManipulationPersistenceException {
		return toGmModels(getSyncModelNames(storageBase));
	}

	public static void preLoadGmModels(File storageBase) {
		toGmModels(getSyncModelNames(storageBase));
	}

	private static Stream<String> getSyncModelNames(File storageBase) {
		return Stream.concat( //
				Stream.of(TF_SYNC_MODEL_NAME), //
				readCortexModelNamesFromStorage(storageBase) //
		);
	}

	// Uses Files.lines (in IOTools.linesUnchecked) -> Stream needs to be closed
	private static Stream<String> readCortexModelNamesFromStorage(File storageBase) {
		return Optional.ofNullable(storageBase) //
				.map(sb -> new File(sb, MODULE_MODELS_FILE_NAME)) //
				.filter(File::exists) //
				.map(IOTools::linesUnchecked) //
				.orElse(Stream.empty());
	}

	@Override
	protected void processSyncedModels(Map<String, GmMetaModel> syncedModelsByName, PersistenceInitializationContext context) {
		createExtensibleModels(context, syncedModelsByName);
		appendCoreModelMd(context, syncedModelsByName);

		super.processSyncedModels(syncedModelsByName, context);
	}

	@SuppressWarnings("unused") // to have normalized code we assign the last model we create to a variable -> unused
	private void createExtensibleModels(PersistenceInitializationContext context, Map<String, GmMetaModel> modelsByName) {
		ManagedGmSession session = context.getSession();

		GmMetaModel cortexModel = createExtensibleModel(session, modelsByName, TF_CORTEX_MODEL_NAME, //
				TF_PACKAGED_CORTEX_MODEL_NAME);
		GmMetaModel cortexServiceModel = createExtensibleModel(session, modelsByName, TF_CORTEX_SERVICE_MODEL_NAME, //
				TF_PACKAGED_CORTEX_SERVICE_MODEL_NAME, cortexModel);
		GmMetaModel cortexWorkbenchModel = createExtensibleModel(session, modelsByName, TF_CORTEX_WORKBENCH_MODEL_NAME, //
				TF_PACKAGED_CORTEX_WORKBENCH_MODEL_NAME, cortexServiceModel);
	}

	private GmMetaModel createExtensibleModel(ManagedGmSession session, Map<String, GmMetaModel> modelsByName, String modelName, //
			String packagedModelName, GmMetaModel... otherDependencies) throws ManipulationPersistenceException {

		List<GmMetaModel> dependencies = asList(otherDependencies);
		dependencies.add(modelsByName.get(packagedModelName));

		GmMetaModel result = session.create(GmMetaModel.T, Model.modelGlobalId(modelName));
		result.setName(modelName);
		result.getDependencies().addAll(dependencies);

		return result;
	}

	private void appendCoreModelMd(PersistenceInitializationContext context, Map<String, GmMetaModel> modelsByName) {
		ManagedGmSession session = context.getSession();
		CoreModel coreModelMd = session.acquire(CoreModel.T, "md:coreModel");

		for (GmMetaModel coreModel : modelsByName.values())
			coreModel.getMetaData().add(coreModelMd);
	}

}
