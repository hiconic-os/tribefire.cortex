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
package tribefire.platform.impl.initializer;

import static com.braintribe.wire.api.util.Sets.set;

import java.util.Set;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;
import com.braintribe.model.meta.data.prompt.Hidden;
import com.braintribe.model.meta.selector.NegationSelector;
import com.braintribe.model.meta.selector.Operator;
import com.braintribe.model.meta.selector.PropertyValueComparator;
import com.braintribe.model.meta.selector.RoleSelector;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.resource.Resource;

public class CoreModelSecurityInitializer extends SimplePersistenceInitializer {

	private static final String METADATA_HIDDEN_CORE_MODELS_FOR_NON_ADMINS = "metadata:hidden/coreModelsForNonAdmins";
	private static final String SELECTOR_IMAGE_RESOURCES = "selector:imageResources";
	private static final String SELECTOR_NEGATION_ADMIN_ROLES = "selector:negation/adminRoles";

	protected static Logger logger = Logger.getLogger(CoreModelSecurityInitializer.class);

	private Set<String> adminRoles = set("tf-admin", "tf-locksmith", "tf-internal");

	@Configurable
	public void setAdminRoles(Set<String> adminRoles) {
		this.adminRoles = adminRoles;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		logger.info("Preparing core security instances.");

		ManagedGmSession session = context.getSession();

		RoleSelector adminRoleSelector = createAdminRolesSelector(session);

		NegationSelector nonAdminSelector = session.create(NegationSelector.T, SELECTOR_NEGATION_ADMIN_ROLES);
		nonAdminSelector.setOperand(adminRoleSelector);

		Hidden hiddenMd = session.create(Hidden.T, METADATA_HIDDEN_CORE_MODELS_FOR_NON_ADMINS);
		hiddenMd.setSelector(nonAdminSelector);

		createImageResourceSelector(session);
	}

	private PropertyValueComparator createImageResourceSelector(ManagedGmSession session) {
		PropertyValueComparator mimeTypeComparator = session.create(PropertyValueComparator.T, SELECTOR_IMAGE_RESOURCES);
		mimeTypeComparator.setPropertyPath(Resource.mimeType);
		mimeTypeComparator.setOperator(Operator.like);
		mimeTypeComparator.setValue("image/*");
		return mimeTypeComparator;
	}

	private RoleSelector createAdminRolesSelector(ManagedGmSession session) {
		RoleSelector selector = session.create(RoleSelector.T, "selector:role/adminRoles");
		selector.setRoles(adminRoles);
		return selector;
	}

	public static PropertyValueComparator getImageResourceSelector(ManagedGmSession session) {
		return session.getEntityByGlobalId(SELECTOR_IMAGE_RESOURCES);
	}

	public static Hidden getHiddenForNonAdminMd(ManagedGmSession session) {
		return session.getEntityByGlobalId(METADATA_HIDDEN_CORE_MODELS_FOR_NON_ADMINS);
	}

	public static NegationSelector getNonAdminSelector(ManagedGmSession session) {
		return session.getEntityByGlobalId(SELECTOR_NEGATION_ADMIN_ROLES);
	}

}
