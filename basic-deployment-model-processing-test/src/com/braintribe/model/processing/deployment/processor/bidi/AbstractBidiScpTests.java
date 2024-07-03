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
package com.braintribe.model.processing.deployment.processor.bidi;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.processor.AbstractScpTests;
import com.braintribe.model.processing.deployment.processor.BidiPropertyStateChangeProcessor;
import com.braintribe.model.processing.deployment.processor.bidi.data.BidiPropertyTestModel;
import com.braintribe.model.processing.deployment.processor.bidi.data.Company;
import com.braintribe.model.processing.deployment.processor.bidi.data.Folder;
import com.braintribe.model.processing.deployment.processor.bidi.data.Person;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.sp.api.StateChangeProcessorRule;
import com.braintribe.model.query.EntityQuery;

/**
 * 
 */
public abstract class AbstractBidiScpTests extends AbstractScpTests {

	private static BidiPropertyStateChangeProcessor processor = new BidiPropertyStateChangeProcessor();

	@Override
	protected StateChangeProcessorRule stateChangeProcessorRule() {
		return processor;
	}

	@Override
	protected GmMetaModel newMetaModel() {
		return BidiPropertyTestModel.enriched();
	}

	// ###################################
	// ## . . . . . queries . . . . . . ##
	// ###################################

	protected Person personByName(String name) {
		return byName(Person.class, name);
	}

	protected Company companyByName(String name) {
		return byName(Company.class, name);
	}

	protected Folder folderByName(String name) {
		return byName(Folder.class, name);
	}

	protected Person personByName(String name, PersistenceGmSession session) {
		return byName(Person.class, name, session);
	}

	protected Company companyByName(String name, PersistenceGmSession session) {
		return byName(Company.class, name, session);
	}
	
	protected Folder folderByName(String name, PersistenceGmSession session) {
		return byName(Folder.class, name, session);
	}

	
	private <T extends GenericEntity> T byName(Class<T> clazz, String name, PersistenceGmSession session) {
		try {
			EntityQuery eq = EntityQueryBuilder.from(clazz).where().property("name").eq(name).done();
			return clazz.cast(session.query().entities(eq).unique());

		} catch (Exception e) {
			throw new RuntimeException("Query failed!", e);
		}
	}

	// ###################################
	// ## . . . instantiations . . . . .##
	// ###################################

	protected Person newPerson(PersistenceGmSession session) {
		return session.create(Person.T);
	}

	protected Person newPerson(PersistenceGmSession session, String name) {
		Person person = session.create(Person.T);
		person.setName(name);

		return person;
	}

	protected Company newCompany(PersistenceGmSession session) {
		return newCompany(session, null);
	}

	protected Company newCompany(PersistenceGmSession session, String name) {
		Company company = session.create(Company.T);
		company.setName(name);

		return company;
	}

	
	protected Folder newFolder(PersistenceGmSession session) {
		return newFolder(session, null);
	}

	protected Folder newFolder(PersistenceGmSession session, String name) {
		Folder folder = session.create(Folder.T);
		folder.setName(name);
		return folder;
	}

}
