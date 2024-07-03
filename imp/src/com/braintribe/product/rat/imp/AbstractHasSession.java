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
package com.braintribe.product.rat.imp;

import java.util.HashSet;
import java.util.Set;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.impl.utils.QueryHelper;
import com.braintribe.utils.lcd.Arguments;

/**
 * Base class for any class in this artifact that has a session (so almost every one).
 */
public abstract class AbstractHasSession implements HasSession {
	private final static Set<PersistenceGmSession> invalidatedSessions = new HashSet<>();

	// Makes all log output look like it came from ImpApi class to hide implementation details from user in log output
	protected final static Logger logger = Logger.getLogger(ImpApi.class);

	private final PersistenceGmSession session;
	protected final QueryHelper queryHelper;

	public AbstractHasSession(PersistenceGmSession session) {
		Arguments.notNullWithName("session", session);
		this.session = session;
		queryHelper = new QueryHelper(session);
	}

	protected void invalidateSession() {
		invalidatedSessions.add(session);
	}

	@Override
	public PersistenceGmSession session() {
		if (invalidatedSessions.contains(session)) {
			throw new ImpException("This imp's session is no longer valid. Please create a new imp, i.e. using ImpApiFactory");
		}

		return session;
	}

	@Override
	public void commit() {
		session().commit();
	}
}
