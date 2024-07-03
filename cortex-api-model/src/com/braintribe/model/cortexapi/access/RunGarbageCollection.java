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
package com.braintribe.model.cortexapi.access;

import java.util.List;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.cleanup.GarbageCollection;
import com.braintribe.model.service.api.ServiceRequest;

public interface RunGarbageCollection extends GarbageCollectionRequest {
	
	EntityType<RunGarbageCollection> T = EntityTypes.T(RunGarbageCollection.class);
	
	
	/**
	 * See {@link #setAccess(IncrementalAccess)}
	 */
	IncrementalAccess getAccess();

	/**
	 * The access on which the garbage collection will be performed.
	 */
	void setAccess(IncrementalAccess access);

	/**
	 * see {@link #setUseCases(List)}
	 */
	List<String> getUseCases();

	/**
	 * An optional list of use cases. If set, the garbage collection will use a use case selector to retrieve the
	 * {@link GarbageCollection} metadata.
	 */
	void setUseCases(List<String> useCases);

	/**
	 * See {@link #getTestModeEnabled()}
	 */
	@Initializer("true")
	boolean getTestModeEnabled();

	/**
	 * If enabled, the garbage collection will be run and a report will be created, but nothing will actually be
	 * deleted.
	 */
	void setTestModeEnabled(boolean testModeEnabled);
	
	
	
	@Override
	EvalContext<? extends GarbageCollectionResponse> eval(Evaluator<ServiceRequest> evaluator);

}
