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
package com.braintribe.model.processing.exchange;

import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public abstract class AbstractExchangePayloadContext implements ExchangePayloadContext {
	
	private PersistenceGmSession systemSession;
	private PersistenceGmSession session;

	public AbstractExchangePayloadContext(PersistenceGmSession session, PersistenceGmSession systemSession) {
		this.session = session;
		this.systemSession = systemSession;
	}

	@Override
	public PersistenceGmSession getSession() {
		return session;
	}

	@Override
	public PersistenceGmSession getSystemSession() {
		return systemSession;
	}

}
