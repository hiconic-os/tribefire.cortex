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
package tribefire.module.api;

import java.util.Map;

import com.braintribe.model.generic.GenericEntity;

/**
 * Interface for EnvironmentDenotationRegistry.
 * 
 * @author peter.gazdik
 */
public interface EnvironmentDenotations {

	void register(String bindId, GenericEntity denotationInstance);

	/**
	 * Returns a denotation instance for given bindId as registered via {@link #register}, or <tt>null</tt> if no entry is found.
	 */
	<T extends GenericEntity> T lookup(String bindId);

	/**
	 * Returns a sub-set of {@link #entries()}, where the key (bindId) matches given pattern using {@link String#matches(String)} check.
	 */
	<T extends GenericEntity> Map<String, T> find(String pattern);

	/**
	 * Returns a map where keys are bindIds and values are corresponding denotation instances, as registered via {@link #register} method.
	 */
	Map<String, GenericEntity> entries();

}
