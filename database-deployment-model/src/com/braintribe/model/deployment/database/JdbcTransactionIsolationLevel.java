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
package com.braintribe.model.deployment.database;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * Enum constants corresponding to values in {@link java.sql.Connection}.
 * 
 * @author peter.gazdik
 */
public enum JdbcTransactionIsolationLevel implements EnumBase<JdbcTransactionIsolationLevel> {

	NONE,
	READ_UNCOMMITTED,
	READ_COMMITTED,
	REPEATABLE_READ,
	SERIALIZABLE;

	public static final EnumType<JdbcTransactionIsolationLevel> T = EnumTypes.T(JdbcTransactionIsolationLevel.class);
	
	@Override
	public EnumType<JdbcTransactionIsolationLevel> type() {
		return T;
	}	
}
