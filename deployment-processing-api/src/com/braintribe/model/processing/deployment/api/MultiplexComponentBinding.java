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
package com.braintribe.model.processing.deployment.api;

import java.util.function.Supplier;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;

public interface MultiplexComponentBinding<D extends Deployable> {

	/**
	 * Binds a {@link Deployable} denotation type hold by this builder to an expert with the help of a {@link ComponentBinder}. This binding will be used
	 * during a deployment of and instance of the denotation type.
	 * 
	 * @param componentBinder the component binder that acts as a type-safety correlation between denotation type and expert. It is also a 
	 * 	potential enricher of the expert being supplied by the valueSupplier param. Another quality of the component binder is the announcement of the interfaces the final expert will have.
	 * @param valueSupplier the supplier that will supply the actual expert during a deployment of the deployable type
	 * @return <code>this</code> in order to allow the continuation of the multiplex bindings in a fluent way
	 */
	<T> MultiplexComponentBinding<D> bind(ComponentBinder<? super D, T> componentBinder, Supplier<? extends T> valueSupplier);

	/**
	 * Binds the {@link Deployable} denotation type hold by this builder to an expert without the help of a convenient {@link ComponentBinder}. Because of that no enriching will take place
	 * and the expert interfaces must be announced manually.
	 * 
	 * @param componentType the component type for which you are binding an expert
	 * @param valueSupplier the supplier that will supply the actual expert during a deployment of the deployable type
	 * @param componentInterfaces the interfaces that the expert will support.
	 * @return <code>this</code> in order to allow the continuation of the multiplex bindings in a fluent way
	 */
	<T> MultiplexComponentBinding<D> bindPlain(EntityType<? super D> componentType, Supplier<? extends T> valueSupplier, Class<T> ... componentInterfaces);
}
