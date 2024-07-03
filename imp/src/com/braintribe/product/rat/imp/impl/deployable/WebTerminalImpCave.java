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

import com.braintribe.model.extensiondeployment.WebTerminal;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.AbstractImpCave;

/**
 * An {@link AbstractImpCave} specialized in {@link WebTerminal}
 */
public class WebTerminalImpCave extends AbstractImpCave<WebTerminal, WebTerminalImp> {

	public WebTerminalImpCave(PersistenceGmSession session) {
		super(session, "externalId", WebTerminal.T);
	}

	@Override
	public WebTerminalImp buildImp(WebTerminal instance) {
		return new WebTerminalImp(session(), instance);
	}

	public <T extends WebTerminal> BasicDeployableImp<T> create(EntityType<T> entityType, String name, String externalId, String pathIdentifier) {
		logger.info("Creating WebTerminal with the name '" + name + "'");
		T webTerminal = session().create(entityType);
		webTerminal.setName(name);
		webTerminal.setExternalId(externalId);
		webTerminal.setPathIdentifier(pathIdentifier);
		return new BasicDeployableImp<T>(session(), webTerminal);
	}
}
