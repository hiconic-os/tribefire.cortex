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
package tribefire.platform.wire.space.cortex.accesses;

import java.util.function.Supplier;

import com.braintribe.cartridge.common.processing.deployment.ReflectBeansForDeployment;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.processing.aop.api.service.AopIncrementalAccess;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.SessionFactoryBasedSessionProvider;
import com.braintribe.model.processing.session.impl.persistence.BasicPersistenceGmSession;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.MasterResourcesSpace;
import tribefire.platform.wire.space.bindings.BindingsSpace;
import tribefire.platform.wire.space.common.BindersSpace;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;

@Managed
public abstract class SystemAccessSpaceBase implements WireSpace, ReflectBeansForDeployment {

	// @formatter:off
	@Import protected BindingsSpace bindings;
	@Import protected BindersSpace binders;
	@Import protected GmSessionsSpace gmSessions;
	@Import protected MasterResourcesSpace resources;

	public final String globalId() { return "hardwired:access/" + id(); }
	public abstract String id();
	public abstract String name();
	public abstract String modelName();
	public String serviceModelName() { return null; }

	// @formatter:on

	@Managed
	public Supplier<PersistenceGmSession> sessionProvider() {
		SessionFactoryBasedSessionProvider bean = new SessionFactoryBasedSessionProvider();
		bean.setAccessId(id());
		bean.setPersistenceGmSessionFactory(gmSessions.systemSessionFactory());
		return bean;
	}

	// @Managed(Scope.prototype)
	public PersistenceGmSession lowLevelSession() {
		BasicPersistenceGmSession bean = new BasicPersistenceGmSession();
		bean.setIncrementalAccess(lowLevelAccess());
		return bean;
	}

	public abstract IncrementalAccess access();

	private IncrementalAccess lowLevelAccess() {
		IncrementalAccess access = access();

		if (access instanceof AopIncrementalAccess)
			return ((AopIncrementalAccess) access).getDelegate();

		return access;
	}

}
