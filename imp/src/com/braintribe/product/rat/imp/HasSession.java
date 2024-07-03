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
package com.braintribe.product.rat.imp;

import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * Interface for various classes that have a session.
 */
public interface HasSession {

	/**
	 * Returns the session.
	 */
	PersistenceGmSession session();

	/**
	 * {@link PersistenceGmSession#commit() Commits} all changes (if any).
	 * <p>
	 * <b>Note:</b> This only commits changes done through this session. If you are working with two different instances
	 * of {@link ImpApi} then you have two different sessions. Take care to commit the correct one(s).
	 */
	void commit();

}
