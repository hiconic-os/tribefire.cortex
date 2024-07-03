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
package tribefire.platform.impl.model;

import java.util.Objects;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.modelnotification.InternalModelNotificationRequest;
import com.braintribe.model.modelnotification.InternalOnModelChanged;
import com.braintribe.model.modelnotification.ModelNotificationRequest;
import com.braintribe.model.modelnotification.ModelNotificationResponse;
import com.braintribe.model.modelnotification.OnModelChanged;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.service.api.MulticastRequest;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * <p>
 * A {@link ServiceProcessor} which broadcasts the incoming {@link OnModelChanged} signal to all available cartridge nodes.
 * 
 */
public class ModelNotificationProcessor extends AbstractDispatchingServiceProcessor<ModelNotificationRequest, ModelNotificationResponse> {

	// constants
	private static final Logger log = Logger.getLogger(ModelNotificationProcessor.class);

	@Override
	protected void configureDispatching(DispatchConfiguration<ModelNotificationRequest, ModelNotificationResponse> dispatching) {
		dispatching.register(OnModelChanged.T, this::onModelChange);
	}

	private ModelNotificationResponse onModelChange(ServiceRequestContext requestContext, OnModelChanged request) {

		String modelName = request.getModelName();

		Objects.requireNonNull(modelName, "Model name must not be null");

		InternalOnModelChanged internalRequest = InternalOnModelChanged.T.create();
		internalRequest.setModelName(modelName);

		notifyChange(requestContext, internalRequest);

		log.debug(() -> "Notified change on " + modelName);

		return ModelNotificationResponse.T.create();

	}

	private void notifyChange(Evaluator<ServiceRequest> requestEvaluator, InternalModelNotificationRequest request) {
		MulticastRequest multiRequest = MulticastRequest.T.create();
		multiRequest.setServiceRequest(request);
		multiRequest.eval(requestEvaluator).get();

		log.trace(() -> "Notified " + request);
	}

}
