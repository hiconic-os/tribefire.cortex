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
package tribefire.module.wire.contract;

import javax.sql.DataSource;

import com.braintribe.model.deployment.database.pool.DatabaseConnectionInfoProvider;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.processing.deployment.api.ComponentBinder;

import tribefire.cortex.model.deployment.mimetypedetection.MimeTypeDetector;

/**
 * Aggregation of all {@link ComponentBinder}s available on the {@link TribefireWebPlatformContract web platform}.
 */
public interface WebPlatformBindersContract extends //
		AccessBindersContract, //
		CheckBindersContract, //
		ClusterBindersContract, //
		MarshallingBindersContract, //
		ResourceProcessingBindersContract, //
		ServiceBindersContract, //
		ServletBindersContract, //
		StateProcessingBindersContract, //
		WorkerBindersContract {

	ComponentBinder<MimeTypeDetector, com.braintribe.mimetype.MimeTypeDetector> mimeTypeDetector();

	ComponentBinder<DatabaseConnectionPool, DataSource> databaseConnectionPool();

	ComponentBinder<DatabaseConnectionInfoProvider, tribefire.module.api.DatabaseConnectionInfoProvider> databaseConnectionInfoProvider();

}
