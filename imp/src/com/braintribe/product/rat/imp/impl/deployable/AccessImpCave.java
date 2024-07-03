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
package com.braintribe.product.rat.imp.impl.deployable;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.AbstractImpCave;

/**
 * An {@link AbstractImpCave} specialized in {@link IncrementalAccess}
 */
public class AccessImpCave extends AbstractImpCave<IncrementalAccess, AccessImp<IncrementalAccess>> {

	public AccessImpCave(PersistenceGmSession session) {
		super(session, "externalId", IncrementalAccess.T);
	}

	public AspectImpCave aspect() {
		return new AspectImpCave(session());
	}

	@Override
	protected AccessImp<IncrementalAccess> buildImp(IncrementalAccess instance) {
		return new AccessImp<IncrementalAccess>(session(), instance);
	}

	public CollaborativeSmoodAccessImp createCsa(String name, String externalId, GmMetaModel metaModel) {
		CollaborativeSmoodAccess access = createIA(CollaborativeSmoodAccess.T, name, externalId, metaModel);
		return new CollaborativeSmoodAccessImp(session(), access);
	}

	public CollaborativeSmoodAccessImp createCsa(String name, String externalId, GmMetaModel metaModel, GmMetaModel serviceModel) {
		CollaborativeSmoodAccess access = createIA(CollaborativeSmoodAccess.T, name, externalId, metaModel, serviceModel);
		return new CollaborativeSmoodAccessImp(session(), access);
	}

	public <T extends IncrementalAccess> AccessImp<T> createIncremental(EntityType<T> accessType, String name, String externalId,
			GmMetaModel metaModel) throws GmSessionException {
		T access = session().create(accessType);
		access.setName(name);
		access.setExternalId(externalId);
		access.setMetaModel(metaModel);
		return new AccessImp<T>(session(), access);
	}

	private <T extends IncrementalAccess> T createIA(EntityType<T> accessType, String name, String externalId, GmMetaModel metaModel)
			throws GmSessionException {
		T access = session().create(accessType);
		access.setName(name);
		access.setExternalId(externalId);
		access.setMetaModel(metaModel);
		return access;
	}

	public <T extends IncrementalAccess> AccessImp<T> createIncremental(EntityType<T> accessType, String name, String externalId,
			GmMetaModel metaModel, GmMetaModel serviceModel) throws GmSessionException {
		T access = createIA(accessType, name, externalId, metaModel);
		access.setServiceModel(serviceModel);
		return new AccessImp<T>(session(), access);
	}

	private <T extends IncrementalAccess> T createIA(EntityType<T> accessType, String name, String externalId, GmMetaModel metaModel,
			GmMetaModel serviceModel) throws GmSessionException {
		T access = createIA(accessType, name, externalId, metaModel);
		access.setServiceModel(serviceModel);
		return access;
	}

}
