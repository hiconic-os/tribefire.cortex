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
package com.braintribe.model.processing.management.impl.util;

import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.typecondition.TypeConditions;
import com.braintribe.model.generic.typecondition.basic.TypeKind;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.data.display.GroupPriority;

public class MetaModelSyncUtils {

	
	public static TraversingCriterion buildModelTc() {
		return TC.create()
			.conjunction()
				.property()
				.disjunction()
					.typeCondition(TypeConditions.isKind(TypeKind.collectionType))
					.typeCondition(TypeConditions.isKind(TypeKind.entityType))
				.close()
				.negation()
					.disjunction()
						.pattern()
							.disjunction()
								.typeCondition(TypeConditions.isAssignableTo(GmType.class.getName()))
								.typeCondition(TypeConditions.isType(GmProperty.class.getName()))
								.typeCondition(TypeConditions.isType(GmMetaModel.class.getName()))
							.close()
							.property()
						.close()
						.pattern()
						    .typeCondition(TypeConditions.isType(GroupPriority.class.getName()))
							.property("group")
						.close()
					.close()
			.close()
			.done();		
	}

	public static TraversingCriterion buildOverlayPropertyTc() {
		return TC.create()
			.conjunction()
				.property()
				.disjunction()
					.typeCondition(TypeConditions.isKind(TypeKind.collectionType))
					.typeCondition(TypeConditions.isKind(TypeKind.entityType))
				.close()
				.negation()
						.pattern()
							.disjunction()
								.typeCondition(TypeConditions.isAssignableTo(GmType.class.getName()))
								.typeCondition(TypeConditions.isType(GmProperty.class.getName()))
							.close()
							.property()
						.close()
			.close()
			.done();
		
	}
	
}
