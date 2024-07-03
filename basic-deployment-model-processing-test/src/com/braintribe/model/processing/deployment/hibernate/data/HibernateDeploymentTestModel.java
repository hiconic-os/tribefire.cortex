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
package com.braintribe.model.processing.deployment.hibernate.data;

import java.util.Arrays;
import java.util.List;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.model.tools.MetaModelTools;

/**
 * 
 */
public class HibernateDeploymentTestModel {

	// @formatter:off
	public static final List<EntityType<?>> classes = Arrays.asList(
			Car.T,
			Card.T,
			Company.T,
			CardCompany.T,
			CreditCard.T,
			DebitCard.T,
			TravelPrePaidDebitCard.T,
			Bank.T,
			Employee.T,
			Person.T
	);
	// @formatter:on

	public static GmMetaModel enriched() {
		GmMetaModel originalMetaModel = MetaModelTools.provideRawModel(classes);
		return enrichMetaModel(originalMetaModel);
	}

	private static GmMetaModel enrichMetaModel(GmMetaModel gmMetaModel) {
		@SuppressWarnings("unused")
		ModelMetaDataEditor model = new BasicModelMetaDataEditor(gmMetaModel);

		// TODO: use modelEditor to apply hibernate metadata do entities and properties

		return gmMetaModel;
	}

}
