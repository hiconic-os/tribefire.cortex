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



import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.check.service.jdbc.SelectedDatabaseConnectionCheck;
import com.braintribe.model.cortexapi.connection.TestConnectionRequest;
import com.braintribe.model.cortexapi.connection.TestConnectionResponse;
import com.braintribe.model.cortexapi.connection.TestDatabaseConnection;
import com.braintribe.model.cortexapi.connection.TestDatabaseConnections;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessors;
import com.braintribe.model.processing.check.api.ParameterizedAccessCheckProcessor;

public class TestConnectionRequestProcessor implements AccessRequestProcessor<TestConnectionRequest, TestConnectionResponse> {
	
	private ParameterizedAccessCheckProcessor<SelectedDatabaseConnectionCheck> databaseConnectionCheck;
	
	@Required
	@Configurable
	public void setDatabaseConnectionCheck(ParameterizedAccessCheckProcessor<SelectedDatabaseConnectionCheck> databaseConnectionCheck) {
		this.databaseConnectionCheck = databaseConnectionCheck;
	}
	
	
	private AccessRequestProcessor<TestConnectionRequest, TestConnectionResponse> dispatcher = AccessRequestProcessors.dispatcher(config->{
		config.register(TestDatabaseConnections.T, this::testDatabaseConnections);
		config.register(TestDatabaseConnection.T, this::testDatabaseConnection);
	});

	@Override
	public TestConnectionResponse process(AccessRequestContext<TestConnectionRequest> context) {
		return dispatcher.process(context);
	}

	public TestConnectionResponse testDatabaseConnections(AccessRequestContext<TestDatabaseConnections> context) {
		return new DatabaseConnectionsTester(context, databaseConnectionCheck).run(); 
	}

	public TestConnectionResponse testDatabaseConnection(AccessRequestContext<TestDatabaseConnection> context) {
		return new DatabaseConnectionTester(context, databaseConnectionCheck).run();
	}

	
	
	
	
}
