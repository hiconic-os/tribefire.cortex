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

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.accessory.api.PlatformModelEssentials;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;
import com.braintribe.model.processing.meta.cmd.CmdResolverImpl;
import com.braintribe.model.processing.meta.cmd.context.aspects.RoleAspect;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;

/**
 * {@link ModelAccessory} created by {@link PlatformModelAccessoryFactory}
 */
/* package */ class PlatformModelAccessory extends ModelAccessoryBase {

	// constants
	private static final Logger log = Logger.getLogger(PlatformModelAccessory.class);

	// configurable
	private Supplier<PlatformModelEssentials> modelEssentialsSupplier;
	private BiConsumer<CmdResolverBuilder, PlatformModelEssentials> cmdConfigurer;
	private Supplier<Set<String>> userRolesProvider;

	// cached
	private volatile PlatformModelEssentials modelEssentials;
	private final String modelEssentialsLoadMonitor = new String("model-essentials-loading-lock");

	public void setModelEssentialsSupplier(Supplier<PlatformModelEssentials> modelEssentialsSupplier) {
		this.modelEssentialsSupplier = modelEssentialsSupplier;
	}

	public void setCmdConfigurer(BiConsumer<CmdResolverBuilder, PlatformModelEssentials> cmdConfigurer) {
		this.cmdConfigurer = cmdConfigurer;
	}

	public void setUserRolesProvider(Supplier<Set<String>> userRolesProvider) {
		this.userRolesProvider = userRolesProvider;
	}

	@Override
	protected void build() {
		PlatformModelEssentials modelEssentials = getModelEssentials();

		modelSession = modelEssentials.getModelSession();
		modelOracle = modelEssentials.getOracle();

		CmdResolverBuilder rcb = CmdResolverImpl.create(modelOracle) //
				.addDynamicAspectProvider(RoleAspect.class, userRolesProvider) //
				.setSessionProvider(getSessionProvider());

		if (cmdConfigurer != null)
			cmdConfigurer.accept(rcb, modelEssentials);

		cmdResolver = rcb.done();

		log.debug(() -> "Built " + this);
	}

	public PlatformModelEssentials getModelEssentials() {
		if (modelEssentials == null)
			synchronized (modelEssentialsLoadMonitor) {
				if (modelEssentials == null) {
					modelEssentials = modelEssentialsSupplier.get();
					modelEssentials.addListener(this::onModelEssentialsOutdated);
				}
			}

		return modelEssentials;
	}

	private void onModelEssentialsOutdated() {
		if (modelEssentials == null)
			return;

		log.debug(() -> "Notifying onOutdated from modelEssentials: " + modelEssentials + " to " + PlatformModelAccessory.this);
		modelEssentials = null;
		outdated();
	}

	@Override
	public void outdated() {
		if (modelEssentials != null) {
			// we set modelEssentials to null so that when it notifies us back via onModelEssentialsOutdated we ignore the call
			PlatformModelEssentials essentials = modelEssentials;
			modelEssentials = null;
			essentials.outdated();
		}

		super.outdated();
	}

}
