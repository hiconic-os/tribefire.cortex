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
package tribefire.module.wire.contract;

import java.util.Set;
import java.util.function.Supplier;

import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.processing.session.api.persistence.auth.SessionAuthorization;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * @author peter.gazdik
 */
public interface UserRelated {

	Evaluator<ServiceRequest> evaluator();

	PersistenceGmSessionFactory sessionFactory();

	Supplier<SessionAuthorization> sessionAuthorizationSupplier();

	ModelAccessoryFactory modelAccessoryFactory();

	Supplier<String> userSessionIdSupplier();

	Supplier<String> userNameSupplier();

	Supplier<Set<String>> userRolesSupplier();

	/**
	 * Returns a cortex session factory. Calling a {@link Supplier#get() get} on the returned instance is equivalent to
	 * <code>sessionFactory().newSession("cortex")</code>.
	 */
	Supplier<PersistenceGmSession> cortexSessionSupplier();

	Supplier<ModelAccessory> cortexModelAccessorySupplier();

}
