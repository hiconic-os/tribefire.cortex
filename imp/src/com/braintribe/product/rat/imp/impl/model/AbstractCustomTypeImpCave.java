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
package com.braintribe.product.rat.imp.impl.model;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.ImpException;
import com.braintribe.utils.lcd.Arguments;

abstract class AbstractCustomTypeImpCave<I extends SimpleTypeImp<T>, T extends GmType> extends AbstractTypeImpCave<T, I> {

	AbstractCustomTypeImpCave(PersistenceGmSession session, EntityType<T> typeOfT) {
		super(session, typeOfT);

		Arguments.notNullWithNames("typeOfT", typeOfT, "session", session);

	}

	/**
	 * creates a new type with the passed typeSignature and adds it to the passed metamodel
	 *
	 * @param typeSignature
	 *            for the newly created type
	 * @param declaringModel
	 *            of the newly created type
	 */
	public I create(String typeSignature, GmMetaModel declaringModel) {

		if (find(typeSignature).isPresent()) {
			throw new ImpException("Type with this name already exists: " + typeSignature + ". Consider using the withName() method");
		}

		if (!typeSignature.contains(".")) {
			throw new ImpException("Please specify a full name including groupId (including at least one '.'). You supplied " + typeSignature);
		}

		T type = session().create(typeOfT);
		type.setTypeSignature(typeSignature);

		type.setDeclaringModel(declaringModel);
		declaringModel.getTypes().add(type);

		return with(type);
	}

	/**
	 * creates a new type with the passed typeSignature ('groupId.typeName') and adds it to the passed metamodel
	 */
	public I create(String groupId, String typeName, GmMetaModel declaringModel) {
		String typeSignature = groupId + "." + typeName;

		return create(typeSignature, declaringModel);
	}

	/**
	 * @param groupId
	 *            of the already existing type
	 * @param typeName
	 *            of the already existing type
	 * @return an imp for managing the specified type
	 * @throws ImpException
	 *             when no type could be found with provided groupId and typeName
	 */
	public I withName(String groupId, String typeName) {
		String typeSignature = groupId + "." + typeName;

		return with(typeSignature);
	}

}
