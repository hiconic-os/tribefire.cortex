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
