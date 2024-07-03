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
package com.braintribe.model.processing.access.service.impl.standard;

import static com.braintribe.model.generic.typecondition.TypeConditions.isAssignableTo;
import static com.braintribe.model.generic.typecondition.TypeConditions.isKind;
import static com.braintribe.model.generic.typecondition.TypeConditions.or;
import static com.braintribe.model.generic.typecondition.basic.TypeKind.collectionType;
import static com.braintribe.model.generic.typecondition.basic.TypeKind.entityType;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.resource.Resource;

class ModelEnvironmentQueryBuilder {

	/* package */ static EntityQuery buildModelEnvironmentQuery(String modelName) {
		// @formatter:off
		EntityQuery query = EntityQueryBuilder
					.from(GmMetaModel.class)
					.where()
						.property(GmMetaModel.name).eq(modelName)
					.tc(createTraversingCriterion())
					.done();
		// @formatter:on
		
		query.setNoAbsenceInformation(true);
		return query;
	}

	private static TraversingCriterion createTraversingCriterion() {
		return TC.create()
			.disjunction()
				.pattern()
					.entity(Resource.T)
					.property(Resource.resourceSource)
				.close()
				.pattern()
					.conjunction()
						.entity()
						.typeCondition(isAssignableTo(IncrementalAccess.T))
					.close()
					.conjunction()
						.property()
						.typeCondition(or(isKind(collectionType), isKind(entityType)) )
					.close()
				.close()
			.close()
			.done();
		
	}
}
