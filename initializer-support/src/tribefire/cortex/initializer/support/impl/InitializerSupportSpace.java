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
package tribefire.cortex.initializer.support.impl;

import java.util.function.Supplier;

import com.braintribe.model.deployment.Module;
import com.braintribe.model.descriptive.HasExternalId;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.BaseType;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.StrategyOnCriterionMatch;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.initializer.support.wire.contract.InitializerSupportContract;
import tribefire.module.wire.contract.ModuleReflectionContract;

@Managed
public class InitializerSupportSpace implements InitializerSupportContract {

	private final PersistenceInitializationContext context;
	private final ManagedGmSession session;
	private final ModuleReflectionContract moduleReflection;
	private final Supplier<String> initializerIdSupplier;

	private Module currentModule;

	public InitializerSupportSpace(//
			PersistenceInitializationContext context, ModuleReflectionContract moduleReflection, Supplier<String> initializerIdSupplier) {

		this.context = context;
		this.session = context.getSession();
		this.moduleReflection = moduleReflection;
		this.initializerIdSupplier = initializerIdSupplier;
	}

	@Override
	public ManagedGmSession session() {
		return session;
	}

	@Override
	public <T extends GenericEntity> T create(EntityType<T> entityType) {
		return session.create(entityType);
	}

	@Override
	public <T extends GenericEntity> T lookup(String globalId) {
		return session.findEntityByGlobalId(globalId);
	}

	@Override
	public <T extends HasExternalId> T lookupExternalId(String externalId) {
		EntityQuery query = EntityQueryBuilder.from(HasExternalId.T).where().property(HasExternalId.externalId).eq(externalId).done();
		return session.query().entities(query).first();
	}

	@Override
	public Module currentModule() {
		if (currentModule == null)
			if ("cortex".equals(context.getAccessId()))
				currentModule = lookup(moduleReflection.globalId());
			else
				throw new UnsupportedOperationException("Can only access current module denotation instance when initializing [cortex], "
						+ "but this initializer is initializing [" + context.getAccessId() + "]. Initializer: [" + initializerId() + "]");

		return currentModule;
	}

	@Override
	public String initializerId() {
		return initializerIdSupplier.get();
	}

	@Override
	public <T> T importEntities(T entities) {
		ImportAssemblyCloningContext cloningContext = new ImportAssemblyCloningContext(session);
		return BaseType.INSTANCE.clone(cloningContext, entities, StrategyOnCriterionMatch.reference);
	}

}
