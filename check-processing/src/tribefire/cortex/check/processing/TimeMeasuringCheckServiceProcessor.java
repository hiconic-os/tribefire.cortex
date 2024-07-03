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
package tribefire.cortex.check.processing;

import com.braintribe.model.check.service.CheckRequest;
import com.braintribe.model.check.service.CheckResult;
import com.braintribe.model.processing.check.api.CheckProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;

/**
 * Measures the time a processor takes to execute a {@link CheckRequest}.
 * 
 * @author christina.wilpernig
 */
public class TimeMeasuringCheckServiceProcessor implements ServiceProcessor<CheckRequest, CheckResult> {

	private CheckProcessor checkProcessor;

	public TimeMeasuringCheckServiceProcessor(CheckProcessor checkProcessor) {
		this.checkProcessor = checkProcessor;
	}
	
	@Override
	public CheckResult process(ServiceRequestContext requestContext, CheckRequest request) {
		
		long t0 = System.nanoTime();
		
		CheckResult checkResult = checkProcessor.check(requestContext, request);
		
		checkResult.setElapsedTimeInMs((System.nanoTime() - t0)/1_000_000.0);
		
		return checkResult;
	}

}
