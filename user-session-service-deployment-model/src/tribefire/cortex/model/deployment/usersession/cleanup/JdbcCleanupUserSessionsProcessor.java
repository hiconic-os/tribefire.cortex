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
package tribefire.cortex.model.deployment.usersession.cleanup;

import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * {@link CleanupUserSessionsProcessor} that based on JDBC, i.e. an optimized implementation to go alongside a JDBC based user sessions access
 * (currently that means that one is a HibernateAccess).
 * <p>
 * The implementation assumes the DB schema already exists and does not create one.
 * 
 * @author peter.gazdik
 */
public interface JdbcCleanupUserSessionsProcessor extends CleanupUserSessionsProcessor {

	EntityType<JdbcCleanupUserSessionsProcessor> T = EntityTypes.T(JdbcCleanupUserSessionsProcessor.class);

	DatabaseConnectionPool getConnectionPool();
	void setConnectionPool(DatabaseConnectionPool connectionPool);

}
