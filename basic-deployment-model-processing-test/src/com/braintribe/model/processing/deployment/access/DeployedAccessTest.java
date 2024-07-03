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
package com.braintribe.model.processing.deployment.access;

import static com.braintribe.model.processing.deployment.access.utils.EntityTools.getValueForSimpleType;
import static com.braintribe.model.processing.deployment.access.utils.EntityTools.isIntegerType;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.SimpleType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class DeployedAccessTest extends AbstractAccessTest {

	private static String typeSignature = "com.braintribe.$public.Person";

	public static void main(String[] args) throws Exception {
		loadArguments(args);

		listAllEntities();
		Object newEntityId = createNewEntity();
		listAllEntities();
		deleteEntity(newEntityId);
		listAllEntities();
	}

	private static void loadArguments(String[] args) {
		if (args.length < 2) {
			throw new RuntimeException(
					"2 arguments needed - 'acceessId' and 'typeSignature' - id of access to be used and signature of type which will be queried and manipulated.");
		}

		accessId = args[0];
		typeSignature = args[1];

		System.out.println("###########################");
		System.out.println("CONFIG:");
		System.out.println("accessId -> " + accessId);
		System.out.println("typeSignature -> " + typeSignature);
		System.out.println("###########################");
	}

	private static Object createNewEntity() throws Exception {
		System.out.println("\nCreating new " + typeSignature);

		PersistenceGmSession gmSession = createNewSession();
		EntityType<? extends GenericEntity> et = GMF.getTypeReflection().getType(typeSignature);

		GenericEntity ge = gmSession.create(et);
		for (Property p: et.getProperties()) {
			if (p.isIdentifier()) {
				if (!isIntegerType(p.getType())) {
					Object value = getValueForSimpleType((SimpleType) p.getType());
					p.set(ge, value);
					System.out.println(p.getName() + " -> " + value + "[id]");
				} else {
					System.out.println(p.getName() + " -> ?? [id]");
				}

				continue;
			}

			GenericModelType pt = p.getType();
			if (pt instanceof SimpleType) {
				Object value = getValueForSimpleType((SimpleType) pt);
				System.out.println(p.getName() + " -> " + value);
				p.set(ge, value);

			} else {
				System.out.println(p.getName() + " -> null [" + pt.getTypeName() + "]");
			}
		}
		System.out.println("Committing: " + ge);
		gmSession.commit();

		System.out.println("--------------> Entity saved with id: " + et.getIdProperty().get(ge));

		return et.getIdProperty().get(ge);
	}

	private static void deleteEntity(Object id) throws Exception {
		newEntityService().deleteEntity(typeSignature, id);
	}

	private static void listAllEntities() {
		listAllEntities(typeSignature);
	}

}
