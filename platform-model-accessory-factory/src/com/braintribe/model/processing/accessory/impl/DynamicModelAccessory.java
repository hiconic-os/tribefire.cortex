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

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryListener;

/**
 * @author peter.gazdik
 */
public class DynamicModelAccessory implements ModelAccessory, ModelAccessoryListener {

	private final Supplier<ModelAccessory> modelAccessorySupplier;

	private final Object lock = new String("DynamicModelAccessory");
	private final List<ModelAccessoryListener> listeners = Collections.synchronizedList(newList());

	private volatile ModelAccessory delegate;

	public DynamicModelAccessory(Supplier<ModelAccessory> modelAccessorySupplier) {
		this.modelAccessorySupplier = modelAccessorySupplier;
	}

	@Override
	public void onOutdated() {
		synchronized (lock) {
			ModelAccessory newModelAccessory = modelAccessorySupplier.get();

			if (delegate != newModelAccessory) {
				if (delegate != null)
					delegate.removeListener(this);

				delegate = newModelAccessory;
				delegate.addListener(this);
			}
		}

		ModelAccessoryListener[] listenersCopy = listeners.toArray(new ModelAccessoryListener[0]);

		for (ModelAccessoryListener listener : listenersCopy)
			listener.onOutdated();
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
	public CmdResolver getCmdResolver() {
		return getDelegate().getCmdResolver();
	}

	@Override
	public ManagedGmSession getModelSession() {
		return getDelegate().getModelSession();
	}

	@Override
	public GmMetaModel getModel() {
		return getDelegate().getModel();
	}

	@Override
	public ModelOracle getOracle() {
		return getDelegate().getOracle();
	}

	private ModelAccessory getDelegate() {
		if (delegate == null)
			synchronized (lock) {
				if (delegate == null) {
					delegate = modelAccessorySupplier.get();
					delegate.addListener(this);
				}

			}

		return delegate;
	}

}
