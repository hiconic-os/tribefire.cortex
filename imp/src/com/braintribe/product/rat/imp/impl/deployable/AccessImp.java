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
import com.braintribe.model.accessdeployment.aspect.AspectConfiguration;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.utils.lcd.CommonTools;

/**
 * A {@link BasicDeployableImp} specialized in {@link IncrementalAccess}
 *
 * @param <T>
 *            type of access, this imp is specialized in
 */
public class AccessImp<T extends IncrementalAccess> extends BasicDeployableImp<T> {

	public AccessImp(PersistenceGmSession session, T access) {
		super(session, access);
	}

	public AccessImp<T> addAspect(AccessAspect accessAspect) {
		logger.debug("Adding an access aspect '" + accessAspect.getExternalId() + "' to the access '" + instance.getExternalId() + "'");
		AspectConfiguration aspectConfiguration = session().create(AspectConfiguration.T);
		aspectConfiguration.setAspects(CommonTools.getList(accessAspect));
		instance.setAspectConfiguration(aspectConfiguration);
		return this;
	}

}
