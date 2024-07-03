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

import com.braintribe.model.cortex.aspect.FulltextAspect;
import com.braintribe.model.cortex.aspect.StateProcessingAspect;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.extensiondeployment.StateChangeProcessorRule;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.AbstractImpCave;
import com.braintribe.utils.CommonTools;

/**
 * An {@link AbstractImpCave} specialized in {@link AccessAspect}
 */
public class AspectImpCave extends AbstractImpCave<AccessAspect, AspectImp> {

	public AspectImpCave(PersistenceGmSession session) {
		super(session, "externalId", AccessAspect.T);
	}

	@Override
	protected AspectImp buildImp(AccessAspect instance) {
		return new AspectImp(session(), instance);
	}

	public BasicDeployableImp<StateProcessingAspect> createStateProcessingAspect(String name, String externalId,
			StateChangeProcessorRule... processors) {
		StateProcessingAspect accessAspect = createAA(StateProcessingAspect.T, name, externalId);
		accessAspect.setProcessors(CommonTools.getList(processors));
		return new BasicDeployableImp<StateProcessingAspect>(session(), accessAspect);
	}

	public BasicDeployableImp<FulltextAspect> createFulltextAspect(String name, String externalId) {
		FulltextAspect accessAspect = createAA(FulltextAspect.T, name, externalId);
		return new BasicDeployableImp<FulltextAspect>(session(), accessAspect);
	}

	private <T extends AccessAspect> T createAA(EntityType<T> accessType, String name, String externalId) {
		logger.info("Creating access aspect of type '" + accessType.getShortName() + "'");
		T accessAspect = session().create(accessType);
		accessAspect.setName(name);
		accessAspect.setExternalId(externalId);
		return accessAspect;
	}

	public <T extends AccessAspect> AspectImp createAccessAspect(EntityType<T> accessType, String name, String externalId) {
		return new AspectImp(session(), createAA(accessType, name, externalId));
	}

}
