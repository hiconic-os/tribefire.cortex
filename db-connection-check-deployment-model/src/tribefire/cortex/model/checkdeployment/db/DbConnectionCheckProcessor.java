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
package tribefire.cortex.model.checkdeployment.db;

import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface DbConnectionCheckProcessor extends CheckProcessor {

	EntityType<DbConnectionCheckProcessor> T = EntityTypes.T(DbConnectionCheckProcessor.class);

	/**
	 * A pattern used when querying for DB Connection pools to check.
	 * <p>
	 * This is NOT A REGULAR EXPRESSION pattern, but rather a pattern used directly in the like condition, matching the externalId property.
	 * <p>
	 * If the value is <tt>null</tt>, every DB Connection is matched.
	 */
	String getDbConnectionExternalidPattern();
	void setDbConnectionExternalidPattern(String pattern);

}
