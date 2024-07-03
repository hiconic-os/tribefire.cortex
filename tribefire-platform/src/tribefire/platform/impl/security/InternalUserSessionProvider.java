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
package tribefire.platform.impl.security;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.usersession.UserSession;

public class InternalUserSessionProvider implements Supplier<UserSession>, Consumer<UserSession> {

	private UserSession userSession;
	
	private static final Logger log = Logger.getLogger(InternalUserSessionProvider.class);

	@Required
	@Configurable
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	@Override
	public UserSession get() throws RuntimeException {
		return userSession;
	}

	@Override
	public void accept(UserSession newUserSession) throws RuntimeException {

		UserSession previousUserSession = this.userSession;
		this.userSession = newUserSession;
		
		if (log.isDebugEnabled()) {
			log.debug("Internal user session provider updated. Replaced previous [ "+previousUserSession+" ] with [ "+this.userSession+" ]");
		}
		
	}

}
