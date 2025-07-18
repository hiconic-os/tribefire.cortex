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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.common.lcd.Tuple;
import com.braintribe.common.lcd.Tuple.Tuple3;
import com.braintribe.exception.Exceptions;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.accessory.api.PlatformModelEssentials;
import com.braintribe.model.processing.accessory.api.PlatformModelEssentialsSupplier;
import com.braintribe.model.processing.accessory.api.PmeKey;
import com.braintribe.model.processing.accessory.impl.ReadOnlyPropertyAccessInterceptor.ReadOnlyPai;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryListener;
import com.braintribe.model.processing.session.api.notifying.interceptors.VdEvaluation;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;

/**
 * Base for a caching implementation of {@link PlatformModelEssentialsSupplier}.
 * 
 * @author peter.gazdik
 */
/* package */ abstract class PmeSupplierBase implements PlatformModelEssentialsSupplier {

	private final Map<Tuple3<String, String, Boolean>, PlatformModelEssentials> accessPmeCache = new ConcurrentHashMap<>();
	private final Map<Tuple3<String, String, Boolean>, PlatformModelEssentials> sdPmeCache = new ConcurrentHashMap<>();

	// #####################################################
	// ## . . . . . . . . . Access . . . . . . . . . . . .##
	// #####################################################

	@Override
	public PlatformModelEssentials getForAccess(String accessId, String perspective, boolean extended) {
		Tuple3<String, String, Boolean> key = Tuple.of(accessId, perspective, extended);

		// ConcurrentHashMap guarantees the createNewAccessPme method is called only once per key
		return accessPmeCache.computeIfAbsent(key, this::createNewAccessPme);
	}

	private PlatformModelEssentials createNewAccessPme(Tuple3<String, String, Boolean> k) {
		try {
			PlatformModelEssentials result = createNewAccessPme(k.val0(), k.val1(), k.val2());
			result.addListener(() -> accessPmeCache.remove(k));
			return result;

		} catch (RuntimeException e) {
			throw Exceptions.contextualize(e, "Error while creating model essentials for accessId: " + k.val0());
		}

	}

	/** Cannot return null! */
	protected abstract PlatformModelEssentials createNewAccessPme(String accessId, String perspective, boolean extended);

	// #####################################################
	// ## . . . . . . . . . Service . . . . . . . . . . . ##
	// #####################################################

	@Override
	public PlatformModelEssentials getForServiceDomain(String serviceDomainId, String perspective, boolean extended) {
		Tuple3<String, String, Boolean> key = Tuple.of(serviceDomainId, perspective, extended);

		// ConcurrentHashMap guarantees the createNewServiceDomainPme method is called only once per key
		return sdPmeCache.computeIfAbsent(key, this::createNewServiceDomainPme);
	}

	private PlatformModelEssentials createNewServiceDomainPme(Tuple3<String, String, Boolean> k) {
		try {
			PlatformModelEssentials result = createNewServiceDomainPme(k.val0(), k.val1(), k.val2());
			result.addListener(() -> sdPmeCache.remove(k));
			return result;

		} catch (RuntimeException e) {
			throw Exceptions.contextualize(e, "Error while creating model essentials for service domain: " + k.val0());
		}
	}

	/** Cannot return null! */
	protected abstract PlatformModelEssentials createNewServiceDomainPme(String serviceDomainId, String perspective, boolean extended);

	// #####################################################
	// ## . . . . . . . . PME Builder . . . . . . . . . . ##
	// #####################################################

	/* package */ static class BasicPmeBuilder {
		protected final ReadOnlyPropertyAccessInterceptor readOnlyPai;
		protected final BasicManagedGmSession session;
		protected final GmMetaModel model;
		protected final PmeKey key;

		public BasicPmeBuilder(GmMetaModel model, PmeKey key) {
			this.model = model;
			this.key = key;
			this.readOnlyPai = new ReadOnlyPropertyAccessInterceptor();
			this.session = newManagedGmSession();
		}

		private BasicManagedGmSession newManagedGmSession() {
			BasicManagedGmSession result = new BasicManagedGmSession();
			result.interceptors().with(ReadOnlyPai.class).before(VdEvaluation.class).add(readOnlyPai);
			result.setDescription("PlatformModelEssentials for model: " + this.model.getName());

			return result;
		}

		public BasicPme build() {
			GmMetaModel mergedModel = prepareModel();

			readOnlyPai.isReadOnly = true;

			return new BasicPme(session, mergedModel, key);
		}

		protected GmMetaModel prepareModel() {
			return merge(model);
		}

		protected <T> T merge(T ge) {
			return session.merge().adoptUnexposed(false).doFor(ge);
		}

	}

	// #####################################################
	// ## . . . . . . . . . . PMEs . . . . . . . . . . . .##
	// #####################################################

	public static class OwnerAwarePme implements PlatformModelEssentials {
		private final PlatformModelEssentials pme;
		private final String ownerType;
		private final PmeKey key;

		public OwnerAwarePme(PlatformModelEssentials pme, String ownerType, PmeKey key) {
			this.pme = pme;
			this.ownerType = ownerType;
			this.key = key;
		}

		// @formatter:off
		@Override public GmMetaModel getModel() {	return pme.getModel(); }
		@Override public ManagedGmSession getModelSession() { return pme.getModelSession(); }
		@Override public ModelOracle getOracle() { return pme.getOracle(); }
		@Override public String getOwnerType() { return ownerType; }
		@Override public void addListener(ModelAccessoryListener modelAccessoryListener) { pme.addListener(modelAccessoryListener); }
		@Override public void outdated() { pme.outdated(); }
		@Override public PmeKey key() { return key; }
		// @formatter:on
	}

	public static class BasicPme implements PlatformModelEssentials {
		public final ManagedGmSession session;
		public final GmMetaModel model;
		public final PmeKey key;
		public /* lazy */ ModelOracle oracle;

		private final List<ModelAccessoryListener> listeners = Collections.synchronizedList(newList());

		public BasicPme(ManagedGmSession session, GmMetaModel model, PmeKey key) {
			this.session = session;
			this.model = model;
			this.key = key;
		}

		// @formatter:off
		@Override public GmMetaModel getModel() { return model; }
		@Override public String getOwnerType() { return null; }
		@Override public ManagedGmSession getModelSession() { return session; }
		@Override public void addListener(ModelAccessoryListener listener) { listeners.add(listener); }
		@Override public void outdated() { listeners.forEach(ModelAccessoryListener::onOutdated); }
		@Override public PmeKey key() { return key; }
		// @formatter:on

		@Override
		public ModelOracle getOracle() {
			if (oracle == null)
				synchronized (this) {
					if (oracle == null)
						oracle = new BasicModelOracle(model);
				}

			return oracle;
		}

	}

}
