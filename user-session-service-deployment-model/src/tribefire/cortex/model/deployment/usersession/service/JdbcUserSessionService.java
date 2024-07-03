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
package tribefire.cortex.model.deployment.usersession.service;

import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * {@link UserSessionService} that based on JDBC, i.e. an optimized implementation to go alongside a JDBC based user sessions access (currently that
 * means that one is a HibernateAccess).
 * <p>
 * The implementation assumes the DB schema already exists and does not create one. Moreover, there is one table which represents the
 * <tt>PersistenceUserSession</tt> entity, named:<br>
 * TF_US_PERSISTENCE_USER_SESSION<br>
 * and its properties are stored in the following columns, which are all strings:<br>
 * ID<br>
 * USER_NAME<br>
 * USER_FIRST_NAME<br>
 * USER_LAST_NAME<br>
 * USER_EMAIL<br>
 * CREATION_DATE<br>
 * FIXED_EXPIRY_DATE<br>
 * EXPIRY_DATE<br>
 * LAST_ACCESSED_DATE<br>
 * MAX_IDLE_TIME<br>
 * EFFECTIVE_ROLES<br>
 * SESSION_TYPE<br>
 * CREATION_INTERNET_ADDRESS<br>
 * CREATION_NODE_ID<br>
 * PROPERTIES<br>
 * 
 * @author peter.gazdik
 */
public interface JdbcUserSessionService extends UserSessionService {

	EntityType<JdbcUserSessionService> T = EntityTypes.T(JdbcUserSessionService.class);

	DatabaseConnectionPool getConnectionPool();
	void setConnectionPool(DatabaseConnectionPool connectionPool);

}
