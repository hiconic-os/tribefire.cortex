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
package tribefire.cortex.db_connection_check_standard_initializer.wire.space;

import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.model.checkdeployment.db.DbConnectionCheckProcessor;
import tribefire.module.api.InitializerBindingBuilder;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * Configures a
 */
@Managed
public class DbConnectionCheckStandardInitializerSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Override
	public void bindInitializers(InitializerBindingBuilder bindings) {
		bindings.bind(this::createStandardDbConnectionCheckProcessor);
	}

	private void createStandardDbConnectionCheckProcessor(PersistenceInitializationContext ctx) {
		String externalId = "checkProcessor.dbConnection.standard";

		DbConnectionCheckProcessor cp = ctx.getSession().create(DbConnectionCheckProcessor.T, "initializer:" + externalId);
		cp.setExternalId(externalId);
		cp.setName("Standard Db Connection Check Processor");
	}

}
