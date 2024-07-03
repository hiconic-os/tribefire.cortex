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
package com.braintribe.model.execution.persistence.deployment;

import com.braintribe.model.extensiondeployment.ServiceProcessor;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Confidential;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface CallbackRestProcessor extends ServiceProcessor {

	final EntityType<CallbackRestProcessor> T = EntityTypes.T(CallbackRestProcessor.class);
	
	void setAccessId(String accessId);
	String getAccessId();
	
	void setContentType(String contentType);
	@Initializer("'application/json'")
	String getContentType();
	
	void setCryptorSecret(String cryptorSecret);
	@Confidential
	String getCryptorSecret();
}
