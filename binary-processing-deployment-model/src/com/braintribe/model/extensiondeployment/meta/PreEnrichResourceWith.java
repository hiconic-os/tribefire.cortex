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
import com.braintribe.model.meta.data.EntityTypeMetaData;
import com.braintribe.model.resource.source.ResourceSource;

/**
 * Specifies which {@link ResourceEnricher}s should be used to enrich resources. This meta-data is placed on the {@link ResourceSource} type, similar
 * to {@link PersistResourceWith}.
 */
public interface PreEnrichResourceWith extends EntityTypeMetaData {

	EntityType<PreEnrichResourceWith> T = EntityTypes.T(PreEnrichResourceWith.class);

	List<ResourceEnricher> getPrePersistenceEnrichers();
	void setPrePersistenceEnrichers(List<ResourceEnricher> value);

}