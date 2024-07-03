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
package tribefire.cortex.model.lockingdeployment.db;

import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.lockingdeployment.LockManager;

@Deprecated
public interface DbLockManager extends LockManager {

	EntityType<DbLockManager> T = EntityTypes.T(DbLockManager.class);

	@Mandatory
	DatabaseConnectionPool getDatabaseConnection();
	void setDatabaseConnection(DatabaseConnectionPool databaseConnection);

	@Initializer("true")
	boolean getAutoUpdateSchema();
	void setAutoUpdateSchema(boolean autoUpdateSchema);

	@Initializer("100")
	int getPollIntervalInMillis();
	void setPollIntervalInMillis(int pollIntervalInMillis);

	@Initializer("5000L")
	long getTopicExpirationInMillis();
	void setTopicExpirationInMillis(long topicExpirationInMillis);

	Long getLockTtlInMillis();
	void setLockTtlInMillis(Long lockTtlInMillis);

}
