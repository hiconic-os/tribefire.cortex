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
package com.braintribe.model.processing.accessory.api;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryListener;

/**
 * @author peter.gazdik
 */
public interface PlatformModelEssentials {

	GmMetaModel getModel();

	ManagedGmSession getModelSession();

	ModelOracle getOracle();

	String getOwnerType();

	void addListener(ModelAccessoryListener modelAccessoryListener);

	/**
	 * Signals (from the outside) that this instance is out-dated. As a result, the listeners are notified with
	 * {@link ModelAccessoryListener#onOutdated()}.
	 * <p>
	 * With the current implementation the first listener is the one that removes this entry from the cache.
	 */
	// See PmeSupplierBase.getForAccess(...)
	// See PmeSupplierBase.getForServiceDomain(...)
	void outdated();

	PmeKey key();

}
