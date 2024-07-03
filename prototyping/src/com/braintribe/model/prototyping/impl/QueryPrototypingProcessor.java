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
package com.braintribe.model.prototyping.impl;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.prototyping.api.QueryPrototyping;
import com.braintribe.model.query.EntityQuery;

public class QueryPrototypingProcessor extends PrototypingProcessor<QueryPrototyping> {
	private PersistenceGmSessionFactory sessionFactory;
	
	@Configurable
	@Required
	public void setSessionFactory(PersistenceGmSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public GenericEntity process(ServiceRequestContext requestContext, QueryPrototyping request) {
		PersistenceGmSession querySession = sessionFactory.newSession(request.getAccessId());
		
		EntityQuery entityQuery = EntityQueryBuilder.from(GenericEntity.T) //
				.tc() //
					.negation() //
					.joker() //
				.where() //
				.property(GenericEntity.globalId) //
				.eq(request.getPrototypeGlobalId()) //
				.done();
		
		GenericEntity prototype = querySession.query().entities(entityQuery).first();
		
		if (prototype == null) {
			throw new IllegalArgumentException("Could not find prototype with globalId: '" + request.getPrototypeGlobalId() + "' in access: '" + request.getAccessId() + "'.");
		}
		
		return prototype;
	}

}
