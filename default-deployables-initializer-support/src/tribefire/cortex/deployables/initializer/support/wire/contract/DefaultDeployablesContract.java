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
package tribefire.cortex.deployables.initializer.support.wire.contract;

import java.util.List;

import com.braintribe.model.cortex.aspect.CryptoAspect;
import com.braintribe.model.cortex.aspect.FulltextAspect;
import com.braintribe.model.cortex.aspect.IdGeneratorAspect;
import com.braintribe.model.cortex.aspect.SecurityAspect;
import com.braintribe.model.cortex.aspect.StateProcessingAspect;
import com.braintribe.model.cortex.processorrules.BidiPropertyStateChangeProcessorRule;
import com.braintribe.model.cortex.processorrules.MetaDataStateChangeProcessorRule;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.extensiondeployment.StateChangeProcessorRule;
import com.braintribe.wire.api.space.WireSpace;

public interface DefaultDeployablesContract extends WireSpace {

	List<Deployable> defaultDeployables();

	List<AccessAspect> defaultAspects();

	List<StateChangeProcessorRule> defaultStateChangeProcessorRules();

	StateProcessingAspect stateProcessingAspect();

	FulltextAspect fulltextAspect();

	SecurityAspect securityAspect();

	IdGeneratorAspect idGeneratorAspect();

	CryptoAspect cryptoAspect();
	
	BidiPropertyStateChangeProcessorRule bidiPropertyProcessorRule();

	MetaDataStateChangeProcessorRule metaDataProcessorRule();

}
