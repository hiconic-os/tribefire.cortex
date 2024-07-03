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

import static com.braintribe.model.processing.accessory.test.cortex.MaTestConstants.CUSTOM_MODEL_NAME;
import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.Collection;

import com.braintribe.model.access.collaboration.persistence.ModelsPersistenceInitializer;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.accessory.test.custom.model.CustomEntity;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

/**
 * @author peter.gazdik
 */
public class MaInit_1B_CustomModel extends ModelsPersistenceInitializer {

	private final NewMetaModelGeneration mmg = new NewMetaModelGeneration(asList(GenericEntity.T.getModel()));

	public MaInit_1B_CustomModel() {
		setCheckIfModelsAreAlreadyThere(true);
	}

	@Override
	protected Collection<GmMetaModel> getModels() {
		return asList(customDataModel());
	}

	private GmMetaModel customDataModel() {
		return mmg.buildMetaModel(CUSTOM_MODEL_NAME, asList(CustomEntity.T));
	}

}
