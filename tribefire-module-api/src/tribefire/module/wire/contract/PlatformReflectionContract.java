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

import java.util.List;

import com.braintribe.model.service.api.InstanceId;
import com.braintribe.wire.api.space.WireSpace;

/**
 * @author peter.gazdik
 */
public interface PlatformReflectionContract extends WireSpace {

	InstanceId instanceId();

	/** id of current node, typically set via environment variable. */
	String nodeId();

	String globalId();

	String getProperty(String propertyName);

	/**
	 * Unmodifiable list of {@link ModuleReflectionContract}s for modules that have already been wired.
	 * <p>
	 * Note that all the modules are wired before the binding phase begins, as explained here: {@link TribefireModuleContract#onBeforeBinding()}
	 */
	List<ModuleReflectionContract> modules();

}
