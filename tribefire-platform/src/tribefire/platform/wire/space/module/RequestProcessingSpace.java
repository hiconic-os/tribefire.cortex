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
package tribefire.platform.wire.space.module;

import java.util.function.Supplier;

import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.thread.api.ThreadContextScope;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.RequestProcessingContract;
import tribefire.platform.wire.space.rpc.RpcSpace;

/**
 * @author peter.gazdik
 */
@Managed
public class RequestProcessingSpace implements RequestProcessingContract {

	@Import
	private RpcSpace rpc;

	@Override
	public Evaluator<ServiceRequest> evaluator() {
		return rpc.serviceRequestEvaluator();
	}

	@Override
	public Evaluator<ServiceRequest> systemEvaluator() {
		return rpc.systemServiceRequestEvaluator();
	}

	@Override
	public Supplier<ThreadContextScope> serviceRequestContextThreadContextScopeSupplier() {
		return rpc.serviceRequestContextThreadContextScopeSupplier();
	}

}
