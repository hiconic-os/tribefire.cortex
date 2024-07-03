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
package tribefire.platform.impl.initializer;

import java.util.List;

import com.braintribe.cfg.Required;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.pr.AbsenceInformation;
import com.braintribe.model.generic.reflection.CloningContext;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.StandardCloningContext;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

public class DefaultDeployableInitializer extends SimplePersistenceInitializer {

	private List<Deployable> deployables;

	public static String defaultDeployableGlobalId(String externalId) {
		return "default:deployable/" + externalId;
	}

	@Required
	public void setDeployables(List<Deployable> deployables) {
		this.deployables = deployables;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		CloningContext cc = new SessionCreatingCloningContext(context.getSession());

		for (Deployable deployable : deployables)
			deployable.clone(cc);
	}

	private static class SessionCreatingCloningContext extends StandardCloningContext {
		private final ManagedGmSession session;

		public SessionCreatingCloningContext(ManagedGmSession session) {
			this.session = session;
		}

		@Override
		public GenericEntity supplyRawClone(EntityType<? extends GenericEntity> entityType, GenericEntity instanceToBeCloned) {
			GenericEntity clone = session.create(entityType);

			if (clone instanceof Deployable)
				if (instanceToBeCloned.getGlobalId() == null) {
					Deployable deployable = (Deployable) instanceToBeCloned;
					clone.setGlobalId(defaultDeployableGlobalId(deployable.getExternalId()));
				}

			return clone;
		}

		@Override
		public boolean canTransferPropertyValue(EntityType<? extends GenericEntity> entityType, Property property, GenericEntity instanceToBeCloned,
				GenericEntity clonedInstance, AbsenceInformation sourceAbsenceInformation) {

			return !property.isGlobalId() || clonedInstance.getGlobalId() == null;
		}

	}

}
