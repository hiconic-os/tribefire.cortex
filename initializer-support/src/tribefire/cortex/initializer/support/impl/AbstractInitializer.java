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
package tribefire.cortex.initializer.support.impl;

import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.utils.lcd.StopWatch;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.api.InitializerContextBuilder;
import tribefire.cortex.initializer.support.api.WiredInitializerContext;
import tribefire.module.wire.contract.TribefireModuleContract;

/**
 * Basis for a {@link WireModule} for an initializer (module), which binds {@link PrimingModuleSpace} as the implementation for the
 * {@link TribefireModuleContract}, and also binds itself as the {@link AbstractInitializerContract} used in that module.
 * <p>
 * When a CSA is initializing via this initializer, the flow is:
 * <ul>
 * <li>CSA calls {@link PrimingModuleSpace#initialize(PersistenceInitializationContext)}
 * <li>This then calls {@link #initialize(PersistenceInitializationContext, WireContext)}
 * <li>Inside this initialize method a nested {@link WiredInitializerContext} is created.
 * <li>This is nested context also depends on {@link #getInitializerWireModule()}, which returns the wire module that binds the entity bean spaces.
 * <li>The nested context and it's main contract are then passed to the
 * {@link #initialize(PersistenceInitializationContext, WiredInitializerContext, WireSpace)} method.
 * <li>This method then typically just calls the initialize() method on the passed main contract.
 * </ul>
 * 
 * @param <C>
 *            {@link WireContext#contract() Main contract} of the initializer-specific wire context.
 *            <p>
 *            Note that the initializer-specific context uses the regular module-specific wire context as parent, thus all its contracts are
 *            accessible.
 * 
 * @see AbstractInitializerContract
 */
public abstract class AbstractInitializer<C extends WireSpace> implements AbstractInitializerContract, WireModule {

	private static final Logger log = Logger.getLogger(AbstractInitializer.class);

	@Override
	public final void initialize(PersistenceInitializationContext context, WireContext<?> wireContext) {
		StopWatch sw = new StopWatch();

		WireTerminalModule<C> initializerModule = getInitializerWireModule();
		String initializerId = initializerModule.getClass().getSimpleName();

		WiredInitializerContext<C> initializerContext = InitializerContextBuilder.build(initializerModule, initializerId, context, wireContext);

		try (GlobalIdManager globalIdManager = new GlobalIdManager(initializerId, context.getSession(), initializerContext.wireContext())) {
			initialize(context, initializerContext, initializerContext.contract());

			globalIdManager.ensureGlobalIds();
		} catch (Exception e) {
			throw new ManipulationPersistenceException(e);
		} finally {
			initializerContext.wireContext().shutdown();
		}

		log.debug(() -> "Initializer [" + getClass().getSimpleName() + "] took " + sw.getElapsedTime() + " ms. (" + getClass().getPackage().getName()
				+ ")");
	}

	@Override
	public final void configureContext(WireContextBuilder<?> contextBuilder) {
		contextBuilder.bindContract(TribefireModuleContract.class, PrimingModuleSpace.class);
		contextBuilder.bindContract(AbstractInitializerContract.class, this);
	}

	protected abstract WireTerminalModule<C> getInitializerWireModule();
	protected abstract void initialize(PersistenceInitializationContext context, WiredInitializerContext<C> initializerContext,
			C initializerContract);

	/* final methods to block overrides that to not match the soley purpose of the persistence initializer usecase */

	@Override
	public final void onLoaded(WireContextConfiguration configuration) {
		AbstractInitializerContract.super.onLoaded(configuration);
	}

	@Override
	public final List<WireModule> dependencies() {
		return WireModule.super.dependencies();
	}
}