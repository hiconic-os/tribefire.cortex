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
package tribefire.platform.wire.space.cortex;

import static com.braintribe.wire.api.util.Sets.set;

import com.braintribe.cartridge.common.processing.deployment.ReflectBeansForDeployment;
import com.braintribe.model.access.security.SecurityAspect;
import com.braintribe.model.access.security.manipulation.experts.EntityDeletionExpert;
import com.braintribe.model.access.security.manipulation.experts.EntityInstantiationDisabledExpert;
import com.braintribe.model.access.security.manipulation.experts.MandatoryPropertyExpert;
import com.braintribe.model.access.security.manipulation.experts.PropertyModifiableExpert;
import com.braintribe.model.access.security.manipulation.experts.UniqueKeyPropertyExpert;
import com.braintribe.model.processing.aop.fulltext.ReplaceFulltextComparisons;
import com.braintribe.model.processing.aspect.crypto.CryptoAspect;
import com.braintribe.model.processing.deployment.utils.AccessLookupModelProvider;
import com.braintribe.model.processing.idgenerator.GlobalIdGeneratorAspect;
import com.braintribe.model.processing.idgenerator.IdGeneratorAspect;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.common.CryptoSpace;
import tribefire.platform.wire.space.cortex.accesses.PlatformSetupAccessSpace;
import tribefire.platform.wire.space.cortex.deployment.DeploymentSpace;
import tribefire.platform.wire.space.cortex.services.AccessServiceSpace;
import tribefire.platform.wire.space.security.AuthContextSpace;

@Managed
public class AccessAspectsSpace implements WireSpace, ReflectBeansForDeployment {

	@Import
	private AccessServiceSpace accessService;

	@Import
	private AuthContextSpace authContext;

	@Import
	private CryptoSpace crypto;

	@Import
	private DeploymentSpace deployment;
	
	@Import
	private GmSessionsSpace gmSessions;

	@Import
	private PlatformSetupAccessSpace platformSetupAccess;
	
	@Managed
	public SecurityAspect security() {
		SecurityAspect bean = new SecurityAspect();
		bean.setTrustedRoles(set("tf-internal"));
		// @formatter:off
		bean.setManipulationSecurityExperts(
				set(
					new UniqueKeyPropertyExpert(),
					new MandatoryPropertyExpert(),
					new EntityInstantiationDisabledExpert(),
					new EntityDeletionExpert(),
					new PropertyModifiableExpert()
				)
			);
		// @formatter:on
		return bean;
	}

	@Managed
	public ReplaceFulltextComparisons fulltext() {
		ReplaceFulltextComparisons bean = new ReplaceFulltextComparisons();
		return bean;
	}

	@Managed
	public AccessLookupModelProvider lookupModelProvider() {
		AccessLookupModelProvider bean = new AccessLookupModelProvider();
		bean.setAccessService(accessService.service());
		bean.setAccessIdentificationLookup(accessService.service());
		return bean;
	}

	@Managed
	public GlobalIdGeneratorAspect globalIdGenerator() {
		GlobalIdGeneratorAspect bean = new GlobalIdGeneratorAspect();
		return bean;
	}

	@Managed
	public IdGeneratorAspect idGenerator() {
		IdGeneratorAspect bean = new IdGeneratorAspect();
		bean.setDeployRegistry(deployment.registry());
		return bean;
	}

	@Managed
	public CryptoAspect crypto() {
		CryptoAspect bean = new CryptoAspect();
		bean.setCryptorProvider(crypto.cryptorProvider());
		bean.setCacheCryptorsPerContext(true);
		return bean;
	}

}
