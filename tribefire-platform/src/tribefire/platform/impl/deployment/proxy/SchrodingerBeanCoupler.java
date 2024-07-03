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
package tribefire.platform.impl.deployment.proxy;

import java.util.List;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.processing.deployment.api.ConfigurableDcProxyDelegation;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

/**
 * Initializer which sets a correct exernalId or default delegate on the configured {@link SchrodingerBean}s.
 * <p>
 * This must run as the very last initializer, once cortex is prepared. Then it can use SchrodingerBean's
 * {@link SchrodingerBean#deployable(CortexConfiguration, ManagedGmSession)} to access the deployable. If it finds one, it changes the extrnalId of
 * the bean's {@link SchrodingerBean#proxyDelegation() proxy delegation} to the deployable's id. Otherwise
 */
public class SchrodingerBeanCoupler extends SimplePersistenceInitializer {

	private static final Logger log = Logger.getLogger(SchrodingerBeanCoupler.class);

	private List<SchrodingerBean<?>> cortexBeans;

	@Required
	public void setBeans(List<SchrodingerBean<?>> cortexBeans) {
		this.cortexBeans = cortexBeans;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		ManagedGmSession cortexSession = context.getSession();
		CortexConfiguration cc = cortexSession.getEntityByGlobalId(CortexConfiguration.CORTEX_CONFIGURATION_GLOBAL_ID);

		for (SchrodingerBean<?> bean : cortexBeans) {
			Deployable cortexDeployable = bean.deployable(cc, cortexSession);
			if (cortexDeployable == null)
				throw new IllegalStateException("Cannot initialize SchrodingerBean: " + bean.name() + ", it has not provided a deployable.");

			String externalId = cortexDeployable.getExternalId();
			if (externalId == null)
				throw new IllegalStateException(
						"SchrodingerBean: " + bean.name() + " has a deployable with externalId null. Deployable:" + cortexDeployable);

			log.info("Coupling SchrodingerBean: " + bean.name() + " with externalId: '" + externalId + "'. Deployable: " + cortexDeployable);

			ConfigurableDcProxyDelegation proxyDelegation = bean.proxyDelegation();
			proxyDelegation.changeExternalId(externalId);
		}
	}
}
