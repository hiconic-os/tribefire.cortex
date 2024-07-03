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

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.logging.Logger.LogLevel;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.service.api.AsynchronousRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.AsynchronousResponse;
import com.braintribe.thread.api.ThreadContextScoping;

/**
 * A {@link ServiceProcessor} which processes the {@link ServiceRequest}(s) wrapped by the incoming
 * {@link AsynchronousRequest}(s) asynchronously.
 * 
 */
public class AsynchronousServiceProcessor implements ServiceProcessor<AsynchronousRequest, AsynchronousResponse> {

	private static final Logger log = Logger.getLogger(AsynchronousServiceProcessor.class);

	// configurable
	private ExecutorService executorService;
	private LogLevel swallowedExceptionsLogLevel = LogLevel.ERROR;
	protected ThreadContextScoping threadScoping;

	@Required
	@Configurable
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Configurable
	public void setSwallowedExceptionsLogLevel(LogLevel swallowedExceptionsLogLevel) {
		this.swallowedExceptionsLogLevel = swallowedExceptionsLogLevel;
	}

	@Required
	@Configurable
	public void setThreadScoping(ThreadContextScoping threadScoping) {
		this.threadScoping = threadScoping;
	}

	@Override
	public AsynchronousResponse process(ServiceRequestContext requestContext, AsynchronousRequest request) {

		Objects.requireNonNull(requestContext, "requestContext");
		Objects.requireNonNull(request, "request");

		ServiceRequest payload = request.getServiceRequest();

		if (payload == null) {
			throw new IllegalArgumentException("The incoming " + AsynchronousRequest.class.getSimpleName() + " has no service request set.");
		}

		String correlationId = UUID.randomUUID().toString();

		AsynchronousResponse response = AsynchronousResponse.T.create();
		response.setCorrelationId(correlationId);

		Callable<Void> callable = () -> {

			log.trace(() -> "Asynchronous processing " + correlationId + " of " + payload.getClass().getName() + " has started");

			try {
				Object result = payload.eval(requestContext).get();

				log.trace(() -> "Asynchronous processing " + correlationId + " of " + payload.getClass().getName() + " completed. Result: " + result);

			} catch (Throwable t) {
				log.log(swallowedExceptionsLogLevel,
						"Asynchronous processing " + correlationId + " of " + payload.getClass().getName() + " failed with " + t, t);
			}

			return null;

		};

		callable = threadScoping.bindContext(callable);

		log.trace(() -> "Asynchronous processing " + correlationId + " of " + payload.getClass().getName() + " will be submitted");

		executorService.submit(callable);

		log.trace(() -> "Asynchronous processing " + correlationId + " of " + payload.getClass().getName() + " was submitted");

		return response;

	}

}
