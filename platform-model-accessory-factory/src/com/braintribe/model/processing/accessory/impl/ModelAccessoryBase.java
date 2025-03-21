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

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.accessory.api.PurgableModelAccessory;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryListener;
import com.braintribe.provider.Holder;

public abstract class ModelAccessoryBase implements PurgableModelAccessory {

	// constants
	private static final Logger log = Logger.getLogger(ModelAccessoryBase.class);

	// configurable
	private Supplier<?> sessionProvider;

	// post-initialized
	protected ManagedGmSession modelSession;
	protected ModelOracle modelOracle;
	protected CmdResolver cmdResolver;
	private final Object buildMonitor = new Object();
	private final Object updateMonitor = new Object();
	private volatile boolean isUpToDate = false; // when false, access to this accessory requires a build() call.
	private final List<ModelAccessoryListener> listeners = Collections.synchronizedList(newList());

	private String toString = getClass().getSimpleName() + "@" + hashCode();

	public ModelAccessoryBase() {
	}

	@Configurable
	public void setSessionProvider(Supplier<?> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	/** Just a simple descriptor to improve toString information. */
	@Configurable
	public void setDescription(String description) {
		toString = getClass().getSimpleName() + "[" + description + "]@" + hashCode();
	}

	public Supplier<?> getSessionProvider() {
		if (sessionProvider == null) {
			this.sessionProvider = new Holder<>(new Object());
		}
		return sessionProvider;
	}

	@Override
	public CmdResolver getCmdResolver() {
		ensureInitialized();
		return cmdResolver;
	}

	@Override
	public ManagedGmSession getModelSession() {
		ensureInitialized();
		return modelSession;
	}

	@Override
	public ModelOracle getOracle() {
		ensureInitialized();
		return modelOracle;
	}

	@Override
	public ModelMdResolver getMetaData() {
		return getCmdResolver().getMetaData();
	}

	@Override
	public GmMetaModel getModel() {
		return getOracle().getGmMetaModel();
	}

	@Override
	public boolean isUpToDate() {
		return isUpToDate; // when false, access to this accessory requires a build() call.
	}

	@Override
	public void addListener(ModelAccessoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(ModelAccessoryListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void outdated() {
		synchronized (updateMonitor) {
			if (!isUpToDate)
				return;

			this.isUpToDate = false;
		}

		log.debug(() -> "Outdating " + this);

		// THIS WAY WE MIGHT NOTIFY A LISTENER AFTER IT WAS REMOVED!!! WE SHOULD AT LEAST DOCUMET THIS

		ModelAccessoryListener[] accessoryListeners = listeners.toArray(new ModelAccessoryListener[0]);

		for (ModelAccessoryListener listener : accessoryListeners)
			listener.onOutdated();
	}

	protected abstract void build();

	protected void ensureInitialized() {
		if (!isUpToDate) {
			synchronized (buildMonitor) {
				if (!isUpToDate) {
					log.trace(() -> "Ensuring initialization for " + this);
					this.build();
					isUpToDate = true;
					log.debug(() -> "Ensured initialization for " + this);
				}
			}
		}
	}

	@Override
	public String toString() {
		return toString;
	}

}
