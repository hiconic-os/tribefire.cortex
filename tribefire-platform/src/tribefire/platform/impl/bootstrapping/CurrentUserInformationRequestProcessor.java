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
package tribefire.platform.impl.bootstrapping;

import com.braintribe.model.bapi.CurrentUserInformationRequest;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessorException;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.common.context.UserSessionAspect;
import com.braintribe.model.user.User;
import com.braintribe.model.usersession.UserSession;

/**
 * TODO: Move to the upcoming SecurityServiceProcessor
 */
public class CurrentUserInformationRequestProcessor implements ServiceProcessor<CurrentUserInformationRequest, User> {

	// This is now a copy of SecurityServiceProcessor.getCurrentUser(...)
	@Override
	public User process(ServiceRequestContext requestContext, CurrentUserInformationRequest request) throws ServiceProcessorException {
		return requestContext.findAttribute(UserSessionAspect.class).map(UserSession::getUser).orElse(null);
	}

}
