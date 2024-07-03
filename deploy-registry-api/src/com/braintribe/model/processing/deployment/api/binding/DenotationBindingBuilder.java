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
import com.braintribe.model.generic.annotation.meta.DeployableComponent;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.ComponentBinder;

/**
 * The DenotationBindingBuilder API is used to register local and proxy expert bindings for {@link Deployable} denotation/component types which are
 * then used during deployment of {@link Deployable} instances.
 * 
 * <h1>Meaning of the different Deployable Types</h1>
 * 
 * There are {@link Deployable deployable} types with different meanings that play a role in binding.
 * 
 * <h2>Deployable Component Types</h2>
 * 
 * {@linkplain Deployable} component types are polymorphic base types that are annotated with {@link DeployableComponent} (or simply having the
 * {@link DeployableComponent DeployableComponent meta data}). They represent an extension point which is associated with an API that has to be
 * implemented by an expert (e.g. Marshaller is a component with corresponding API, and there are many different implementations of it). This
 * association between the deployable component type and the expert API type is a convention which can be made more concise by implementing a
 * {@link ComponentBinder} that will create a type-safe correlation.
 * 
 * <h2>Deployable Denotation Types</h2>
 * 
 * {@link Deployable} denotation {@link EntityType types} are instantiable and can be bound with this API. Such denotation types can inherit from
 * multiple component types and must therefore be bound for each inherited component to a fitting expert.
 * 
 * <h1>Structure of the builder API</h1>
 * 
 * The API consists of 4 levels represented by 4 interfaces:
 * 
 * <pre>
 *  - DenotationBindingBuilder
 *      - {@link ComponentBindingBuilder}
 *          - {@link ExpertBindingBuilder}
 *      - {@link ProxyExpertBindingBuilder}
 * </pre>
 * 
 * @author Dirk Scheffler
 */
public interface DenotationBindingBuilder {

	/**
	 * Starts a fluent builder to bind a number of experts to a {@link Deployable} denotation {@link EntityType type}.
	 * 
	 * @param denotationType
	 *            An instantiable {@link EntityType type} of the {@link Deployable} to be bound
	 * @return a {@link ComponentBindingBuilder} to continue with the specification of a {@link Deployable} component {@link EntityType type}
	 */
	<D extends Deployable> ComponentBindingBuilder<D> bind(EntityType<D> denotationType);

	/**
	 * Starts a fluent builder to bind a number of experts to a {@link Deployable} denotation instance of a given {@link EntityType type} and
	 * {@link Deployable#getExternalId() externalId}.
	 * 
	 * @param denotationType
	 *            An instantiable {@link EntityType type} of the {@link Deployable} to be bound
	 * @param externalId
	 *            the externalId which the denotation instance must have during deployment to match to this binding
	 * @return a {@link ComponentBindingBuilder} to continue with the specification of a {@link Deployable} component {@link EntityType type}
	 */
	<D extends Deployable> ComponentBindingBuilder<D> bind(EntityType<D> denotationType, String externalId);
}
