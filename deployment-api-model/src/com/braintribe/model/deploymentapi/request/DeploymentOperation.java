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
package com.braintribe.model.deploymentapi.request;

import java.util.Set;

import com.braintribe.model.deploymentapi.response.DeploymentResponse;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

@Abstract
public interface DeploymentOperation extends DeploymentRequest {

	EntityType<DeploymentOperation> T = EntityTypes.T(DeploymentOperation.class);

	@Mandatory
	@Description("A list of externalIds that identifies the deployables for this deployment operation.")
	Set<String> getExternalIds();
	void setExternalIds(Set<String> value);

	@Description("Optional deployment mode.")
	DeploymentMode getMode();
	void setMode(DeploymentMode value);

	@Override
	EvalContext<? extends DeploymentResponse> eval(Evaluator<ServiceRequest> evaluator);

}
