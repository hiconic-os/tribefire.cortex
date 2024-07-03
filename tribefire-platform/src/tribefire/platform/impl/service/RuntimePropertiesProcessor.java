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
package tribefire.platform.impl.service;

import java.util.Map;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.common.lcd.Numbers;
import com.braintribe.logging.Logger;
import com.braintribe.model.cortexapi.service.MulticastScope;
import com.braintribe.model.cortexapi.service.SetRuntimeProperty;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.common.FailureCodec;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.MulticastRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.service.api.result.MulticastResponse;
import com.braintribe.model.service.api.result.ResponseEnvelope;
import com.braintribe.model.service.api.result.ServiceResult;

public class RuntimePropertiesProcessor implements ServiceProcessor<SetRuntimeProperty, String>{

	private static Logger logger = Logger.getLogger(RuntimePropertiesProcessor.class);

	protected Evaluator<ServiceRequest> requestEvaluator;

	@Override
	public String process(ServiceRequestContext requestContext, SetRuntimeProperty request) {

		String sessionId = requestContext.getRequestorSessionId();

		MulticastScope multicastScope = request.getMulticastScope();
		if (multicastScope == null) {
			multicastScope = MulticastScope.none;
		}
		
		if (multicastScope != MulticastScope.none) {

			request.setMulticastScope(MulticastScope.none);
			
			MulticastRequest mcR = MulticastRequest.T.create();
			mcR.setAsynchronous(false);
			mcR.setServiceRequest(request);
			mcR.setTimeout((long) Numbers.MILLISECONDS_PER_SECOND * 10);
			mcR.setSessionId(sessionId);
			if (multicastScope == MulticastScope.masters) {
				mcR.setAddressee(InstanceId.of(null, TribefireConstants.TRIBEFIRE_SERVICES_APPLICATION_ID));
			}
			EvalContext<? extends MulticastResponse> eval = mcR.eval(requestEvaluator);
			MulticastResponse multicastResponse = eval.get();

			String oldValue = null;
			
			for (Map.Entry<InstanceId,ServiceResult>  entry : multicastResponse.getResponses().entrySet()) {

				InstanceId instanceId = entry.getKey();

				logger.debug(() -> "Received a response from instance: "+instanceId);

				ServiceResult result = entry.getValue();
				if (result instanceof Failure) {
					Throwable throwable = FailureCodec.INSTANCE.decode(result.asFailure());
					logger.error("Received failure from "+instanceId, throwable);
				} else if (result instanceof ResponseEnvelope) {

					ResponseEnvelope envelope = (ResponseEnvelope) result;
					String responseValue = (String) envelope.getResult();
					if (responseValue != null) {
						oldValue = responseValue;
					}

				} else {
					logger.error("Unsupported response type: "+result);
				}

			}

			return oldValue;

		} else {

			String requestorSessionId = requestContext.getRequestorSessionId();
			String name = requestContext.getRequestorUserName();

			String key = request.getKey();
			String newValue = request.getValue();

			if (logger.isDebugEnabled()) logger.debug("Got a request to set "+key+" to value "+newValue+" from user "+name+" in session "+requestorSessionId);

			String oldValue = TribefireRuntime.setProperty(key, newValue);

			return oldValue;

		}
	}

	@Configurable
	@Required
	public void setRequestEvaluator(Evaluator<ServiceRequest> requestEvaluator) {
		this.requestEvaluator = requestEvaluator;
	}

}
