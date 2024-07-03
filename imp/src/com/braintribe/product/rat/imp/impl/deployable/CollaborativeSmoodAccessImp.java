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

import com.braintribe.model.accessdeployment.aspect.AspectConfiguration;
import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.utils.lcd.CommonTools;

/**
 * A {@link AccessImp} specialized in {@link CollaborativeSmoodAccess}
 */
public class CollaborativeSmoodAccessImp extends BasicDeployableImp<CollaborativeSmoodAccess> {

	public CollaborativeSmoodAccessImp(PersistenceGmSession session, CollaborativeSmoodAccess access) {
		super(session, access);
	}

	public CollaborativeSmoodAccessImp addAspect(AccessAspect accessAspect) {
		logger.debug("Adding 'AccessAspect' of the type [" + accessAspect.getClass().getName() + "]");
		AspectConfiguration aspectConfiguration = session().create(AspectConfiguration.T);
		aspectConfiguration.setAspects(CommonTools.getList(accessAspect));
		get().setAspectConfiguration(aspectConfiguration);
		return this;
	}

}
