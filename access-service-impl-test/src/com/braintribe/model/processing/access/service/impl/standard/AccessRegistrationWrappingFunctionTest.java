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
package com.braintribe.model.processing.access.service.impl.standard;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.processing.access.service.api.registry.AccessRegistrationInfo;
import com.braintribe.model.processing.access.service.impl.standard.OriginAwareAccessRegistrationInfo.Origin;
import static org.mockito.Mockito.*;
/**
 * Tests for {@link AccessRegistrationWrappingFunction}
 * 
 * 
 */
public class AccessRegistrationWrappingFunctionTest {

	private AccessRegistrationWrappingFunction function;
	private static final Origin ORIGIN = Origin.REGISTRATION;

	@Before
	public void setUp() throws Exception {
		function = new AccessRegistrationWrappingFunction(ORIGIN);
	}

	@Test
	public void testWrapping() throws Exception {
		IncrementalAccess access  = mock(IncrementalAccess.class);
		
		AccessRegistrationInfo regInfo = new AccessRegistrationInfo();
		regInfo.setAccess(access);

		OriginAwareAccessRegistrationInfo result = function.apply(regInfo);
		IncrementalAccess actualAccess = result.getAccess();
		Assertions.assertThat(actualAccess).isEqualTo(access);
		Origin actualOrigin = result.getOrigin();
		Assertions.assertThat(actualOrigin).isEqualTo(ORIGIN);
	}
}
