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
package com.braintribe.model.processing.securityservice.basic.test.wire.space.access;

import java.util.function.Supplier;

import com.braintribe.common.MutuallyExclusiveReadWriteLock;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.impl.persistence.BasicPersistenceGmSession;
import com.braintribe.model.processing.smood.Smood;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public abstract class SystemAccessSpaceBase implements WireSpace {

	public abstract String id();
	
	public abstract String modelName();

	public String serviceModelName() {
		return null;
	}

	public abstract IncrementalAccess rawAccess();

	//@Managed(Scope.prototype)
	public PersistenceGmSession lowLevelSession() {
		BasicPersistenceGmSession bean = new BasicPersistenceGmSession();
		bean.setIncrementalAccess(rawAccess());
		return bean;
	}

	protected Smood smood() {
		Smood bean = new Smood(new MutuallyExclusiveReadWriteLock());
		bean.setAccessId(id());
		return bean;
	}

	@Managed
	public Supplier<GmMetaModel> metaModelProvider() {
		return () -> GMF.getTypeReflection().getModel(modelName()).getMetaModel();
	}

}
