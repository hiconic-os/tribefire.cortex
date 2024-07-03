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
package com.braintribe.model.processing.check;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.check.service.CheckRequest;
import com.braintribe.model.check.service.CheckResult;
import com.braintribe.model.check.service.CheckResultEntry;
import com.braintribe.model.check.service.CheckStatus;
import com.braintribe.model.check.service.CompositeCheck;
import com.braintribe.model.check.service.CompositeCheckResult;
import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.service.api.CompositeRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.CompositeResponse;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.service.api.result.ResponseEnvelope;
import com.braintribe.model.service.api.result.ServiceResult;

public class CompositeCheckProcessor implements ServiceProcessor<CompositeCheck,CompositeCheckResult> {

	@Override
	public CompositeCheckResult process(ServiceRequestContext requestContext, CompositeCheck request) {
		
		List<ServiceRequest> requests = new ArrayList<>();
		for (CheckProcessor checkProcessor : request.getCheckProcessors()) {
			CheckRequest cr = CheckRequest.T.create();
			cr.setServiceId(checkProcessor.getExternalId());
			requests.add(cr);
		}
		requests.addAll(request.getParameterizedChecks());
		
		CompositeRequest compositeRequest = CompositeRequest.T.create();
		compositeRequest.setRequests(requests);
		compositeRequest.setContinueOnFailure(true);
		compositeRequest.setParallelize(request.getParallelize());
		CompositeResponse compositeResult = compositeRequest.eval(requestContext).get();
		
		CompositeCheckResult result = CompositeCheckResult.T.create();
		
		List<ServiceResult> results = compositeResult.getResults();
		int i = 0;
		for (; i<results.size(); ++i) {

			CheckResult checkResult = null;
			CheckResultEntry cre = null;
			
			ServiceResult serviceResult = results.get(i);
			switch(serviceResult.resultType()) {
				case failure:
					Failure failure = (Failure) serviceResult;
					checkResult = CheckResult.T.create();
					cre = CheckResultEntry.T.create();
					cre.setCheckStatus(CheckStatus.fail);
					cre.setMessage(failure.getMessage());
					checkResult.getEntries().add(cre);
					break;
				case success:
					ResponseEnvelope ssr = (ResponseEnvelope) serviceResult;
					checkResult = (CheckResult) ssr.getResult();
					break;
				default:
					checkResult = CheckResult.T.create();
					cre = CheckResultEntry.T.create();
					cre.setCheckStatus(CheckStatus.fail);
					cre.setMessage("Unexpected service result type "+serviceResult.resultType());
					checkResult.getEntries().add(cre);
			}

			if (i < request.getCheckProcessors().size()) {
				result.getCheckProcessorResults().add(checkResult);
			} else {
				result.getParameterizedCheckResults().add(checkResult);
			}
		}
		
		return result;
	}

}
