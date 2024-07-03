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
package com.braintribe.model.deployment.database.connector;

import java.util.Map;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface MssqlConnectionDescriptor extends DatabaseConnectionDescriptor {

	EntityType<MssqlConnectionDescriptor> T = EntityTypes.T(MssqlConnectionDescriptor.class);

	String getHost();
	void setHost(String host);

	@Initializer("1433")
	Integer getPort();
	void setPort(Integer port);

	String getDatabase();
	void setDatabase(String database);

	String getInstance();
	void setInstance(String instance);

	MssqlVersion getVersion();
	void setVersion(MssqlVersion version);

	@Initializer("enum(com.braintribe.model.deployment.database.connector.MssqlDriver,MicrosoftJdbc4Driver)")
	MssqlDriver getDriver();
	void setDriver(MssqlDriver driver);

	Map<String, String> getProperties();
	void setProperties(Map<String, String> properties);

	@Override
	default String describeConnection() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHost());
		if (getPort() != null)
			sb.append(":" + getPort());
		return sb.toString();
	}
}
