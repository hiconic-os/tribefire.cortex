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
package tribefire.platform.wire.space.cortex.accesses;

import static com.braintribe.wire.api.util.Lists.list;

import java.util.List;

import com.braintribe.model.access.collaboration.CollaborativeSmoodAccess;
import com.braintribe.model.processing.aop.api.aspect.AccessAspect;
import com.braintribe.model.processing.platformsetup.PlatformSetupStateChangeProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessorRule;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.impl.setup.PlatformSetupSupplier;
import tribefire.platform.wire.space.cortex.accesses.TribefireProductModels.TribefireProductModel;
import tribefire.platform.wire.space.cortex.deployment.StateChangeProcessorsSpace;

@Managed
public class PlatformSetupAccessSpace extends CollaborativeSystemAccessSpaceBase {

	private static final String id = TribefireConstants.ACCESS_SETUP;
	private static final String name = "Platform Setup";

	private static final TribefireProductModel model = TribefireProductModels.platformSetupAccessModel;
	private static final String modelName = model.modelName;
	private static final String serviceModelName = "tribefire.cortex:platform-setup-service-model";

	@Import
	private StateChangeProcessorsSpace stateChangeProcessors;

	// @formatter:off
	@Override public String id() { return id; }
	@Override public String name() { return name; }
	@Override public String modelName() { return modelName; }
	@Override public String serviceModelName() { return serviceModelName; }
	// @formatter:on

	@Override
	protected List<AccessAspect> aopAspects() {
		return list( //
				aspects.security(), //
				aspects.fulltext(), //
				stateProcessingAspect() //
		);
	}

	/* IMPORTANT: This should not be @Managed, the returned bean is already managed. */
	@Override
	public CollaborativeSmoodAccess access() {
		return systemAccessCommons.collaborativeSmoodAccess(id());
	}

	@Override
	protected List<StateChangeProcessorRule> stateChangeProcessorRules() {
		return list( //
				stateChangeProcessors.bidiProperty(), //
				new PlatformSetupStateChangeProcessor() //
		);
	}

	@Override
	public HardwiredAccessSpaceBase workbenchAccessSpace() {
		return systemAccesses.platformSetupWorkbench();
	}

	@Managed
	public PlatformSetupSupplier platformSetupSupplier() {
		PlatformSetupSupplier bean = new PlatformSetupSupplier();
		bean.setSetupSessionFactory(sessionProvider());
		return bean;
	}

}
