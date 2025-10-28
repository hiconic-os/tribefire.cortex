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
package com.braintribe.model.logs.request;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.Resource;

public interface Logs extends LogsResponse {

	EntityType<Logs> T = EntityTypes.T(Logs.class);

	void setLog(Resource log);
	Resource getLog();

	// The following properties are used when the caller requested a base64 encoded response rather than a resource

	String getFilename();
	void setFilename(String filename);

	String getMimeType();
	void setMimeType(String mimeType);

	String getBase64EncodedResource();
	void setBase64EncodedResource(String base64EncodedResource);
}
