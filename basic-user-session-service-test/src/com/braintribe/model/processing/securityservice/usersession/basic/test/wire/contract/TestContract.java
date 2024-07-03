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
package com.braintribe.model.processing.securityservice.usersession.basic.test.wire.contract;

import com.braintribe.common.db.wire.contract.DbTestDataSourcesContract;
import com.braintribe.model.processing.securityservice.api.UserSessionService;
import com.braintribe.model.processing.securityservice.usersession.basic.test.common.TestConfig;
import com.braintribe.model.processing.securityservice.usersession.basic.test.wire.space.AccessBasedTestSpace;
import com.braintribe.model.processing.securityservice.usersession.basic.test.wire.space.DbBasedTestSpace;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.space.WireSpace;

public interface TestContract extends WireSpace {

	static WireContext<TestContract> context(boolean useRelationalDb) {
		return useRelationalDb ? dbBasedContext() : accessBasedContext();
	}

	static WireContext<TestContract> dbBasedContext() {
		return Wire.context(TestContract.class) //
				.bindContract(TestContract.class, DbBasedTestSpace.class) //
				.bindContracts(TestContract.class)
				// This doesn't work for whatever reason
				// .bindContract(DbTestDataSourcesContract.class, DbTestDataSourcesSpace.class) //
				.bindContracts(DbTestDataSourcesContract.class).build();
	}

	static WireContext<TestContract> accessBasedContext() {
		return Wire.context(TestContract.class) //
				.bindContract(TestContract.class, AccessBasedTestSpace.class) //
				// TODO, why TF is this needed?
				.bindContracts(TestContract.class).build();
	}

	TestConfig testConfig();

	UserSessionService userSessionService();
}
