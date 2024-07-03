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
package com.braintribe.model.check.service;

import java.util.List;

import com.braintribe.model.accessapi.AccessRequest;
import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.StandardRequest;

public interface CompositeCheck extends StandardRequest, AccessRequest {

	EntityType<CompositeCheck> T = EntityTypes.T(CompositeCheck.class);

	void setCheckProcessors(List<CheckProcessor> checkProcessors);
	List<CheckProcessor> getCheckProcessors();

	void setParameterizedChecks(List<ParameterizedCheckRequest> parameterizedChecks);
	List<ParameterizedCheckRequest> getParameterizedChecks();

	@Initializer("true")
	boolean getParallelize();
	void setParallelize(boolean parallelize);

	@Override
	EvalContext<? extends CompositeCheckResult> eval(Evaluator<ServiceRequest> evaluator);
	
	@Override
	void setDomainId(String domainId);
	@Override
	@Initializer("'cortex'")
	String getDomainId();
}
