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
 * This is an entry for a component of the {@link DeployedUnit} which is found in a {@link DeployRegistry}
 * @author dirk.scheffler
 */
public interface DeployedComponent {
	/**
	 * The maybe enriched value which could add additional value to an expert that was given by a customer's deployable binding
	 * In some cases it is not enriched and then equal to {@link #suppliedImplementation()}
	 * 
	 * One example:
	 * 
	 * Given: a supplier for a SmoodAccess bound to a denotation type with an IncrementalAccessBinder 
	 * Result: InternalizingPersistenceProcessor(AopAccess(SmoodAccess))
	 */
	Object exposedImplementation();
	
	/**
	 * The original value that came from a supplier that was bound to some denotation type with help of a {@link ComponentBinder}
	 */
	Object suppliedImplementation();
	
	/**
	 * The {@link ComponentBinder} that was used to create this DeployedComponent
	 */
	ComponentBinder<?, ?> binder();
}
