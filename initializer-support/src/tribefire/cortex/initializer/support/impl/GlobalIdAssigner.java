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

import com.braintribe.cfg.Required;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.source.ResourceSource;
import com.braintribe.model.resource.specification.ResourceSpecification;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;

import tribefire.cortex.initializer.support.SetGlobalIds;
import tribefire.cortex.initializer.support.wire.contract.InitializerSupportContract;
import tribefire.module.model.resource.ModuleSource;

/**
 * <p>
 * This class sets the globalId for managed instances which have no globalId assigned explicitly.
 * 
 * <p>
 * It is checked if a initializerId is given. <br />
 * Otherwise, a {@link WireModule} needs to be in place as its name is taken to generate the globalId prefix.
 * 
 * globalIdPattern: wire://WireModuleSimpleName/wireSpace/managesInstance
 * 
 */
public class GlobalIdAssigner implements LifecycleListener {

	private InitializerSupportContract initializerSpace;
	private WireContext<?> wireContext;

	@Required
	public void setInitializerSpace(InitializerSupportContract initializerSpace) {
		this.initializerSpace = initializerSpace;
	}

	@Required
	public void setWireContext(WireContext<?> wireContext) {
		this.wireContext = wireContext;
	}

	@Override
	public void onPreDestroy(InstanceHolder beanHolder, Object bean) {
		// noop
	}

	@Override
	public void onPostConstruct(InstanceHolder beanHolder, Object bean) {
		if (!isEligibleForGidAssignment(beanHolder, bean))
			return;

		GenericEntity entity = (GenericEntity) bean;
		String globalId = entity.getGlobalId();
		if (globalId == null) {
			String prefix = initializerSpace.initializerId();

			if (prefix == null) {
				WireModule module = wireContext.findModuleFor(beanHolder.space().getClass());
				if (module != null)
					prefix = module.getClass().getSimpleName();
			}

			if (prefix == null)
				throw new IllegalStateException("You have to either specify a initializerId or use WireModules to automatically generate globalIds.");

			globalId = "wire://" + prefix + "/" + beanHolder.space().getClass().getSimpleName() + "/" + beanHolder.name();
			entity.setGlobalId(globalId);
		}

		if (entity instanceof Resource) {
			Resource r = (Resource) entity;
			ResourceSource source = r.getResourceSource();
			if (!(source instanceof ModuleSource))
				return;

			if (source.getGlobalId() == null)
				source.setGlobalId(globalId + "/source");
			
			ResourceSpecification spec = r.getSpecification();
			if (spec != null && spec.getGlobalId() == null)
				spec.setGlobalId(globalId + "/specification");
		}
	}

	private boolean isEligibleForGidAssignment(InstanceHolder beanHolder, Object bean) {
		return beanHolder.space() instanceof SetGlobalIds && bean instanceof GenericEntity;
	}
}
