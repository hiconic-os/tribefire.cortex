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
package com.braintribe.model.processing.session.wire.common.space;

import java.util.concurrent.ExecutorService;

import com.braintribe.execution.virtual.VirtualThreadExecutorBuilder;
import com.braintribe.model.processing.session.wire.common.contract.CommonContract;
import com.braintribe.utils.stream.api.StreamPipeFactory;
import com.braintribe.utils.stream.api.StreamPipes;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public class CommonSpace implements CommonContract {

	@Override
	@Managed
	public ExecutorService executorService() {
		return VirtualThreadExecutorBuilder.newPool().concurrency(200).build();
	}

	@Override
	@Managed
	public StreamPipeFactory streamPipeFactory() {
		return StreamPipes.simpleFactory();
	}
}
