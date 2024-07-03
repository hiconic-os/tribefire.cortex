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
 * Interface that every deployable proxy implements.
 * <p>
 * Every deployable is deployed as a proxy with a delegate, which among other things allows for it's re-deployment without the need to re-deploy the
 * components that depend on it. These generated proxies have to extend/implement all the class/interfaces relevant for given component type (which is
 * usually the same type - IncrementalAccess as component type and also as the interface) and additionally we add this interface to be able to access
 * the underlying {@link DcProxyDelegation delegation} handler.
 * 
 * @author peter.gazdik
 */
public interface DcProxy {

	static DcProxyDelegation getDelegateManager(Object o) {
		return ((DcProxy) o).$_delegatorAligator();
	}

	static ConfigurableDcProxyDelegation getConfigurableDelegateManager(Object o) {
		return ((DcProxy) o).$_delegatorAligator();
	}

	/**
	 * Returns the underlying {@link ConfigurableDcProxyDelegation}
	 * <p>
	 * Because our proxies implement this interface and the actual deployable component interface (IncrementalAccess or ServiceProcessor or whatever),
	 * we needed a method name that will not conflict with the deployable component. We're confident this does the trick.
	 */
	ConfigurableDcProxyDelegation $_delegatorAligator();

}
