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
package tribefire.platform.impl.configuration;

import org.junit.Assert;
import org.junit.Test;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public class EnvironmentDenotationRegistryTest {

	@Test
	public void testRegisterLookup() {
		
		TestDenotationType registeredDenotationType = TestDenotationType.T.create();
		
		EnvironmentDenotationRegistry.getInstance().register("testRegisterLookup", registeredDenotationType);
		
		GenericEntity lookedUpDenotationType = EnvironmentDenotationRegistry.getInstance().lookup("testRegisterLookup");
		
		Assert.assertEquals("Unexpected denotation type fetched", registeredDenotationType, lookedUpDenotationType);
		
	}
	
	@Test
	public void testMultipleRegisterLookup() {
		
		int totalDenotationTypes = 100;
		
		GenericEntity[] registered = new GenericEntity[totalDenotationTypes];
		
		for (int i = 0; i < totalDenotationTypes; i++) {
			registered[i] = TestDenotationType.T.create();
		}
		
		for (int i = 0; i < totalDenotationTypes; i++) {
			EnvironmentDenotationRegistry.getInstance().register("testMultipleRegisterLookup"+i, registered[i]);
		}
		
		for (int i = 0; i < totalDenotationTypes; i++) {
			Assert.assertEquals("Unexpected denotation type fetched", registered[i], EnvironmentDenotationRegistry.getInstance().lookup("testMultipleRegisterLookup"+i));
		}
		
	}
	
	@Test
	public void testOverwriteRegister() {
		
		TestDenotationType registeredType1 = TestDenotationType.T.create();
		TestDenotationType registeredType2 = TestDenotationType.T.create();
		
		EnvironmentDenotationRegistry.getInstance().register("testOverwriteRegister", registeredType1);
		EnvironmentDenotationRegistry.getInstance().register("testOverwriteRegister", registeredType2);

		Assert.assertEquals("Unexpected denotation type fetched", registeredType2, EnvironmentDenotationRegistry.getInstance().lookup("testOverwriteRegister"));
		
	}
	
	@Test
	public void testNotFoundLookup() {

		GenericEntity nullDenotationType = EnvironmentDenotationRegistry.getInstance().lookup("FAKE");
		
		Assert.assertNull("Looking up for [ FAKE ] should have returned null", nullDenotationType);
		
	}
	
	public static interface TestDenotationType extends GenericEntity {
		
		EntityType<EnvironmentDenotationRegistryTest.TestDenotationType> T = EntityTypes
				.T(EnvironmentDenotationRegistryTest.TestDenotationType.class);
	}
	

}
