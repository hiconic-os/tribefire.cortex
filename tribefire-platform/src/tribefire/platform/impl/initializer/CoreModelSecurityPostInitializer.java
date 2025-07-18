// ============================================================================
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.braintribe.cfg.Configurable;
import com.braintribe.logging.Logger;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.data.prompt.Hidden;
import com.braintribe.model.meta.selector.ConjunctionSelector;
import com.braintribe.model.meta.selector.NegationSelector;
import com.braintribe.model.meta.selector.PropertyValueComparator;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.resource.Resource;

public class CoreModelSecurityPostInitializer extends SimplePersistenceInitializer {

	protected static Logger logger = Logger.getLogger(CoreModelSecurityPostInitializer.class);

	private Set<String> excludedAccesses = set("workbench");

	@Configurable
	public void setExcludedAccesses(Set<String> excludedAccesses) {
		this.excludedAccesses = excludedAccesses;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		logger.info("Start synchronization of system accesses.");

		//@formatter:off
		Set<String> systemAccessIds = new HashSet<String>(
				Arrays.asList(
					"auth", "auth.wb",
					"cortex", "cortex.wb",
					"setup", "setup.wb",
					"transient-messaging-data", "transient-messaging-data.wb",
					"user-sessions", "user-sessions.wb",
					"user-statistics", "user-statistics.wb",
					"workbench"
				));

		ManagedGmSession session = context.getSession();
		EntityQuery coreAccessQuery =
				EntityQueryBuilder
					.from(IncrementalAccess.T)
					.where().property(IncrementalAccess.externalId).in(systemAccessIds)
				.done();
		//@formatter:on

		Hidden hiddenMd = CoreModelSecurityInitializer.getHiddenForNonAdminMd(session);

		List<IncrementalAccess> coreAccesses = session.query().entities(coreAccessQuery).list();
		for (IncrementalAccess a : coreAccesses) {
			if (!excludedAccesses.contains(a.getExternalId())) {
				ModelMetaDataEditor modelEditor = BasicModelMetaDataEditor.create(a.getMetaModel()).withSession(session).done();
				modelEditor.addModelMetaData(hiddenMd);

				if ("cortex".equals(a.getExternalId())) {
					// For the CortexAccess all resources except image resources are Hidden for non-admin users.
					PropertyValueComparator mimeTypeComparator = CoreModelSecurityInitializer.getImageResourceSelector(session);
					NegationSelector negationResourceSelector = session.create(NegationSelector.T, "selector:negation/imageResources");
					negationResourceSelector.setOperand(mimeTypeComparator);

					ConjunctionSelector cortexResourceSelector = session.create(ConjunctionSelector.T, "selector:cortexResources");
					cortexResourceSelector.getOperands().add(negationResourceSelector);
					cortexResourceSelector.getOperands().add(CoreModelSecurityInitializer.getNonAdminSelector(session));

					Hidden hiddenCortexResourcesMd = session.create(Hidden.T, "metadata:hidden/allNonImageResourcesFornNonAdmins");
					hiddenCortexResourcesMd.setSelector(cortexResourceSelector);
					modelEditor.onEntityType(Resource.T).addMetaData(hiddenCortexResourcesMd);

				} else {
					ModelOracle oracle = new BasicModelOracle(a.getMetaModel());
					GmEntityType resourceType = oracle.findGmType(Resource.T);
					if (resourceType != null)
						modelEditor.onEntityType(Resource.T).addMetaData(hiddenMd);
				}
			}
		}
	}

}
