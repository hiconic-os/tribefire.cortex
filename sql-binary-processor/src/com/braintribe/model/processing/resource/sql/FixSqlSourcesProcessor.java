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
package com.braintribe.model.processing.resource.sql;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.resource.source.SqlSource;
import com.braintribe.model.resourceapi.request.FixSqlSources;
import com.braintribe.model.resourceapi.request.FixSqlSourcesResponse;

public class FixSqlSourcesProcessor implements AccessRequestProcessor<FixSqlSources, FixSqlSourcesResponse> {
	static final Set<String> fixedAccesses = ConcurrentHashMap.newKeySet();
	
	private static final Logger log = Logger.getLogger(FixSqlSourcesProcessor.class);
	
	@Override
	public FixSqlSourcesResponse process(AccessRequestContext<FixSqlSources> context) {
		FixSqlSourcesResponse response = FixSqlSourcesResponse.T.create();
		String accessId = context.getDomainId();
		response.setAccessId(accessId);
		
		// If an access was just or is just being fixed, don't bother
		if (!fixedAccesses.add(accessId) && !context.getRequest().getForceUpdate()) {
			response.setAlreadyUpdated(true);
			log.info("Didn't update missing blobId properties for SqlSources in access '" + accessId + "' because the access was already fixed previously or is being fixed at this moment in another thread.");
			return response;
		}
		
		EntityQuery entityQuery = EntityQueryBuilder.from(SqlSource.T).where().property(SqlSource.blobId).eq(null).done();
		List<SqlSource> sources = context.getSession().query().entities(entityQuery).list();
		sources.forEach(s -> s.setBlobId(s.getId()));
		
		response.setNumUpdated(sources.size());
		
		log.info("Set missing blobId property for " + response.getNumUpdated() + " SqlSource entities in access '" + accessId + "'.");
		
		return response;
	}
	
}
