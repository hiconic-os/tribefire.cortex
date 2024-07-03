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
package com.braintribe.model.processing.aspect.crypto.test.interceptor.manipulation;

import org.junit.Test;

import com.braintribe.model.processing.aspect.crypto.test.commons.model.Encrypted;
import com.braintribe.model.processing.aspect.crypto.test.commons.model.EncryptedMulti;
import com.braintribe.model.processing.aspect.crypto.test.commons.model.Hashed;
import com.braintribe.model.processing.aspect.crypto.test.commons.model.HashedMulti;
import com.braintribe.model.processing.aspect.crypto.test.commons.model.Mixed;
import com.braintribe.model.processing.aspect.crypto.test.commons.model.MixedMulti;
import com.braintribe.model.processing.aspect.crypto.test.interceptor.CryptoInterceptorTestBase;

public class CryptoManipulationInterceptorTest extends CryptoInterceptorTestBase {

	@Test
	public void testEncryptedCreation() throws Exception {
		testCreation(Encrypted.T);
	}
	
	@Test
	public void testHashedCreation() throws Exception {
		testCreation(Hashed.T);
	}

	@Test
	public void testMixedCreation() throws Exception {
		testCreation(Mixed.T);
	}
	
	@Test
	public void testEncryptedMultiCreation() throws Exception {
		testCreation(EncryptedMulti.T);
	}
	
	@Test
	public void testHashedMultiCreation() throws Exception {
		testCreation(HashedMulti.T);
	}

	@Test
	public void testMixedMultiCreation() throws Exception {
		testCreation(MixedMulti.T);
	}

	@Test
	public void testEncryptedUpdate() throws Exception {
		testUpdate(Encrypted.T);
	}

	@Test
	public void testHashedUpdate() throws Exception {
		testUpdate(Hashed.T);
	}

	@Test
	public void testMixedUpdate() throws Exception {
		testUpdate(Mixed.T);
	}
	
	@Test
	public void testEncryptedMultiUpdate() throws Exception {
		testUpdate(EncryptedMulti.T);
	}
	
	@Test
	public void testHashedMultiUpdate() throws Exception {
		testUpdate(HashedMulti.T);
	}

	@Test
	public void testMixedMultiUpdate() throws Exception {
		testUpdate(MixedMulti.T);
	}

}
