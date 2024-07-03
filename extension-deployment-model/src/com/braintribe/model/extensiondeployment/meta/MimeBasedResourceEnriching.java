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
package com.braintribe.model.extensiondeployment.meta;

import java.util.Set;

import com.braintribe.model.extensiondeployment.ResourceEnricher;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.ModelMetaData;

/**
 * Special meta-data resolved by MimeBasedDispatchingResourceEnricher - a ResourceEnricher which handles an EnrichResource request by finding other
 * enrichers based on this MD.
 * <p>
 * This MD should be configured on the data model of an access.
 * <p>
 * Of course, this can only works as long as the mime type is present on given Resource, i.e. if it was specified or already enriched by a previous
 * enricher.
 */
public interface MimeBasedResourceEnriching extends ModelMetaData {

	EntityType<MimeBasedResourceEnriching> T = EntityTypes.T(MimeBasedResourceEnriching.class);

	ResourceEnricher getResourceEnricher();
	void setResourceEnricher(ResourceEnricher resourceEnricher);

	Set<String> getMimeTypes();
	void setMimeTypes(Set<String> mimeTypes);

}