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
package tribefire.cortex.manipulation.conversion.beans;

import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Root beans are {@link NewBean}s which have dependencies on all the {@link NewBean}s in out pool. Only these need to be stated in the "initialize"
 * method.
 */
public class BeansFinder_Managed {

	public static void markManagedBeans(Set<NewBean> rootBeans, BeanRegistry beanRegistry) {
		new BeansFinder_Managed(rootBeans, beanRegistry).findEm();
	}

	private final Set<NewBean> rootBeans;
	private final BeanRegistry beanRegistry;

	private final Map<NewBean, Integer> beanToDependersCount = newMap();

	private BeansFinder_Managed(Set<NewBean> rootBeans, BeanRegistry beanRegistry) {
		this.rootBeans = rootBeans;
		this.beanRegistry = beanRegistry;
	}

	private void findEm() {
		indexDeps(rootBeans);
		index(beanRegistry.existingBeans);
		index(beanRegistry.newBeans);

		beanToDependersCount.entrySet().stream() //
				.filter(e -> e.getValue() > 1) //
				.map(e -> e.getKey()) //
				.forEach(nb -> nb.isManaged = true);
	}

	private void index(Collection<? extends EntityBean<?>> beans) {
		for (EntityBean<?> bean : beans)
			indexDeps(bean.deps);
	}

	private void indexDeps(Collection<? extends EntityBean<?>> deps) {
		for (EntityBean<?> dep : deps)
			countDepOn(dep);
	}

	private void countDepOn(EntityBean<?> dep) {
		if (dep instanceof ExistingBean)
			return;

		Integer i = beanToDependersCount.get(dep);
		i = i == null ? 1 : i + 1;
		beanToDependersCount.put((NewBean) dep, i);
	}

}
