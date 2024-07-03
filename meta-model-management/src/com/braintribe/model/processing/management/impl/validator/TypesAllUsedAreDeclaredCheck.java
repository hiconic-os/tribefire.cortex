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
package com.braintribe.model.processing.management.impl.validator;

import java.util.List;
import java.util.Set;

import com.braintribe.model.management.MetaModelValidationViolation;
import com.braintribe.model.management.MetaModelValidationViolationType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.processing.management.impl.util.MetaModelDependencyRegistry;
import com.braintribe.model.processing.management.impl.util.MetaModelDependencyRegistry.DependencyLink;

public class TypesAllUsedAreDeclaredCheck implements ValidatorCheck {
	
	@Override
	public boolean check(GmMetaModel metaModel, List<MetaModelValidationViolation> validationErrors) {
		
		MetaModelDependencyRegistry dependencyRegistry = new MetaModelDependencyRegistry(metaModel);
		for (GmType dependency : dependencyRegistry.getDependencies()) {
//			if (metaModel.getBaseType() == dependency) {
//				continue;
//			}
//			
//			if ((metaModel.getSimpleTypes() != null) && (metaModel.getSimpleTypes().contains(dependency))) {
//				continue;
//			}
//			
//			if ((metaModel.getEntityTypes() != null) && (metaModel.getEntityTypes().contains(dependency))) {
//				continue;
//			}
//			
//			if ((metaModel.getEnumTypes() != null) && (metaModel.getEnumTypes().contains(dependency))) {
//				continue;
//			}
		
			Set<DependencyLink> dependencyLinks = dependencyRegistry.getDependencyLinks(dependency);
			if (dependency == null) {
				ViolationBuilder.to(validationErrors).withTypeAndDependers(dependency, dependencyLinks)
					.add(MetaModelValidationViolationType.TYPE_NULL_HAS_REFERENCES, 
						"<null> type encountered with following dependents: " +
						MetaModelDependencyRegistry.describe(dependencyLinks));
			} else {
				ViolationBuilder.to(validationErrors).withTypeAndDependers(dependency, dependencyLinks)
					.add(MetaModelValidationViolationType.TYPE_NOT_DECLARED_HAS_REFERENCES, 
						"Type '" + dependency.getTypeSignature() + 
						"' not listed in the metaModel.simpleTypes, .entityTypes or .enumTypes, but has following dependents: " + 
						MetaModelDependencyRegistry.describe(dependencyLinks));
			}
		}
		
		return true;
	}

}
