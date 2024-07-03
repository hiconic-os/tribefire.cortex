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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.product.rat.imp.impl.utils.QueryHelper;

/**
 * See {@link #run(PersistenceGmSession)}
 * @author Neidhart
 *
 */
public class DeleteEntitiesTest extends AbstractAccessCRUDTest {
	private Collection<GenericEntity> entitiesToDelete;

	public DeleteEntitiesTest(String accessId, PersistenceGmSessionFactory factory) {
		super(accessId, factory);
	}

	public Collection<GenericEntity> getEntitiesToDelete() {
		return entitiesToDelete;
	}



	public void setEntitiesToDelete(Collection<GenericEntity> entitiesToDelete) {
		this.entitiesToDelete = entitiesToDelete;
	}


	/**
	 * {@link #setEntitiesToDelete(Collection) must be called first}<br>
	 * deletes every of these entities one by one<br>
	 * Verification step: Checks if the entities really can't be found now any more by a query
	 * @return deleted entities
	 */
	@Override
	protected List<GenericEntity> run(PersistenceGmSession session) {
		if (entitiesToDelete == null)
			throw new IllegalStateException("setEntitiesToDelete() must be called first");
		
		QueryHelper queryHelper = new QueryHelper(session);
		
		for (GenericEntity entity : entitiesToDelete) {
			GenericEntity updatedEntity = session.query().entity(entity).refresh();
			session.deleteEntity(updatedEntity);
			session.commit();

//			assertThat(queryHelper.findById(GenericEntity.T, updatedEntity.getId())).as("Entity was not deleted properly").isNull();
		}
		
		return new ArrayList<>(entitiesToDelete);
	}


	@Override
	protected void verifyResult(Verificator verificator, List<GenericEntity> testResult) {
		verificator.assertEntitiesAreNotPresent(testResult);
	}
}
