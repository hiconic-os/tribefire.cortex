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
package com.braintribe.model.processing.management;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.management.MetaModelValidationResult;
import com.braintribe.model.management.MetaModelValidationViolation;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.management.impl.validator.BasicCheck;
import com.braintribe.model.processing.management.impl.validator.PropertiesConsistencyCheck;
import com.braintribe.model.processing.management.impl.validator.TypesAllUsedAreDeclaredCheck;
import com.braintribe.model.processing.management.impl.validator.TypesIsPlainConsistencyCheck;
import com.braintribe.model.processing.management.impl.validator.TypesLoopsInHierarchyCheck;
import com.braintribe.model.processing.management.impl.validator.ValidatorCheck;

public class MetaModelValidator {
	
	private List<ValidatorCheck> checksList = null;
	
	public MetaModelValidator() {
		
	}
	
	public void setChecksList(List<ValidatorCheck> checksList) {
		this.checksList = checksList;
	}
	
	private List<ValidatorCheck> getChecksList() {
		if (checksList == null) {
			checksList = asList( //

					new BasicCheck(), // must be the first one, as it returns false if MM is null
					new TypesLoopsInHierarchyCheck(), //
					new TypesAllUsedAreDeclaredCheck(), //
					new TypesIsPlainConsistencyCheck(), //
					new PropertiesConsistencyCheck() //
			);
		}
		return checksList;
	}
	
	public MetaModelValidationResult validate(GmMetaModel metaModel) {
		List<MetaModelValidationViolation> violations = new ArrayList<MetaModelValidationViolation>();

		for (ValidatorCheck check : getChecksList()) {
			boolean continueWithNextCheck = check.check(metaModel, violations);
			if (!continueWithNextCheck) {
				break;
			}
		}
		
		MetaModelValidationResult res = MetaModelValidationResult.T.create();
		res.setValid(violations.size() == 0);
		res.setViolations(violations);
		return res;
	}

}
