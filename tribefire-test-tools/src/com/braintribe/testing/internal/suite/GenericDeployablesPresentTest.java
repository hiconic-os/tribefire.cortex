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
package com.braintribe.testing.internal.suite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.DeploymentStatus;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.product.rat.imp.ImpApi;
import com.braintribe.product.rat.imp.ImpApiFactory;
import com.braintribe.utils.lcd.CommonTools;

/**
 * checks if all expected deployables are present and deployed, as well as expected demo entities are present
 *
 */
public class GenericDeployablesPresentTest  {

	private static Logger logger = Logger.getLogger(GenericDeployablesPresentTest.class);

	private final ImpApi imp;
	public GenericDeployablesPresentTest(PersistenceGmSessionFactory factory) {
		PersistenceGmSession session = factory.newSession("cortex");
		imp = new ImpApiFactory().factory(factory).build();
	}

	/**
	 *
	 * @param externalId
	 *            String
	 * @param deployableType
	 *            EntityType
	 *
	 * @throws GmSessionException
	 *             if no deployable with respective externalId and type is present AND deployed
	 */
	public void assertThatDeployableIsPresentAndDeployed(String externalId, EntityType<? extends Deployable> deployableType) {

		Deployable deployable = imp.deployable(deployableType, externalId).get();
		
		boolean isDeployed = deployable.getDeploymentStatus().equals(DeploymentStatus.deployed);
		String assertionFailureMessage = deployableType.getShortName() + " '" + externalId + "' is not deployed";

		if (!isDeployed) {
			throw new GmSessionException(assertionFailureMessage);
		}
	}


	/**
	 * makes sure there is at least one entity of every given type present
	 */
	public static void testEntitiesPresent(PersistenceGmSession accessSessoin, EntityType<?>... types) {
		logger.info("Test if at least one instance of each expected entity type was instantiated...");
		
		for (EntityType<?> type: CommonTools.getSet(types)){
			logger.info("Check if at least one instance of " + type.getTypeSignature() + " was instantiated.");
			EntityQuery entityQuery = EntityQueryBuilder.from(type).done();
			List<?> list = accessSessoin.query().entities(entityQuery).list();

			assertThat(!list.isEmpty());
		}
		
		logger.info("Test succeeded!");

	}

}
