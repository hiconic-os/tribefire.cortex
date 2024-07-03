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
package com.braintribe.model.processing.platformreflection.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.braintribe.model.platformreflection.Healthz;
import com.braintribe.model.platformreflection.PackagingInformation;
import com.braintribe.model.platformreflection.tf.DeployablesInfo;
import com.braintribe.model.resource.Resource;

public class DiagnosticPackageContext {

	protected String threadDump;
	protected String platformReflectionJson;
	protected String hotThreads;
	protected Healthz healthz;
	protected String processesJson;
	protected File heapDump;
	protected String heapDumpFilename;
	protected File logs;
	protected String logsFilename;
	protected PackagingInformation packagingInformation;
	protected DeployablesInfo deployablesInfo;
	protected Resource setupDescriptorResource;
	protected File configurationFolderAsZip;
	protected String configurationFolderAsZipFilename;
	protected File modulesFolderAsZip;
	protected String modulesFolderAsZipFilename;
	protected File sharedStorageAsZip;
	protected String sharedStorageAsZipFilename;
	protected File accessDataFolderAsZip;
	protected String accessDataFolderAsZipFilename;
	protected String setupAssetsAsJson;
	protected List<String> errors = Collections.synchronizedList(new ArrayList<>());

}
