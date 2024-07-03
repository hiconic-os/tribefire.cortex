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
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.database.pool.ConfiguredDatabaseConnectionPool;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.AbstractImpCave;

/**
 * An {@link AbstractImpCave} specialized in {@link Deployable}. Additionally this cave serves as an entry point to the
 * "Deployables" part of the ImpApi
 */
public class DeployableImpCave extends AbstractImpCave<Deployable, BasicDeployableImp<Deployable>> {

	public DeployableImpCave(PersistenceGmSession session) {
		super(session, "externalId", Deployable.T);
	}

	public <T extends ConfiguredDatabaseConnectionPool> ConnectionImpCave<T> connection(EntityType<T> connectionType) {
		return new ConnectionImpCave<>(session(), connectionType);
	}

	public AccessImpCave access() {
		return new AccessImpCave(session());
	}

	public WebTerminalImpCave webTerminal() {
		return new WebTerminalImpCave(session());
	}

	// public WorkerImp worker() {
	// return new WorkerImp(session);
	// }

	public ServiceProcessorImpCave serviceProcessor() {
		return new ServiceProcessorImpCave(session());
	}

	@Override
	protected BasicDeployableImp<Deployable> buildImp(Deployable instance) {
		return new BasicDeployableImp<Deployable>(session(), instance);
	}

	public AccessImp<IncrementalAccess> access(String externalId) {
		return new AccessImpCave(session()).with(externalId);
	}

}
