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
package com.braintribe.model.execution.persistence.cleanup;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.execution.persistence.ExecutionPersistenceRequest;
import com.braintribe.model.execution.persistence.ExecutionPersistenceResponse;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

public interface CleanUpJobs extends ExecutionPersistenceRequest {

	final EntityType<CleanUpJobs> T = EntityTypes.T(CleanUpJobs.class);

	String cleanAll = "cleanAll";

	void setCleanAll(boolean cleanAll);
	@Initializer("false")
	boolean getCleanAll();
	
	void setAccess(IncrementalAccess access);
	IncrementalAccess getAccess();

	void setMaxAge(Long maxAge);
	Long getMaxAge();
	
	@Override
	EvalContext<ExecutionPersistenceResponse> eval(Evaluator<ServiceRequest> evaluator);

}
