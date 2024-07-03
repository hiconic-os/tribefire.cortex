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

/**
 * Allows access to the actual deployable behind a {@link DcProxy}. Every {@link DcProxy} has an instance of this type which contains the
 * identification of the deployable this proxy is there for (its externalId), and can be configured with the actual deployed instance. This interface
 * offers read-only access on the delegation, while {@link ConfigurableDcProxyDelegation} also offers methods to configure the delegation.
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * DcProxyDelegation delegation = DcProxy.getDelegateManager(dcProxyInstance);
 * <b>return</b> delegation.getDeployedUnit();
 * </pre>
 * 
 * @author peter.gazdik
 */
public interface DcProxyDelegation {

	DeployedUnit getDeployedUnit();

	<E> ResolvedComponent<E> getDelegateOptional();

	Object getDelegate() throws DeploymentException;

}