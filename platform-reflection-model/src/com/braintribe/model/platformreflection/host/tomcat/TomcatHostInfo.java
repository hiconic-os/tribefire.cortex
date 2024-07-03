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
package com.braintribe.model.platformreflection.host.tomcat;

import java.util.List;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformreflection.HostInfo;


public interface TomcatHostInfo extends HostInfo {

	EntityType<TomcatHostInfo> T = EntityTypes.T(TomcatHostInfo.class);
	
	List<Connector> getConnectors();
	void setConnectors(List<Connector> connectors);
	
	String getHostBasePath();
	void setHostBasePath(String hostBasePath);
	
	Engine getEngine();
	void setEngine(Engine engine);

	ThreadPools getThreadPools();
	void setThreadPools(ThreadPools threadPools);
	
	List<Ssl> getSsl();
	void setSsl(List<Ssl> ssl);
	
}
