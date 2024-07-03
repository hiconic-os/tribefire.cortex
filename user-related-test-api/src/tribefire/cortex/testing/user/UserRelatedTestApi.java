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
package tribefire.cortex.testing.user;

import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;

/**
 * This class gives access to global static fields that provide session factories for testing purposes. They are set by
 * the <tt>tribefire.cortex.testing:test-runner-module</tt> to be used by integration tests. When you execute the tests from your
 * IDE, these fields most likely won't be set and a remote session should be used instead. <code>Imp</code> for example uses
 * {@link #userSessionFactory} as default when it is set or otherwise a remote session.
 * 
 * @author Neidhart.Orlich
 * @author Peter.Gazdik
 */
public class UserRelatedTestApi {
	public static PersistenceGmSessionFactory userSessionFactory;
	public static PersistenceGmSessionFactory systemSessionFactory;
}
