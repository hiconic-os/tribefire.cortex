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

/**
 * <p>
 * A {@link DeploymentContext} which enables state change.
 * 
 * @author dirk.scheffler
 *
 * @param <D>
 *            The {@link Deployable} type bound to this context.
 * @param <T>
 *            The expert type bound to this context.
 */
public interface MutableDeploymentContext<D extends Deployable, T> extends DeploymentContext<D, T> {

	void setInstanceToBeBoundSupplier(Supplier<? extends T> instanceToBeBoundSupplier);

	/**
	 * In some cases, the binder might create a different instance than what the configured supplier would return (e.g.
	 * dummy simulation), in which case this method could return <tt>null</tt>.
	 * 
	 * @return instance from the configured supplier, in case it was used for deployment, or <tt>null</tt> otherwise.
	 */
	T getInstanceToBoundIfSupplied();
}
