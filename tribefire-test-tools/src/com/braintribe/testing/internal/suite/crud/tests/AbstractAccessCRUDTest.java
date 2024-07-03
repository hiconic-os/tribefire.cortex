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

import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;

/**
 * A class that automatically creates a new session to an access (set in constructor), test some CRUD operations and verify the
 * result in the end with another automatically newly created session
 * 
 * @author Neidhart
 *
 */
public abstract class AbstractAccessCRUDTest extends AbstractAccessInspector {
	private static Logger logger = Logger.getLogger(AbstractAccessCRUDTest.class);

	public AbstractAccessCRUDTest(String accessId, PersistenceGmSessionFactory factory) {
		super(accessId, factory);
	}

	/**
	 * 
	 * @param session session to access. provided automatically when calling start()
	 * @return manipulated entities
	 */
	abstract protected List<GenericEntity> run(PersistenceGmSession session);
	
	/**
	 * 
	 * @param verificator provided automatically when calling start() - helps with result verification
	 * @param testResult which was returned by your custom implementation of run()
	 */
	abstract protected void verifyResult(Verificator verificator, List<GenericEntity> testResult);

	public List<GenericEntity> start() {
		PersistenceGmSession session = sessionFactory.newSession(accessId);

		List<GenericEntity> testResult = run(session);

		Verificator verificator = new Verificator(accessId, sessionFactory);
		verificator.setFilterPredicate(getFilterPredicate());

		verifyResult(verificator, testResult);

		return testResult;
	}
}
