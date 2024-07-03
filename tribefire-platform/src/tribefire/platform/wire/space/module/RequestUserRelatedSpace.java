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
package tribefire.platform.wire.space.module;

import java.util.Set;
import java.util.function.Supplier;

import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.processing.session.api.persistence.auth.SessionAuthorization;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.utils.collection.api.MinimalStack;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.RequestUserRelatedContract;
import tribefire.platform.wire.space.MasterResourcesSpace;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;
import tribefire.platform.wire.space.rpc.RpcSpace;
import tribefire.platform.wire.space.security.CurrentUserAuthContextSpace;

/**
 * @author peter.gazdik
 */
@Managed
public class RequestUserRelatedSpace extends UserRelatedSpace implements RequestUserRelatedContract {

	@Import
	protected MasterResourcesSpace resources;

	@Import
	private GmSessionsSpace gmSessions;

	@Import
	private RpcSpace rpc;

	@Import
	private CurrentUserAuthContextSpace currentUserAuth;

	@Override
	public Evaluator<ServiceRequest> evaluator() {
		return rpc.serviceRequestEvaluator();
	}

	@Override
	public PersistenceGmSessionFactory sessionFactory() {
		return gmSessions.sessionFactory();
	}

	@Override
	public Supplier<SessionAuthorization> sessionAuthorizationSupplier() {
		return gmSessions.sessionAuthorizationProvider();
	}

	@Override
	public ModelAccessoryFactory modelAccessoryFactory() {
		return gmSessions.userModelAccessoryFactory();
	}

	@Override
	public MinimalStack<UserSession> userSessionStack() {
		return currentUserAuth.userSessionStack();
	}

	@Override
	public Supplier<String> userSessionIdSupplier() {
		return currentUserAuth.userSessionIdProvider();
	}

	@Override
	public Supplier<String> userNameSupplier() {
		return currentUserAuth.userNameProvider();
	}

	@Override
	public Supplier<Set<String>> userRolesSupplier() {
		return currentUserAuth.rolesProvider();
	}

}
