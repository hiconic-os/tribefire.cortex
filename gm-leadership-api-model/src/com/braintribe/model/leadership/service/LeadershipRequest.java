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
package com.braintribe.model.leadership.service;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.AuthorizedRequest;
import com.braintribe.model.service.api.DomainRequest;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;

@Abstract
public interface LeadershipRequest extends DomainRequest, AuthorizedRequest {

	EntityType<LeadershipRequest> T = EntityTypes.T(LeadershipRequest.class);

	String getCandidateId();
	void setCandidateId(String candidateId);
	
	InstanceId getInstanceId();
	void setInstanceId(InstanceId instanceId);
	
	InstanceId getOriginatorInstanceId();
	void setOriginatorInstanceId(InstanceId originatorInstanceId);

	@Override
	EvalContext<Neutral> eval(Evaluator<ServiceRequest> evaluator);

}
