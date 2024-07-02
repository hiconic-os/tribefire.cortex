// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.platformreflection.request;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformreflection.DiagnosticPackage;
import com.braintribe.model.service.api.ServiceRequest;

public interface GetDiagnosticPackage extends PlatformReflectionRequest {

	EntityType<GetDiagnosticPackage> T = EntityTypes.T(GetDiagnosticPackage.class);

	Boolean getIncludeHeapDump();
	void setIncludeHeapDump(Boolean includeHeapDump);

	Boolean getIncludeLogs();
	void setIncludeLogs(Boolean includeLogs);

	@Description("If set to 'true', the response will omit binary data from the DCSA, i.e. it will exclude all CsaStoreResource operations.")
	boolean getExcludeSharedStorageBinaries();
	void setExcludeSharedStorageBinaries(boolean excludeSharedStorageBinaries);

	@Initializer("true")
	boolean getPasswordProtection();
	void setPasswordProtection(boolean passwordProtection);

	@Initializer("450000l") // 7,5 min = 75% of the default CollectDiagnosticPackages timeout
	@Name("Wait Timeout (ms)")
	@Description("The maximum amount of the time (in milliseconds) the server should wait for the inidivual parts of a single diagnostic package to collect. By default, the wait timeout is 9 min.")
	Long getWaitTimeoutInMs();
	void setWaitTimeoutInMs(Long waitTimeoutInMs);

	@Override
	EvalContext<? extends DiagnosticPackage> eval(Evaluator<ServiceRequest> evaluator);
}
