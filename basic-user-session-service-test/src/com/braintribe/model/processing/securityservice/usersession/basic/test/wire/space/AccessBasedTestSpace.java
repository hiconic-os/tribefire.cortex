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
package com.braintribe.model.processing.securityservice.usersession.basic.test.wire.space;

import java.util.function.Supplier;

import com.braintribe.common.MutuallyExclusiveReadWriteLock;
import com.braintribe.gm.model.usersession.PersistenceUserSession;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.securityservice.api.UserSessionService;
import com.braintribe.model.processing.securityservice.usersession.service.AccessUserSessionService;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.impl.persistence.BasicPersistenceGmSession;
import com.braintribe.model.processing.smood.Smood;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public class AccessBasedTestSpace extends BaseTestSpace {

	@Override
	public UserSessionService userSessionService() {
		return incrementalAccessService();
	}

	@Managed
	private UserSessionService incrementalAccessService() {
		AccessUserSessionService bean = new AccessUserSessionService();

		bean.setPersistenceUserSessionGmSessionProvider(this::lowLevelSession);
		bean.setSessionIdProvider(userSessionIdFactory());
		bean.setDefaultUserSessionMaxIdleTime(defaultMaxIdleTime());

		return bean;
	}

	private PersistenceGmSession lowLevelSession() {
		BasicPersistenceGmSession bean = new BasicPersistenceGmSession();
		bean.setIncrementalAccess(rawAccess());
		return bean;
	}

	@Managed
	private IncrementalAccess rawAccess() {
		Smood bean = new Smood(new MutuallyExclusiveReadWriteLock());
		bean.setAccessId("user-sessions");
		return bean;
	}

	@Managed
	private Supplier<GmMetaModel> metaModelProvider() {
		return () -> PersistenceUserSession.T.getModel().getMetaModel();
	}

}
