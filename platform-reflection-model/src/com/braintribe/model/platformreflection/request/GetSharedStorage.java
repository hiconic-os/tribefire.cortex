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
package com.braintribe.model.platformreflection.request;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.platformreflection.SharedStorage;
import com.braintribe.model.service.api.ServiceRequest;

public interface GetSharedStorage extends PlatformReflectionRequest {

	EntityType<GetSharedStorage> T = EntityTypes.T(GetSharedStorage.class);

	@Initializer("'cortex'")
	String getAccessId();
	void setAccessId(String accessId);

	@Description("If set to 'true', the response will omit binary data, i.e. it will exclude all CsaStoreResource operations.")
	boolean getExcludeBinaries();
	void setExcludeBinaries(boolean excludeBinaries);

	@Override
	EvalContext<? extends SharedStorage> eval(Evaluator<ServiceRequest> evaluator);
}
