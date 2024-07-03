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
package tribefire.platform.impl.bootstrapping;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.braintribe.cfg.Required;
import com.braintribe.model.access.ModelAccessException;
import com.braintribe.model.accessdeployment.HardwiredAccess;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.bapi.AvailableAccesses;
import com.braintribe.model.bapi.AvailableAccessesRequest;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.typecondition.TypeConditions;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessorException;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.EntityQueryResult;
import com.braintribe.utils.lcd.NullSafe;

/**
 * TODO: Move to the upcoming PersistenceReflectionServiceProcessor
 */
public class AvailableAccessesRequestProcessor implements ServiceProcessor<AvailableAccessesRequest, AvailableAccesses> {

	private Supplier<com.braintribe.model.access.IncrementalAccess> cortexAccessSupplier;
	private com.braintribe.model.access.IncrementalAccess cortexAccess;
	private ReentrantLock cortexAccessLock = new ReentrantLock();

	@Required
	public void setCortexAccessSupplier(Supplier<com.braintribe.model.access.IncrementalAccess> cortexAccessSupplier) {
		this.cortexAccessSupplier = cortexAccessSupplier;
	}

	@Override
	public AvailableAccesses process(ServiceRequestContext requestContext, AvailableAccessesRequest request) throws ServiceProcessorException {
		boolean includeHardwired = request.getIncludeHardwired();

		EntityQueryResult result = queryAccesses();

		AvailableAccesses availableAccesses = AvailableAccesses.T.create();
		List<IncrementalAccess> accesses = availableAccesses.getAccesses();

		for (GenericEntity access : result.getEntities())
			if (includeHardwired || !(access instanceof HardwiredAccess))
				accesses.add((IncrementalAccess) access);

		accesses.sort((a1, a2) -> {
			if (includeHardwired) {
				int res = isHw(a2) - isHw(a1);
				if (res != 0)
					return res;
			}

			return NullSafe.compare(a1.getName(), a2.getName());
		});

		return availableAccesses;
	}

	private EntityQueryResult queryAccesses() {
		//@formatter:off
		TraversingCriterion tc = TC.create()
			.pattern()
				.typeCondition(TypeConditions.isAssignableTo(IncrementalAccess.T))
				.negation()
					.disjunction()
						.property("name")
						.property("externalId")
						.property("id")
						.property("hardwired")
						.property("deploymentState")
					.close()
			.close()
			.done();
		
		EntityQuery entityQuery = EntityQueryBuilder
				.from(IncrementalAccess.T)
				.where()
				//TODO: originally checked for getDeployed() -> if this check actually needs to be aware of the deployment state an according DDSA request needs to be performed				
					.property(Deployable.autoDeploy).eq(true) 
			    .tc(tc)
				.done();
		//@formatter:on

		try {
			return getCortexAccess().queryEntities(entityQuery);
		} catch (ModelAccessException e) {
			throw new ServiceProcessorException("Error while querying for accesses", e);
		}
	}

	private com.braintribe.model.access.IncrementalAccess getCortexAccess() {
		if (cortexAccess == null)
			loadCortexAccessSync();

		return cortexAccess;
	}

	private void loadCortexAccessSync() {
		if (cortexAccess == null) {
			cortexAccessLock.lock();
			try {
				if (cortexAccess == null) {
					cortexAccess = cortexAccessSupplier.get();
				}
			} finally {
				cortexAccessLock.unlock();
			}
		}
	}

	private int isHw(IncrementalAccess a) {
		return a instanceof HardwiredAccess ? 1 : 0;
	}
}
