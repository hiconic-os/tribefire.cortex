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
package com.braintribe.model.platformreflection;

import java.util.Map;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformreflection.request.PlatformReflectionResponse;
import com.braintribe.model.platformreflection.streampipes.StreamPipesInfo;
import com.braintribe.model.platformreflection.tf.ModuleAssets;
import com.braintribe.model.platformreflection.tf.SetupAssets;
import com.braintribe.model.platformreflection.tf.TribefireServicesInfo;

public interface TribefireInfo extends PlatformReflectionResponse {

	EntityType<TribefireInfo> T = EntityTypes.T(TribefireInfo.class);

	void setServicesInfo(TribefireServicesInfo servicesInfo);
	TribefireServicesInfo getServicesInfo();

	void setTribefireRuntimeProperties(Map<String, String> tribefireRuntimeProperties);
	Map<String, String> getTribefireRuntimeProperties();

	SetupAssets getSetupAssets();
	void setSetupAssets(SetupAssets setupAssets);

	ModuleAssets getModuleAssets();
	void setModuleAssets(ModuleAssets modulesAssets);

	StreamPipesInfo getStreamPipeInfo();
	void setStreamPipeInfo(StreamPipesInfo streamPipesInfo);

	FolderInfo getTempDirInfo();
	void setTempDirInfo(FolderInfo folderInfo);

	// TODO: Sessions, active users
	// TODO: https://github.com/brettwooldridge/HikariCP/wiki/MBean-(JMX)-Monitoring-and-Management
	// TODO: http://www.mchange.com/projects/c3p0/#jmx_configuration_and_management
	// https://amemon.wordpress.com/2007/07/15/monitoring-c3p0-using-jmxjconsole/
}
