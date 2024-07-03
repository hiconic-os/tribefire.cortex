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
package tribefire.platform.impl.configuration;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;

import tribefire.module.api.EnvironmentDenotations;

/**
 * A registry for denotation instances provided by environment-specific configurators.
 * 
 */
public class EnvironmentDenotationRegistry implements EnvironmentDenotations {

	private final Map<String, GenericEntity> registry = new ConcurrentHashMap<>();

	private static final EnvironmentDenotationRegistry instance = new EnvironmentDenotationRegistry();

	private static Logger log = Logger.getLogger(EnvironmentDenotationRegistry.class);

	private EnvironmentDenotationRegistry() {
	}

	public static EnvironmentDenotationRegistry getInstance() {
		return instance;
	}

	/**
	 * Registers an environment-provided denotation instance to the registry
	 * 
	 * @param bindId
	 *            The ID that the denotation type should be bound to
	 * @param denotationInstance
	 *            The denotation instance
	 */
	@Override
	public void register(String bindId, GenericEntity denotationInstance) {
		requireNonNull(bindId, "bind id cannot be null");

		GenericEntity previousDeployable = registry.put(bindId, denotationInstance);

		log.info(() -> "Registered denotation instance under bindId [ " + bindId + " ]: [ " + denotationInstance + " ]"
				+ ((previousDeployable != null) ? " replacing previous [ " + denotationInstance + " ]" : ""));
	}

	/**
	 * Retrieves an environment-provided denotation instance from the registry
	 * 
	 * @param bindId
	 *            The ID of the requested denotation instance
	 * @return The denotation instance, or null, if no denotation instance for this ID is registered
	 */
	@Override
	public <T extends GenericEntity> T lookup(String bindId) {
		requireNonNull(bindId, "bind id cannot be null");

		T denotationType = (T) registry.get(bindId);

		if (log.isDebugEnabled() && denotationType != null)
			log.debug("Look up successful for id [ " + bindId + " ]: [ " + denotationType + " ]");

		return denotationType;
	}

	/**
	 * Retrieves a list environment-provided denotation instances from the registry by using a regex pattern.
	 * 
	 * @param pattern
	 *            The regular expression identifying the requested denotation instances
	 * @return The denotation instances, or an empty set, if no denotation instance for this ID is registered
	 */
	@Override
	public <T extends GenericEntity> Map<String, T> find(String pattern) {
		requireNonNull(pattern, "pattern cannot be null");

		Map<String, T> result = new HashMap<>();

		registry.entrySet().stream() //
				.filter(ks -> ks.getKey().matches(pattern)) //
				.forEach(ks -> result.put(ks.getKey(), (T) ks.getValue()));

		if (log.isDebugEnabled()) {
			if (result.isEmpty())
				log.debug("No denotation type registered that matches the pattern [ " + pattern + " ]");
			else
				log.debug("Look up successful for pattern [ " + pattern + " ]: [ " + result + " ]");
		}

		return result;
	}

	@Override
	public Map<String, GenericEntity> entries() {
		return Collections.unmodifiableMap(registry);
	}

}
