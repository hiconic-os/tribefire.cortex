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

import java.util.List;

import com.braintribe.model.extensiondeployment.ResourceEnricher;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Legacy aggregation of {@link PersistResourceWith} and something like {@link PreEnrichResourceWith}, with a little strange post enrichers.
 * <p>
 * Originally this was a sub-type of {@link PreEnrichResourceWith}, but that makes it difficult to define default MD, because they are overridden by
 * existing UploadWith instances. Instead, both MD are independent and the resolution looks for {@link PreEnrichResourceWith} first, and falls back to
 * UploadWith.
 */
public interface UploadWith extends PersistResourceWith {

	EntityType<UploadWith> T = EntityTypes.T(UploadWith.class);

	/** @deprecated configure this with a separate MD - {@link PreEnrichResourceWith}. */
	@Deprecated
	List<ResourceEnricher> getPrePersistenceEnrichers();
	@Deprecated
	void setPrePersistenceEnrichers(List<ResourceEnricher> value);

	List<ResourceEnricher> getPostPersistenceEnrichers();
	void setPostPersistenceEnrichers(List<ResourceEnricher> value);

}