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
import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.cfg.Required;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.DeployableComponent;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.query.SelectQueryResult;

/**
 * @author peter.gazdik
 */
public class DeployableComponentInitializer extends SimplePersistenceInitializer {

	private List<EntityType<? extends Deployable>> deployableComponentTypes;

	@Required
	public void setDeployableComponentTypes(List<EntityType<? extends Deployable>> deployableComponentTypes) {
		this.deployableComponentTypes = deployableComponentTypes;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		SelectQueryResult queryResult = queryGmEntityTypes(context);

		DeployableComponent deployableComponentMd = DeployableComponent.T.create();
		deployableComponentMd.setGlobalId("deployable-component:singleton");

		for (Object object : queryResult.getResults()) {
			GmEntityType gmEntityType = (GmEntityType) object;
			gmEntityType.getMetaData().add(deployableComponentMd);
		}
	}

	private SelectQueryResult queryGmEntityTypes(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		Set<String> typeSignatures = deployableComponentTypes.stream().map(EntityType::getTypeSignature).collect(Collectors.toSet());

		ManagedGmSession session = context.getSession();

		// @formatter:off
		SelectQuery query = new SelectQueryBuilder()
			.from(GmEntityType.T, "e")
			.where()
				.property("e", "typeSignature").in(typeSignatures)
			.done();
		// @formatter:on

		try {
			return session.query().select(query).result();

		} catch (GmSessionException e) {
			throw new ManipulationPersistenceException("Error while querying entityTypes: " + typeSignatures, e);
		}
	}

}
