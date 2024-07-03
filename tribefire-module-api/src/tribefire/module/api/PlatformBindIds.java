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
package tribefire.module.api;

import static com.braintribe.utils.lcd.CollectionTools2.asLinkedSet;

import java.util.Set;

/**
 * Simply a list of constants of known Tribefire Platform bindIds for various system components (which end up being configured on
 * CortexConfiguration).
 * 
 * @author peter.gazdik
 */
public interface PlatformBindIds {

	// Vitals
	String TRIBEFIRE_LOCKING_BIND_ID = "tribefire-locking";
	String TRIBEFIRE_LOCK_DB_BIND_ID = "tribefire-locking-db";
	String TRIBEFIRE_LOCK_MANAGER_BIND_ID = "tribefire-lock-manager";
	String TRIBEFIRE_MQ_BIND_ID = "tribefire-mq";

	// Accesses
	String AUTH_DB_BIND_ID = "tribefire-auth-db";
	String USER_SESSIONS_DB_BIND_ID = "tribefire-user-sessions-db";
	String USER_STATISTICS_DB_BIND_ID = "tribefire-user-statistics-db";
	String TRANSIENT_MESSAGING_DATA_DB_BIND_ID = "tribefire-transient-messaging-data-db";

	// Other
	String RESOURCES_DB = "tribefire-resources-db";

	static boolean isPlatformBindId(String bindId) {
		return platformBindIds().contains(bindId);
	}

	static Set<String> platformBindIds() {
		return asLinkedSet( //
				TRIBEFIRE_LOCKING_BIND_ID, //
				TRIBEFIRE_LOCK_DB_BIND_ID, //
				TRIBEFIRE_LOCK_MANAGER_BIND_ID, //
				TRIBEFIRE_MQ_BIND_ID, //

				AUTH_DB_BIND_ID, //
				USER_SESSIONS_DB_BIND_ID, //
				USER_STATISTICS_DB_BIND_ID, //
				TRANSIENT_MESSAGING_DATA_DB_BIND_ID, //
				
				RESOURCES_DB //
		);
	}

}
