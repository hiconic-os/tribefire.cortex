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
package tribefire.platform.wire.space.cortex;

import static com.braintribe.model.generic.typecondition.TypeConditions.isAssignableTo;
import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.map;

import java.util.Map;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.i18n.LocalizedString;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.typecondition.TypeConditions;
import com.braintribe.model.generic.typecondition.basic.TypeKind;
import com.braintribe.model.resource.source.ResourceSource;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

// @formatter:off
@Managed
public class TraversingCriteriaSpace implements WireSpace {

	@Managed
	public Map<Class<? extends GenericEntity>, TraversingCriterion> cortexDefaultMap() {
		return 
			map(
				entry(GenericEntity.class, cortexDefault())
			);
	}
	
	@Managed
	private TraversingCriterion cortexDefault() {
		TraversingCriterion bean = 
				TC.create()
					.conjunction()
						.criterion(standard())
						.negation()
							.disjunction()
								.criterion(localizedStringProperty())
								.criterion(localizedString())
								.criterion(resourceSource())
							.close()
					.close()
				.done();
		return bean;
	}

	@Managed
	private TraversingCriterion localizedStringProperty() {
		TraversingCriterion bean = TC.create() //
				.typeCondition(TypeConditions.isAssignableTo(LocalizedString.T)) //
				.done();

		return bean;
	}

	@Managed
	private TraversingCriterion localizedString() {
		TraversingCriterion bean = 
				TC.create()
					.pattern()
						.entity(LocalizedString.class)
						.property(LocalizedString.localizedValues)
					.close()
				.done();
		return bean;
	}

	@Managed
	private TraversingCriterion standard() {
		// @formatter:off
		return TC.create()
				.pattern()
					.entity()
					.conjunction()
						.property()
						.typeCondition(TypeConditions.or(
								TypeConditions.isKind(TypeKind.collectionType), 
								TypeConditions.isKind(TypeKind.entityType)))
					.close()
				.close()
				.done();
		// @formatter:on
	}

	@Managed
	private TraversingCriterion resourceSource() {
		return TC.create()
				.typeCondition(isAssignableTo(ResourceSource.T))
			.done();
	}

}
