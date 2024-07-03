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
package com.braintribe.model.processing.deployment.processor.bidi.data;

import static com.braintribe.utils.lcd.CollectionTools2.asSet;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.processing.itw.analysis.JavaTypeAnalysis;
import com.braintribe.model.processing.itw.analysis.JavaTypeAnalysisException;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

/**
 * 
 */
public class BidiPropertyTestModel {

	public static GmMetaModel enriched() {
		try {
			return loadModel();

		} catch (JavaTypeAnalysisException e) {
			throw new RuntimeException("", e);
		}
	}

	private static GmMetaModel loadModel() throws JavaTypeAnalysisException {
		JavaTypeAnalysis jta = new JavaTypeAnalysis();
		GmType c = jta.getGmType(Company.class);
		GmType p = jta.getGmType(Person.class);
		GmType f = jta.getGmType(Folder.class);

		GmMetaModel result = GmMetaModel.T.create();
		result.setName("test:BidiModel");
		result.getDependencies().add(NewMetaModelGeneration.rootModel().getMetaModel());
		result.setTypes(asSet(c, p, f));

		return result;
	}

}
