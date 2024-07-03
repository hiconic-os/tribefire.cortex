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
package tribefire.cortex.qa;

import java.util.function.Supplier;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.cortex.model.QaSerp;

/**
 * @author peter.gazdik
 */
public class CortexQaCommons {

	public static final String mainModuleName = "qa-main-module";
	public static final String subModuleName = "qa-sub-module";
	public static final String otherModuleName = "qa-other-module";
	
	public static <T> Supplier<T> qaServiceExpertSupplier(EntityType<? extends Deployable> deployableType) {
		return () -> {
			throw new UnsupportedOperationException("Unexpected deployment of: " + deployableType.getShortName());
		};
	}

	public static void createServiceProcessor(ManagedGmSession session, EntityType<? extends QaSerp> spType, String origin) {
		QaSerp sp = session.createRaw(spType, spGlobalId(spType, origin));
		sp.setName( spType.getShortName() + " von " + origin);
		sp.setExternalId(spType.getShortName() + "/" + origin);
	}

	public static String spGlobalId(EntityType<? extends QaSerp> spType, String origin) {
		return "qa.serviceProcessor." + origin + "." + spType.getShortName();
	}


}
