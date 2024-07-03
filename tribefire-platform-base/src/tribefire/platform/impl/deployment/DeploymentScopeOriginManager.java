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
package tribefire.platform.impl.deployment;

import java.util.IdentityHashMap;
import java.util.Map;

import com.braintribe.cartridge.common.processing.deployment.DeploymentScope;
import com.braintribe.cartridge.common.processing.deployment.ReflectBeansForDeployment;
import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;

public class DeploymentScopeOriginManager implements LifecycleListener {
	private Map<Object, InstanceHolder> originMap = new IdentityHashMap<>();
	private DeploymentScope scope;
	
	@Configurable @Required
	public void setScope(DeploymentScope scope) {
		this.scope = scope;
	}

	@Override
	public void onPostConstruct(InstanceHolder beanHolder, Object bean) {
		if (beanHolder.scope() == scope || beanHolder.space() instanceof ReflectBeansForDeployment) {
			synchronized (originMap) {
				originMap.put(bean, beanHolder);
			}
		}
	}

	@Override
	public void onPreDestroy(InstanceHolder beanHolder, Object bean) {
		if (beanHolder.scope() == scope || beanHolder.space() instanceof ReflectBeansForDeployment) {
			synchronized (originMap) {
				originMap.remove(bean);
			}
		}
	}

	/**
	 * @return the BeanHolder that was responsible for creating the bean if existing otherwise null
	 */
	public InstanceHolder resolveBeanHolder(Object instance) {
		synchronized (originMap) {
			return originMap.get(instance);
		}
	}
}


