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
package tribefire.platform.impl.multicast;

import com.braintribe.logging.Logger;
import com.braintribe.model.messaging.Message;
import com.braintribe.model.processing.service.common.ServiceResults;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.ServiceResult;
import com.braintribe.model.service.api.result.StillProcessing;

public class TestMulticastConsumer extends MulticastConsumer {

	// constants
	private static final Logger log = Logger.getLogger(TestMulticastConsumer.class);

	private boolean replyAfterKeepAlive;
	private Long delay;
	private volatile boolean closing = false;

	public void setReplyAfterKeepAlive(boolean replyAfterKeepAlive) {
		this.replyAfterKeepAlive = replyAfterKeepAlive;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	@Override
	public void preDestroy() {
		closing = true;
		super.preDestroy();
	}

	@Override
	protected void processRequestMessage(Message requestMessage) {
		try {
			ServiceRequest serviceRequest = (ServiceRequest) requestMessage.getBody();

			transportSessionId(serviceRequest);

			boolean asynchronous = requestMessage.getReplyTo() == null;
			if (!asynchronous) {

				ServiceResult result = ServiceResults.envelope(serviceRequest);

				try {
					if (delay == null) {
						result = ServiceResults.envelope(serviceRequest);
					} else {
						if (replyAfterKeepAlive) {
							Thread.sleep(delay);
							reply(requestMessage, StillProcessing.T.create());
						}
						Thread.sleep(delay);
					}
				} catch (Throwable t) {
					result = ServiceResults.encodeFailure(t);
				}

				reply(requestMessage, result);

			}

		} catch (Exception e) {
			log.error("Failed to process incoming message: " + requestMessage + ": " + e, closing ? null : e);
		}
	}

	private void reply(Message requestMessage, ServiceResult result) {
		if (closing)
			return;
		Message responseMessage = createResponseMessage(requestMessage, result);
		responseProducer.sendMessage(responseMessage, requestMessage.getReplyTo());
	}

}
