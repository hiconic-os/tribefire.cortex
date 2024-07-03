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
package tribefire.cortex.initializer.support.wire.contract;

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.properties.PropertyLookups;

/**
 * Marker interface for contracts dedicated to resolving properties / environment variables.
 * <p>
 * These contracts are implemented automatically.
 * <p>
 * Each method is assumed to be the name of a property and the implementation returns the corresponding value, resolved via {@link TribefireRuntime}.
 * <p>
 * Example:
 * 
 * <pre>
 * public interface XyzEnvContract extends PropertyLookupContract {
 * 	String EXTERNAL_SERVICE_URL();
 * 
 * 	&#64;Default("4")
 * 	Integer NUMBER_OF_THREADS_FOR_SOME_TASK();
 * 
 * 	&#64;Decrypt
 * 	String SOME_DB_PASSWORD();
 * }
 * </pre>
 * 
 * @see PropertyLookups
 * 
 * @author Dirk Scheffler
 */
public interface PropertyLookupContract extends WireSpace {
	// marker interface
}
