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

import static org.junit.Assert.assertNotNull;

import java.util.stream.Collectors;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.data.constraint.Mandatory;
import com.braintribe.model.processing.meta.cmd.builders.PropertyMdResolver;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.testing.internal.suite.crud.PropertyFilterPredicate;

/**
 * Something that holds an accessId and should be able to create a session to this access, as well as ignore certain
 * properties during its work
 * 
 * @author Neidhart
 *
 */
public class AbstractAccessInspector {

	protected final PersistenceGmSessionFactory sessionFactory;
	protected final String accessId;
	protected PropertyFilterPredicate propertyFilterPredicate;

	public AbstractAccessInspector(String accessId, PersistenceGmSessionFactory factory) {
		this.sessionFactory = factory;
		this.accessId = accessId;
		this.propertyFilterPredicate = this::defaultSkipPropertyPredicate;

	}

	public PropertyFilterPredicate getFilterPredicate() {
		return propertyFilterPredicate;
	}

	public void setFilterPredicate(PropertyFilterPredicate filterPredicate) {
		assertNotNull(filterPredicate);
		this.propertyFilterPredicate = filterPredicate;
	}

	public boolean propertyIsNotFiltered(Property property, GenericEntity entity, PersistenceGmSession session) {
		return propertyFilterPredicate.test(property, entity, session);
	}
	
	public Iterable<Property> nonFilteredPropertiesOf(GenericEntity entity, PersistenceGmSession session){
		return entity.entityType()
				.getProperties()
				.stream()
				.filter(prop -> propertyIsNotFiltered(prop, entity, session))
				.collect(Collectors.toList());
	}

	/**
	 * skips inherited properties except when they are mandatory
	 */
	protected boolean defaultSkipPropertyPredicate(Property property, GenericEntity entity, PersistenceGmSession session) {
		PropertyMdResolver propertyMeta = session.getModelAccessory().getMetaData().entity(entity).property(property);
		boolean relevantType = property.getDeclaringType().equals(entity.entityType()) || propertyMeta.is(Mandatory.T);

		return relevantType;

	}

}
