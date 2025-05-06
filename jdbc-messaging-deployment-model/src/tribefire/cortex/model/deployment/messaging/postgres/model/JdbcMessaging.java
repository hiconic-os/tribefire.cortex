// ============================================================================
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
package tribefire.cortex.model.deployment.messaging.postgres.model;

import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.messagingdeployment.Messaging;

public interface JdbcMessaging extends Messaging {

	final EntityType<JdbcMessaging> T = EntityTypes.T(JdbcMessaging.class);

	/**
	 * WARNING: the implementation only supports PostgreSQL.
	 */
	@Mandatory
	DatabaseConnectionPool getConnectionPool();
	void setConnectionPool(DatabaseConnectionPool connectionPool);

	/**
	 * Prefix for tables, indices, triggers, functions created for this messaging.
	 * <p>
	 * Note that internally it expands the prefix with "_msg_", so that for sql prefix "hc" the table names will be "hc_msg_topic" and "hc_msg_queue".
	 */
	@Initializer("'hc'")
	String getSqlPrefix();
	void setSqlPrefix(String sqlPrefix);

}
