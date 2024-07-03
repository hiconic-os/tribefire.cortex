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

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface OracleConnectionDescriptor extends DatabaseConnectionDescriptor {

	EntityType<OracleConnectionDescriptor> T = EntityTypes.T(OracleConnectionDescriptor.class);

	String getSid();
	void setSid(String service);

	String getServiceName();
	void setServiceName(String service);

	OracleVersion getVersion();
	void setVersion(OracleVersion version);

	String getHost();
	void setHost(String host);

	@Initializer("1521")
	Integer getPort();
	void setPort(Integer port);

	@Override
	default String describeConnection() {
		StringBuilder sb = new StringBuilder();
		if (getHost() != null) {
			sb.append(getHost());
			if (getPort() != null) {
				sb.append(":" + getPort());
			}
		}
		String serviceName = getServiceName();
		if (serviceName != null && !serviceName.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("Service: " + serviceName);
		}
		String sid = getSid();
		if (sid != null && !sid.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("SID: " + sid);
		}
		return sb.toString();
	}
}
