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
package com.braintribe.model.processing.check.api;

import com.braintribe.model.check.service.CheckResult;
import com.braintribe.model.check.service.ParameterizedCheckRequest;
import com.braintribe.model.processing.service.api.ServiceRequestContext;

public interface ParameterizedCheckProcessor <T extends ParameterizedCheckRequest> extends CheckProcessorBase {

	CheckResult check(ServiceRequestContext requestContext, T request);

}
