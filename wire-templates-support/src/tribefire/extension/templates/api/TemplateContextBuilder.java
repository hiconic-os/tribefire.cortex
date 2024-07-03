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
package tribefire.extension.templates.api;

import java.util.function.Function;

import com.braintribe.model.descriptive.HasExternalId;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

public interface TemplateContextBuilder<T extends TemplateContext> {

	TemplateContextBuilder<T> setIdPrefix(String idPrefix);

	TemplateContextBuilder<T> setEntityFactory(Function<EntityType<?>, GenericEntity> entityFactory);

	TemplateContextBuilder<T> setModule(com.braintribe.model.deployment.Module module);

	TemplateContextBuilder<T> setLookupFunction(Function<String, ? extends GenericEntity> lookupFunction);

	TemplateContextBuilder<T> setLookupExternalIdFunction(Function<String, ? extends HasExternalId> lookupExternalIdFunction);

	TemplateContextBuilder<T> setName(String name);

	T build();
}