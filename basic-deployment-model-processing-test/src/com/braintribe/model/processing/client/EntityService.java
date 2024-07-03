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
package com.braintribe.model.processing.client;

import java.util.List;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;

/**
 * Class with some convenience methods for manipulating entities via {@link PersistenceGmSession}
 */
public class EntityService {

	private PersistenceGmSession session;

	public EntityService(PersistenceGmSession session) {
		this.session = session;
	}

	public void deleteEntity(String typeSignature, Object id) {
		System.out.println("\nDeleting " + typeSignature + " where ${id}==" + id);
		GenericEntity ge = get(typeSignature, id);

		if (ge == null) {
			System.out.println("empty");
			return;
		}

		deleteEntity(ge);
	}

	public void deleteEntity(GenericEntity ge) {
		deleteEntity(ge, true);
	}
	
	public void deleteEntity(GenericEntity ge, boolean verbose) {
		if (verbose)
			System.out.println("Deleting: " + ge);
		
		session.deleteEntity(ge);
		try {
			session.commit();
		} catch (GmSessionException e) {
			throw new RuntimeException("Deleting entity failed. Entity: " + ge, e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> T get(String typeSignature, Object id) {
		String idName = getIdProperty(typeSignature).getName();
		EntityQuery query = EntityQueryBuilder.from(typeSignature).where().property(idName).eq(id).done();
		List<GenericEntity> list = execute(query);

		if (list.isEmpty()) {
			return null;
		}

		return (T) list.get(0);
	}

	public List<GenericEntity> list(String typeSignature) {
		return execute(EntityQueryBuilder.from(typeSignature).done());
	}

	private List<GenericEntity> execute(EntityQuery query) {
		try {
			return session.query().entities(query).list();

		} catch (GmSessionException e) {
			throw new RuntimeException("Query execution failed.", e);
		}
	}

	private Property getIdProperty(String typeSignature) {
		EntityType<? extends GenericEntity> et = GMF.getTypeReflection().getType(typeSignature);

		for (Property p: et.getProperties()) {
			if (p.isIdentifier()) {
				return p;
			}
		}

		throw new RuntimeException("Id property not foun for: " + typeSignature);
	}
}
