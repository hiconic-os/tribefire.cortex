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
package com.braintribe.web.servlet.about.expert;

import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.platformreflection.HeapDump;
import com.braintribe.model.platformreflection.request.GetHeapDump;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.StringTools;
import com.braintribe.web.servlet.about.ServiceInstanceIdManagement;

public class Heapdump {

	private static Logger logger = Logger.getLogger(Heapdump.class);

	public void processHeapdumpRequest(Evaluator<ServiceRequest> requestEvaluator, Collection<InstanceId> selectedServiceInstances,
			ServiceInstanceIdManagement serviceInstanceMgmt, HttpServletResponse resp, String userSessionId) throws Exception {

		logger.debug(() -> "Sending a request to create a heapdump to " + selectedServiceInstances + " with session " + userSessionId);

		if (selectedServiceInstances.size() != 1) {
			resp.setContentType("text/plain; charset=UTF-8");
			resp.getWriter().append("You have to select a single node on the left side to get a heap dump.");
			return;
		}

		InstanceId selectedServiceInstance = selectedServiceInstances.iterator().next();

		if (StringTools.isBlank(selectedServiceInstance.getNodeId())) {
			resp.setContentType("text/plain; charset=UTF-8");
			resp.getWriter().append("You have to select a single node on the left side to get a heap dump.");

		} else if (serviceInstanceMgmt.isLocalServerInstance(selectedServiceInstance)) {

			try {
				GetHeapDump ghd = GetHeapDump.T.create();
				EvalContext<? extends HeapDump> eval = ghd.eval(requestEvaluator);
				HeapDump heapDump = eval.get();
				Resource resource = heapDump.getHeapDump();

				resp.setContentType(resource.getMimeType());
				resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", resource.getName()));

				try (InputStream in = resource.openStream()) {
					IOTools.pump(in, resp.getOutputStream(), 0xffff);
				}

			} catch (Exception e) {
				throw new Exception("Error while trying to produce a heap dump.", e);
			}

		} else {

			resp.setContentType("text/plain; charset=UTF-8");
			resp.getWriter().append("Getting a heap dump from a remote node is not yet supported.");

			// This is not going to work

			/* try { GetHeapDump ghd = GetHeapDump.T.create();
			 * 
			 * MulticastRequest mcR = MulticastRequest.T.create(); mcR.setAsynchronous(false);
			 * mcR.setServiceRequest(ghd); mcR.setAddressee(selectedServiceInstance); mcR.setTimeout((long)
			 * Numbers.MILLISECONDS_PER_MINUTE * 5); mcR.setSessionId(userSessionId); EvalContext<? extends
			 * MulticastResponse> eval = mcR.eval(requestEvaluator); MulticastResponse multicastResponse = eval.get();
			 * 
			 * for (Map.Entry<InstanceId,ServiceResult> entry : multicastResponse.getResponses().entrySet()) {
			 * 
			 * ResponseEnvelope envelope = (ResponseEnvelope) entry.getValue(); HeapDump heapDump = (HeapDump)
			 * envelope.getResult();
			 * 
			 * Resource resource = heapDump.getHeapDump();
			 * 
			 * resp.setContentType(resource.getMimeType()); resp.setHeader("Content-Disposition",
			 * String.format("attachment; filename=\"%s\"", resource.getName()));
			 * 
			 * try (InputStream in = resource.openStream()) { IOTools.pump(in, resp.getOutputStream(), 0xffff); } }
			 * 
			 * return; } catch (Exception e) { logger.error("Error while trying to produce a heap dump.", e); } */
		}

		logger.debug(() -> "Done with processing a request to create a heapdump.");
	}
}
