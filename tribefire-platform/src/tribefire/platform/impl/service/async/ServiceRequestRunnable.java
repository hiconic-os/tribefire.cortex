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
package tribefire.platform.impl.service.async;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.AsynchronousRequest;
import com.braintribe.model.service.api.ServiceRequest;

public class ServiceRequestRunnable implements Runnable {

	private static final Logger logger = Logger.getLogger(ServiceRequestRunnable.class);
	
	private AsynchronousRequest asyncRequest;
	private Evaluator<ServiceRequest> requestEvaluator;
	protected String correlationId;
	protected CallbackExpert callbackExpert;

	public ServiceRequestRunnable(String correlationId, AsynchronousRequest asyncRequest, Evaluator<ServiceRequest> requestEvaluator, CallbackExpert callbackExpert) {
		this.correlationId = correlationId;
		this.asyncRequest = asyncRequest;
		this.requestEvaluator = requestEvaluator;
		this.callbackExpert = callbackExpert;
	}
	
	@Override
	public void run() {
		ServiceRequest serviceRequest = asyncRequest.getServiceRequest();
		logger.trace(() -> "Asynchronous processing " + correlationId + " of " + serviceRequest.getClass().getName() + " has started");

		try {
			preFlight();
			Object result = serviceRequest.eval(requestEvaluator).get();
			onSuccess(result);
			doCallback(result, null);
		} catch (Throwable t) {
			onFailure(t);
			doCallback(null, t);
		}
	}
	
	protected void preFlight() {
		ServiceRequest serviceRequest = asyncRequest.getServiceRequest();
		logger.trace(() -> "Asynchronous processing " + correlationId + " of " + serviceRequest.getClass().getName() + " starting.");
	}
	protected void onSuccess(Object result) {
		ServiceRequest serviceRequest = asyncRequest.getServiceRequest();
		logger.trace(() -> "Asynchronous processing " + correlationId + " of " + serviceRequest.getClass().getName() + " completed. Result: " + result);
	}
	protected void doCallback(Object result, Throwable t) {
		callbackExpert.doCallback(asyncRequest, result, t);
	}

	protected void onFailure(Throwable t) {
		ServiceRequest serviceRequest = asyncRequest.getServiceRequest();
		logger.error("Asynchronous processing " + correlationId + " of " + serviceRequest.getClass().getName() + " failed with " + t, t);
	}
	
	public AsynchronousRequest getAsyncRequest() {
		return asyncRequest;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	
}
