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
package com.braintribe.model.processing.deployment.api.binding;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

/**
 * First step in the {@link DenotationBindingBuilder} chain when binding deployables in a module. This is {@link #bindForModule(String) first step} is
 * done by the module loader, and the resulting builder is then given to actual module, and all the bound deployables are associated with their module
 * of origin.
 * 
 * @see DenotationBindingBuilder
 */
public interface ModuleBindingBuilder {

	/**
	 * Starts a fluent builder for a module given by it's globalId. This binds a number of experts to a {@link Deployable} denotation
	 * {@link EntityType type}.
	 */
	DenotationBindingBuilder bindForModule(String moduleGlobalId);

}
