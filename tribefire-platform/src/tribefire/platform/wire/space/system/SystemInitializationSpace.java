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
package tribefire.platform.wire.space.system;

import static com.braintribe.wire.api.scope.InstanceConfiguration.currentInstance;
import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.map;

import java.util.Map;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.processing.shutdown.JvmShutdownWatcher;
import com.braintribe.utils.FileAutoDeletion;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class SystemInitializationSpace implements WireSpace {

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		setSystemProperties();
		// We need to load this bean explicitly so that it gets closed when the services are stopped
		fileAutoDeletion();

		shutdownWatcher();
	}

	@Managed
	private JvmShutdownWatcher shutdownWatcher() {
		JvmShutdownWatcher watcher = new JvmShutdownWatcher();
		currentInstance().onDestroy(watcher::startShutdownWatch);
		return watcher;
	}

	@Managed
	public FileAutoDeletion fileAutoDeletion() {
		FileAutoDeletion bean = new FileAutoDeletion();
		return bean;
	}

	public void setSystemProperties() {
		// @formatter:off
		setSystemProperties(
				map(
					entry("net.sf.ehcache.skipUpdateCheck", "true")
				)
			);
		// @formatter:on
	}

	protected void setSystemProperties(Map<String, String> systemProperties) {
		for (Map.Entry<String, String> entry : systemProperties.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();

			try {
				System.setProperty(key, value);
			} catch (Exception e) {
				throw Exceptions.unchecked(e, "Failed to set the system property: " + key + "=" + value);
			}
		}
	}

}
