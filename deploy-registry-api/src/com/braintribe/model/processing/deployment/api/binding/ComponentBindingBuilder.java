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
import com.braintribe.model.processing.deployment.api.ComponentBinder;

/**
 * <p>
 * Coming from {@link DenotationBindingBuilder}, the next step of the binding is to define the {@link Deployable} component type and its expert
 * interfaces to progress to the {@link ExpertBindingBuilder} level.
 * 
 * <p>
 * Component types and expert interfaces are normally {@link ComponentBindingBuilder#component(ComponentBinder) given} by a prepared
 * {@link ComponentBinder} implementation. Additionally those needs can be given {@link ComponentBindingBuilder#component(EntityType, Class, Class...)
 * explicitly} or can be partially {@link ComponentBindingBuilder#component(Class, Class...) inferred}.
 * 
 * @author Dirk Scheffler
 *
 * @param <D>
 *            the denotation type for which the binding is being made
 */
public interface ComponentBindingBuilder<D extends Deployable> {
	/**
	 * This is the preferred way of component binding as it makes use of well prepared {@link ComponentBinder} implementations. The API continues to
	 * the actual expert binding on {@link ExpertBindingBuilder} by supplying the component type and the expert type via a {@link ComponentBinder}.
	 * The ComponentBinder will type-safely correlate the component type and the expert type based on Java generics. It will potentially also care for
	 * an automatic expert enriching during deployment.
	 */
	<T> ExpertBindingBuilder<D, T> component(ComponentBinder<? super D, T> componentBinder);

	/**
	 * This way to bind a component is for the rare case that a specific {@link Deployable} type was invented and is to be bound only once, such that
	 * the preparation of a {@link ComponentBinder} would add no value. The API continues to the actual expert binding on {@link ExpertBindingBuilder}
	 * by directly supplying the component type and the expert types. The first expert type is being used to infer type-safety to the expert binding.
	 */
	<T> ExpertBindingBuilder<D, T> component(EntityType<? super D> componentType, Class<T> expertInterface, Class<?>... additionalExpertInterfaces);

	/**
	 * This way to bind a component is for the rare case that a specific {@link Deployable} type was invented and is to be bound only once, such that
	 * the preparation of a {@link ComponentBinder} would add no value. The API continues to the actual expert binding on {@link ExpertBindingBuilder}
	 * by infering the component type from the denotation type that was given on the {@link DenotationBindingBuilder} level and by directly supplying
	 * the expert types. The first expert type is being used to infer type-safety to the expert binding.
	 */
	<T> ExpertBindingBuilder<D, T> component(Class<T> expertInterface, Class<?>... additionalExpertInterfaces);
}
