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
package com.braintribe.model.access.security.manipulation.experts;

import org.junit.Test;

import com.braintribe.exception.AuthorizationException;
import com.braintribe.model.access.security.common.AbstractSecurityAspectTest;
import com.braintribe.model.access.security.testdata.query.EntityWithProps;
import com.braintribe.model.processing.session.impl.persistence.BasicPersistenceGmSession;

public class ModelVisibilityTests extends AbstractSecurityAspectTest {

	@Test
	public void visibleModelOk() throws Exception {
		BasicPersistenceGmSession aopSession = newAopSession();
		aopSession.create(EntityWithProps.T);
		aopSession.commit();
	}

	@Test(expected = AuthorizationException.class)
	public void nonVisibleModelNotOk() throws Exception {
		setUserRoles(MODEL_IGNORER);

		BasicPersistenceGmSession aopSession = newAopSession();
		aopSession.create(EntityWithProps.T);
		aopSession.commit();
	}

}
