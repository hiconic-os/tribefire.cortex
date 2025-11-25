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
package com.braintribe.model.plugin.jdbc;

import com.braintribe.model.csa.PlugableDcsaSharedStorage;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.generic.annotation.meta.Confidential;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface JdbcPlugableDcsaSharedStorage extends PlugableDcsaSharedStorage {

	EntityType<JdbcPlugableDcsaSharedStorage> T = EntityTypes.T(JdbcPlugableDcsaSharedStorage.class);

	void setProject(String project);
	String getProject();

	void setUrl(String url);
	String getUrl();

	void setDriver(String driver);
	String getDriver();

	void setUsername(String username);
	String getUsername();

	@Confidential
	void setPassword(String password);
	String getPassword();

	void setAutoUpdateSchema(Boolean autoUpdateSchema);
	Boolean getAutoUpdateSchema();

	void setDatabaseConnectionPool(DatabaseConnectionPool databaseConnectionPool);
	DatabaseConnectionPool getDatabaseConnectionPool();

	void setParallelFetchThreads(Integer parallelFetchThreads);
	Integer getParallelFetchThreads();

}
