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
package tribefire.cortex.db_connection_check.wire.space;

import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.db_connection_check.processor.BasicDbConnectionCheckProcessor;
import tribefire.cortex.model.checkdeployment.db.DbConnectionCheckProcessor;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class DbConnectionCheckModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		bindings.bind(DbConnectionCheckProcessor.T) //
				.component(tfPlatform.binders() //
						.checkProcessor())
				.expertFactory(this::dbConnectionCheckProcessor);
	}

	private BasicDbConnectionCheckProcessor dbConnectionCheckProcessor(ExpertContext<DbConnectionCheckProcessor> ctx) {
		DbConnectionCheckProcessor deployable = ctx.getDeployable();

		BasicDbConnectionCheckProcessor bean = new BasicDbConnectionCheckProcessor();
		bean.setDbConnectionExternalidPattern(deployable.getDbConnectionExternalidPattern());
		bean.setCortexSessionSupplier(tfPlatform.systemUserRelated().cortexSessionSupplier());
		bean.setDeployedComponentResolver(tfPlatform.deployment().deployedComponentResolver());

		return bean;
	}

}
