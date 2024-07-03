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
package com.braintribe.testing.internal.suite.crud.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;

/**
 * Has its own session to an access and helps to verify test results
 */
public class Verificator extends AbstractAccessInspector {
	private PersistenceGmSession session;

	public Verificator(String accessId, PersistenceGmSessionFactory factory) {
		super(accessId, factory);
		this.session = factory.newSession(accessId);
	}

	public void assertEntitiesArePersisted(Collection<GenericEntity> expectedEntities) {
		for (GenericEntity entity : expectedEntities) {
			assertThat(expectedEntityWasInstantiated(entity.entityType(), entity)).isTrue();
		}
	}

	public void assertEntitiesAreNotPresent(Collection<GenericEntity> expectedEntities) {
		for (GenericEntity entity : expectedEntities) {
			GenericEntity foundEntity = session.query().entity(entity).find();
			assertThat(foundEntity).as("Entity was not deleted: %s", foundEntity).isNull();
		}
	}

	private boolean expectedEntityWasInstantiated(EntityType<?> entityType, GenericEntity expectedValue) {

		GenericEntity actualValue = session.query().entity(expectedValue).refresh();

		// ensures that all SIMPLE properties of the entity are as expected
		for (Property property : entityType.getProperties()) {
			if (property.getType().isSimple() && propertyIsNotFiltered(property, actualValue, session)) {
				Object actual = property.get(actualValue);
				Object expected = property.get(expectedValue);

				assertThat(actual).as("Property " + property.getName() + " differs: " + actual + " != " + expected).isEqualTo(expected);
			}
		}

		return true;
	}
	
	
	
	public Iterable<Property> nonFilteredPropertiesOf(GenericEntity entity){
		return nonFilteredPropertiesOf(entity, session);
	}


}
