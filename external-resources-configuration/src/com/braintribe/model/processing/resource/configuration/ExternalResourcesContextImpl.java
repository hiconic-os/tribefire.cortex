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
package com.braintribe.model.processing.resource.configuration;

import com.braintribe.model.extensiondeployment.meta.BinaryProcessWith;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.resource.source.ResourceSource;

public class ExternalResourcesContextImpl implements ExternalResourcesContext, ExternalResourcesContextBuilder {

	private BinaryProcessWith binaryProcessWith;
	private GmMetaModel persistenceDataModel;
	private EntityType<? extends ResourceSource> resourceSourceType;

	@Override
	public ExternalResourcesContextBuilder setBinaryProcessWith(BinaryProcessWith binaryProcessWith) {
		this.binaryProcessWith = binaryProcessWith;
		return this;
	}

	@Override
	public ExternalResourcesContextBuilder setPersistenceDataModel(GmMetaModel persistenceDataModel) {
		this.persistenceDataModel = persistenceDataModel;
		return this;
	}

	@Override
	public ExternalResourcesContextBuilder setResourceSourceType(EntityType<? extends ResourceSource> resourceSourceType) {
		this.resourceSourceType = resourceSourceType;
		return this;
	}

	@Override
	public ExternalResourcesContext build() {
		return this;
	}

	@Override
	public BinaryProcessWith getBinaryProcessWith() {
		return binaryProcessWith;
	}

	@Override
	public GmMetaModel getPersistenceDataModel() {
		return persistenceDataModel;
	}

	@Override
	public EntityType<? extends ResourceSource> getResourceSourceType() {
		return resourceSourceType;
	}
	
}
