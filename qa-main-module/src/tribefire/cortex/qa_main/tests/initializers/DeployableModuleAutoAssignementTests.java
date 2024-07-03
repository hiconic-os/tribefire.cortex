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
package tribefire.cortex.qa_main.tests.initializers;

import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;
import static com.braintribe.testing.junit.assertions.gm.assertj.core.api.GmAssertions.assertThat;
import static tribefire.cortex.qa.CortexQaCommons.mainModuleName;
import static tribefire.cortex.qa.CortexQaCommons.spGlobalId;
import static tribefire.cortex.qa.CortexQaCommons.subModuleName;

import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.Module;
import com.braintribe.model.extensiondeployment.ServiceProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

import tribefire.cortex.model.MainAndOtherSerp;
import tribefire.cortex.model.MainAndSubSerp;
import tribefire.cortex.model.MainOnlySerp;
import tribefire.cortex.model.QaSerp;
import tribefire.cortex.model.SubOnlySerp;
import tribefire.cortex.qa.CortexQaCommons;
import tribefire.cortex.qa_main.tests.PlatformHolder;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * @author peter.gazdik
 */
public class DeployableModuleAutoAssignementTests {

	private TribefireWebPlatformContract tfPlatform;

	@Before
	public void setup() {
		tfPlatform = PlatformHolder.platformContract;
	}

	// Tests temporarily disabled due to Jinni backwards compatibility issues
	//@Test
	public void deduceForUnambiguiousTypes() throws Exception {
		PersistenceGmSession session = tfPlatform.systemUserRelated().cortexSessionSupplier().get();

		assertRightModule(session, MainOnlySerp.T, mainModuleName);
		assertRightModule(session, SubOnlySerp.T, subModuleName);
	}

	//@Test
	public void deduceAsOnlyOneDepSupports() throws Exception {
		PersistenceGmSession session = tfPlatform.systemUserRelated().cortexSessionSupplier().get();

		assertRightModule(session, MainAndOtherSerp.T, mainModuleName);
		assertRightModule(session, MainAndOtherSerp.T, CortexQaCommons.otherModuleName);
		assertRightModule(session, MainAndSubSerp.T, subModuleName);
	}

	@Test
	public void cannotDeduceWhenAmbiguous() throws Exception {
		PersistenceGmSession session = tfPlatform.systemUserRelated().cortexSessionSupplier().get();

		ServiceProcessor sp = session.findEntityByGlobalId(spGlobalId(MainAndSubSerp.T, mainModuleName));
		assertThat(sp.getModule()).isNull();
	}

	private void assertRightModule(PersistenceGmSession session, EntityType<? extends QaSerp> spType, String moduleName) {
		ServiceProcessor sp = session.findEntityByGlobalId(spGlobalId(spType, moduleName));
		assertModule(sp, moduleName);
	}

	private void assertModule(Deployable d, String moduleName) {
		Module module = d.getModule();
		assertThat(module).isNotNull();
		assertThat(module.getName()).isEqualTo(moduleName);
	}

}
