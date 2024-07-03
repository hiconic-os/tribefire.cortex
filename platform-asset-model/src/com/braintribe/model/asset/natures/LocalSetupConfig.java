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
package com.braintribe.model.asset.natures;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.FolderName;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface LocalSetupConfig extends GenericEntity {

	EntityType<LocalSetupConfig> T = EntityTypes.T(LocalSetupConfig.class);

	String installationPath = "installationPath";
	String htttpPort = "htttpPort";
	String httpsPort = "httpsPort";
	String tempDir = "tempDir";
	String checkWriteAccessForDirs = "checkWriteAccessForDirs";

	@Description("The directory in which the local setup is placed")
	@FolderName
	String getInstallationPath();
	void setInstallationPath(String installationPath);

	@Description("The port where the server listens for HTTP requests")
	@Initializer("8080")
	Integer getHttpPort();
	void setHttpPort(Integer httpPort);

	@Description("The port where the server listens for secured HTTP requests")
	@Initializer("8443")
	Integer getHttpsPort();
	void setHttpsPort(Integer httpsPort);

	@Description("The directory for temporary files of the JVM (controlled by system property java.io.tmpdir)")
	@FolderName
	String getTempDir();
	void setTempDir(String tempDir);

	@Description("A list of directory paths to be checked for write access. If a directory does not exist, it will be created.")
	@FolderName
	List<String> getCheckWriteAccessForDirs();
	void setCheckWriteAccessForDirs(List<String> checkWriteAccessForDirs);

}
