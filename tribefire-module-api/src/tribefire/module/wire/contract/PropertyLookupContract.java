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
package tribefire.module.wire.contract;

import com.braintribe.wire.api.space.WireSpace;

/**
 * Marker interface for custom property-lookup contracts. Extending contracts (which also have to be interfaces, not classes) will automatically be
 * proxied to resolve properties (System, Environment Variables, platform-specific configuration).
 * 
 * <h3>Example:</h3>
 * 
 * <pre>
 * public interface MyPropertiesContract extends PropertyLookupContract {
 * 
 * 	String MAX_NUMBER_OF_FOOBAR_THREADS();
 * 
 * }
 * </pre>
 */
public interface PropertyLookupContract extends WireSpace {
	// Empty
}
