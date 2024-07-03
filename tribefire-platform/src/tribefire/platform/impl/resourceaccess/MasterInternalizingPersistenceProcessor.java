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
package tribefire.platform.impl.resourceaccess;

import java.util.Set;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.access.ModelAccessException;
import com.braintribe.model.accessapi.CustomPersistenceRequest;
import com.braintribe.model.accessapi.GetModel;
import com.braintribe.model.accessapi.GetPartitions;
import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.accessapi.QueryAndSelect;
import com.braintribe.model.accessapi.QueryEntities;
import com.braintribe.model.accessapi.QueryProperty;
import com.braintribe.model.accessapi.ReferencesRequest;
import com.braintribe.model.accessapi.ReferencesResponse;
import com.braintribe.model.aopaccessapi.AccessAspectAroundProceedRequest;
import com.braintribe.model.aopaccessapi.AccessAspectAroundProceedResponse;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.aop.api.service.AopIncrementalAccess;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.resource.ResourceAccess;
import com.braintribe.model.processing.session.api.resource.ResourceAccessFactory;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.EntityQueryResult;
import com.braintribe.model.query.PropertyQuery;
import com.braintribe.model.query.PropertyQueryResult;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.query.SelectQueryResult;
import com.braintribe.model.service.api.ServiceRequest;

import tribefire.platform.api.MasterIncrementalAccess;

public class MasterInternalizingPersistenceProcessor extends AbstractDispatchingServiceProcessor<ServiceRequest, Object> implements MasterIncrementalAccess {

	private final ResourceAccessFactory<? super PersistenceGmSession> delegateResourceAccessFactory;
	private final AopIncrementalAccess delegate;

	public MasterInternalizingPersistenceProcessor(AopIncrementalAccess aopIncrementalAccess, ResourceAccessFactory<? super PersistenceGmSession> delegateResourceAccessFactory) {
		this.delegate = aopIncrementalAccess;
		this.delegateResourceAccessFactory = delegateResourceAccessFactory;
	}
	
	@Override
	protected void configureDispatching(DispatchConfiguration<ServiceRequest, Object> dispatching) {
		dispatching.register(CustomPersistenceRequest.T, delegate::processCustomRequest);
		dispatching.register(GetModel.T, (c,r) -> delegate.getMetaModel());
		dispatching.register(AccessAspectAroundProceedRequest.T, (c,r) -> delegate.proceedAround(r));
		dispatching.register(QueryAndSelect.T, (c,r) -> delegate.query(r.getQuery()));
		dispatching.register(QueryEntities.T, (c,r) -> delegate.queryEntities(r.getQuery()));
		dispatching.register(QueryProperty.T, (c,r) -> delegate.queryProperty(r.getQuery()));
		dispatching.register(ManipulationRequest.T, (c,r) -> delegate.applyManipulation(r));
		dispatching.register(ReferencesRequest.T, (c,r) -> delegate.getReferences(r));
		dispatching.register(GetPartitions.T, (c,r) -> delegate.getPartitions());
	}

	@Override
	public String getAccessId() {
		return delegate.getAccessId();
	}

	@Override
	public GmMetaModel getMetaModel() {
		return delegate.getMetaModel();
	}

	@Override
	public AccessAspectAroundProceedResponse proceedAround(AccessAspectAroundProceedRequest request) throws ModelAccessException {
		return delegate.proceedAround(request);
	}

	@Override
	public SelectQueryResult query(SelectQuery query) throws ModelAccessException {
		return delegate.query(query);
	}

	@Override
	public EntityQueryResult queryEntities(EntityQuery request) throws ModelAccessException {
		return delegate.queryEntities(request);
	}

	@Override
	public PropertyQueryResult queryProperty(PropertyQuery request) throws ModelAccessException {
		return delegate.queryProperty(request);
	}

	@Override
	public ManipulationResponse applyManipulation(ManipulationRequest manipulationRequest) throws ModelAccessException {
		return delegate.applyManipulation(manipulationRequest);
	}

	@Override
	public ReferencesResponse getReferences(ReferencesRequest referencesRequest) throws ModelAccessException {
		return delegate.getReferences(referencesRequest);
	}

	@Override
	public Set<String> getPartitions() throws ModelAccessException {
		return delegate.getPartitions();
	}

	@Override
	public ResourceAccess newInstance(PersistenceGmSession session) {
		return delegateResourceAccessFactory.newInstance(session);
	}

	@Override
	public IncrementalAccess getDelegate() {
		return delegate.getDelegate();
	}
}
