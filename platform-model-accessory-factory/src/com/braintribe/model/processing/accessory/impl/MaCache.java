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
package com.braintribe.model.processing.accessory.impl;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.accessory.api.PurgableModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.utils.lcd.NullSafe;

/**
 * @author peter.gazdik
 */
/* package */ class MaCache {

	private static final Logger log = Logger.getLogger(MaCache.class);

	private final BiFunction<String, String, PurgableModelAccessory> factory;
	private final String idName;
	private final String monitor;

	private Map<String, Map<String, PurgableModelAccessory>> cache;

	public MaCache(BiFunction<String, String, PurgableModelAccessory> factory, String idName) {
		this.factory = factory;
		this.idName = idName;
		this.monitor = new String("monitor." + idName);
	}

	public void setCacheModelAccessories(boolean cacheModelAccessories) {
		if (cacheModelAccessories)
			cache = new ConcurrentHashMap<>();
	}

	public ModelAccessory getModelAccessory(String id, String perspective) {
		requireNonNull(id, () -> idName + " cannot be null");

		if (cache == null)
			return factory.apply(id, perspective);

		String perspectiveKey = NullSafe.get(perspective, "<default>");

		Map<String, PurgableModelAccessory> perspectiveToAccessory = cache.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

		PurgableModelAccessory result = perspectiveToAccessory.get(perspectiveKey);

		if (result != null)
			return result;

		synchronized (monitor) {
			result = perspectiveToAccessory.get(perspectiveKey);
			if (result == null) {
				result = factory.apply(id, perspective);
				perspectiveToAccessory.put(perspectiveKey, result);
				log.debug(() -> "Cached ModelAccessory for " + idName + "='" + id + "', perspective: '" + perspective + "'");
			}
		}

		return result;
	}

	public void onChange(String id) {
		log.debug(() -> "Received onChange notification for " + idName + "='" + id + "'");

		if (id != null && cache != null)
			purgeEntry(id);
		else
			log.trace(() -> "Ignoring purge request for " + idName + "='" + id + "'");
	}

	private void purgeEntry(String id) {
		Map<String, PurgableModelAccessory> cachedEntry = cache.remove(id);

		if (cachedEntry == null) {
			log.trace(() -> "Component changed but no entry to purge for " + idName + "='" + id + "'");
			return;
		}

		for (Entry<String, PurgableModelAccessory> entry : cachedEntry.entrySet()) {
			String perspectiveKey = entry.getKey();
			PurgableModelAccessory cachedAccessory = entry.getValue();

			log.debug(() -> "Component changed, purging entry for " + idName + "='" + id + "', perspective = '" + perspectiveKey + "' : "
					+ cachedAccessory);

			cachedAccessory.outdated();
		}

	}
}
