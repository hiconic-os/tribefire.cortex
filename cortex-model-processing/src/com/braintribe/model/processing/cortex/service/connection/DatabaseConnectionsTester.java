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
package com.braintribe.model.processing.cortex.service.connection;

import java.util.List;

import com.braintribe.model.check.service.jdbc.SelectedDatabaseConnectionCheck;
import com.braintribe.model.cortexapi.connection.TestDatabaseConnections;
import com.braintribe.model.deployment.database.connector.DatabaseConnectionDescriptor;
import com.braintribe.model.deployment.database.pool.ConfiguredDatabaseConnectionPool;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.check.api.ParameterizedAccessCheckProcessor;

public class DatabaseConnectionsTester extends AbstractConnectionTester<SelectedDatabaseConnectionCheck> {

	private List<DatabaseConnectionPool> connectionPools;
	
	
	public DatabaseConnectionsTester(AccessRequestContext<TestDatabaseConnections> context, ParameterizedAccessCheckProcessor<SelectedDatabaseConnectionCheck> checkProcessor) {
		super(context,checkProcessor);
		this.connectionPools = context.getSystemRequest().getConnectionPools();
		
	}
	
	@Override
	protected SelectedDatabaseConnectionCheck createCheckRequest() {
		
		for (DatabaseConnectionPool cp : connectionPools) {
			if (!validateConnectionPool(cp)) {
				return null;
			}
		}
		
		
		SelectedDatabaseConnectionCheck check = SelectedDatabaseConnectionCheck.T.create();
		check.setDatabaseConnectionPoolList(connectionPools);
		return check;
	}
	
	private boolean validateConnectionPool(DatabaseConnectionPool connectionPool) {
		if (connectionPool instanceof ConfiguredDatabaseConnectionPool) {
			DatabaseConnectionDescriptor connectionDescriptor = ((ConfiguredDatabaseConnectionPool) connectionPool).getConnectionDescriptor();
			if (connectionDescriptor == null) {
				notifyError("No connection descriptor provided for connectionPool: "+connectionPool.getExternalId());
				return false;
			}
		}
		return true;
	}

}
