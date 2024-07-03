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
package com.braintribe.model.deploymentreflection.request;

import com.braintribe.model.deploymentreflection.DeploymentSummary;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.ServiceRequest;

@Description("Returns a summary of the internal status of tribefire deployables.")
public interface GetDeploymentSummary extends DeploymentReflectionRequest {

	EntityType<GetDeploymentSummary> T = EntityTypes.T(GetDeploymentSummary.class);

	@Override
	EvalContext<? extends DeploymentSummary> eval(Evaluator<ServiceRequest> evaluator);

	UnitFilter getUnitFilter();
	void setUnitFilter(UnitFilter unitFilter);
	
	InstanceId getMulticastFilter();
	void setMulticastFilter(InstanceId instanceId);
	
}
