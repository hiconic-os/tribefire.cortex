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
package tribefire.platform.impl.request;

import java.util.Set;
import java.util.function.Supplier;

import com.braintribe.cfg.Required;
import com.braintribe.model.access.AccessService;
import com.braintribe.model.access.AccessServiceException;
import com.braintribe.model.accessapi.AccessDomain;
import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.accessapi.ModelEnvironment;
import com.braintribe.model.accessapi.ModelEnvironmentServices;
import com.braintribe.model.accessapi.ReferencesRequest;
import com.braintribe.model.accessapi.ReferencesResponse;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.securityservice.commons.provider.StaticUserSessionHolder;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.EntityQueryResult;
import com.braintribe.model.query.PropertyQuery;
import com.braintribe.model.query.PropertyQueryResult;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.query.SelectQueryResult;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.utils.collection.api.MinimalStack;

/**
 * @author peter.gazdik
 */
public class InternalAccessService implements AccessService {

	private AccessService delegate;
	private Supplier<UserSession> internalSessionProvider;
	private MinimalStack<UserSession> currentUserSessionStack;

	@Required
	public void setDelegate(AccessService delegate) {
		this.delegate = delegate;
	}

	@Required
	public void setInternalSessionProvider(StaticUserSessionHolder internalSessionProvider) {
		this.internalSessionProvider = internalSessionProvider;
	}

	@Required
	public void setCurrentUserSessionStack(MinimalStack<UserSession> currentUserSessionStack) {
		this.currentUserSessionStack = currentUserSessionStack;
	}

	@Override
	public ModelEnvironment getModelEnvironment(String accessId) throws AccessServiceException {
		before();
		try {
			return delegate.getModelEnvironment(accessId);
		} finally {
			after();
		}
	}

	@Override
	public ModelEnvironment getModelAndWorkbenchEnvironment(String accessId) throws AccessServiceException {
		before();
		try {
			return delegate.getModelAndWorkbenchEnvironment(accessId);
		} finally {
			after();
		}
	}

	@Override
	public ModelEnvironment getModelAndWorkbenchEnvironment(String accessId, Set<String> workbenchPerspectiveNames) throws AccessServiceException {
		before();
		try {
			return delegate.getModelAndWorkbenchEnvironment(accessId, workbenchPerspectiveNames);
		} finally {
			after();
		}
	}

	@Override
	public ModelEnvironment getModelEnvironmentForDomain(String accessId, AccessDomain accessDomain) throws AccessServiceException {
		before();
		try {
			return delegate.getModelEnvironmentForDomain(accessId, accessDomain);
		} finally {
			after();
		}
	}

	@Override
	public ModelEnvironmentServices getModelEnvironmentServices(String accessId) throws AccessServiceException {
		before();
		try {
			return delegate.getModelEnvironmentServices(accessId);
		} finally {
			after();
		}
	}

	@Override
	public ModelEnvironmentServices getModelEnvironmentServicesForDomain(String accessId, AccessDomain accessDomain) throws AccessServiceException {
		before();
		try {
			return delegate.getModelEnvironmentServicesForDomain(accessId, accessDomain);
		} finally {
			after();
		}
	}

	@Override
	public Set<String> getAccessIds() throws AccessServiceException {
		before();
		try {
			return delegate.getAccessIds();
		} finally {
			after();
		}
	}

	@Override
	public GmMetaModel getMetaModel(String accessId) throws AccessServiceException {
		before();
		try {
			return delegate.getMetaModel(accessId);
		} finally {
			after();
		}
	}

	@Override
	public GmMetaModel getMetaModelForTypes(Set<String> typeSignatures) throws AccessServiceException {
		before();
		try {
			return delegate.getMetaModelForTypes(typeSignatures);
		} finally {
			after();
		}
	}

	@Override
	public SelectQueryResult query(String accessId, SelectQuery query) throws AccessServiceException {
		before();
		try {
			return delegate.query(accessId, query);
		} finally {
			after();
		}
	}

	@Override
	public PropertyQueryResult queryProperty(String accessId, PropertyQuery request) throws AccessServiceException {
		before();
		try {
			return delegate.queryProperty(accessId, request);
		} finally {
			after();
		}
	}

	@Override
	public EntityQueryResult queryEntities(String accessId, EntityQuery request) throws AccessServiceException {
		before();
		try {
			return delegate.queryEntities(accessId, request);
		} finally {
			after();
		}
	}

	@Override
	public ManipulationResponse applyManipulation(String accessId, ManipulationRequest manipulationRequest) throws AccessServiceException {
		before();
		try {
			return delegate.applyManipulation(accessId, manipulationRequest);
		} finally {
			after();
		}
	}

	@Override
	public ReferencesResponse getReferences(String accessId, ReferencesRequest referencesRequest) throws AccessServiceException {
		before();
		try {
			return delegate.getReferences(accessId, referencesRequest);
		} finally {
			after();
		}
	}

	@Override
	public Set<String> getPartitions(String accessId) throws AccessServiceException {
		before();
		try {
			return delegate.getPartitions(accessId);
		} finally {
			after();
		}
	}

	private void before() {
		UserSession session = internalSessionProvider.get();
		currentUserSessionStack.push(session);
	}

	private void after() {
		currentUserSessionStack.pop();
	}

}
