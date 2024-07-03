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

import static com.braintribe.utils.SysPrint.spOut;
import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.braintribe.cc.lcd.CodingMap;
import com.braintribe.model.generic.commons.PartitionIgnoringEntRefHashingComparator;
import com.braintribe.model.generic.value.EntityReference;
import com.braintribe.model.generic.value.EntityReferenceType;
import com.braintribe.model.generic.value.GlobalEntityReference;
import com.braintribe.model.generic.value.PreliminaryEntityReference;
import com.braintribe.utils.StringTools;

import tribefire.cortex.manipulation.conversion.code.InitializerWritingContext;

/**
 * @author peter.gazdik
 */
public class BeanRegistry {

	public final InitializerWritingContext ctx;

	public final List<NewBean> newBeans = newList();
	public final List<ExistingBean> existingBeans = newList();
	public final Map<EntityReference, EntityBean<?>> refToBean = CodingMap.create(PartitionIgnoringEntRefHashingComparator.INSTANCE);

	private final Map<String, Integer> simpleNameToCount = newMap();

	public BeanRegistry(InitializerWritingContext context) {
		this.ctx = context;
	}

	public String resolveBeanName(EntityReference ref) {
		String typeSignature = ref.getTypeSignature();
		String simpleName = StringTools.findSuffix(typeSignature, ".");

		Integer count = simpleNameToCount.get(simpleName);
		Integer newCount = count == null ? 1 : count + 1;

		simpleNameToCount.put(simpleName, newCount);
		return StringTools.uncapitalize(simpleName) + "_" + newCount;
	}

	public EntityBean<?> acquireBean(EntityReference ref) {
		EntityBean<?> result = refToBean.get(ref);
		if (result != null)
			return result;
		else
			return createBeanIfGlobal(ref);
	}

	private EntityBean<?> createBeanIfGlobal(EntityReference ref) {
		if (ref.referenceType() != EntityReferenceType.global)
			throw new IllegalArgumentException("Unexpected reference, global was expected, not: " + ref);

		spOut("Existing Bean: " + ref);
		return onExistingBean(new ExistingBean(this, (GlobalEntityReference) ref));
	}

	public void createNewBeanFor(PreliminaryEntityReference ref) {
		onNewBean(new NewBean(this, ref));
	}

	private NewBean onNewBean(NewBean bean) {
		newBeans.add(bean);
		return onBean(bean);
	}

	private ExistingBean onExistingBean(ExistingBean bean) {
		existingBeans.add(bean);
		return onBean(bean);
	}

	private <B extends EntityBean<?>> B onBean(B bean) {
		refToBean.put(bean.ref, bean);
		return bean;
	}

	public void onGidAssigned(EntityReference ref, String newGid) {
		EntityBean<?> bean = requireBean(ref, () -> "Trying to asssing globalId to " + newGid);
		bean.globalId = newGid;

		GlobalEntityReference newRef = GlobalEntityReference.T.create();
		newRef.setTypeSignature(ref.getTypeSignature());
		newRef.setRefId(newGid);

		if (refToBean.put(newRef, bean) != null)
			throw new IllegalArgumentException(
					"An attempt is made to assign globalId, but there already is a different instance with said id: " + newGid);
	}

	public EntityBean<?> requireBean(EntityReference ref, Supplier<String> contextSupplier) {
		EntityBean<?> bean = refToBean.get(ref);
		if (bean == null)
			throw new IllegalArgumentException("Entity not found for reference '" + ref + "'. " + contextSupplier.get());
		return bean;
	}

}
