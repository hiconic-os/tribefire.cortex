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
package tribefire.cortex.initializer.support.integrity.wire.contract;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.cortex.deployment.EnvironmentDenotationRegistry;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.impl.lookup.GlobalId;
import tribefire.cortex.initializer.support.impl.lookup.InstanceLookup;

/**
 * {@link InstanceLookup lookup} of some of the fundamental entities in cortex.
 */
@InstanceLookup(lookupOnly = true)
public interface CoreInstancesContract extends WireSpace {

	String cortexModelGlobalId = "model:tribefire.cortex:tribefire-cortex-model";
	String cortexServiceModelGlobalId = "model:tribefire.cortex:tribefire-cortex-service-model";
	String workbenchModelGlobalId = "model:tribefire.cortex:workbench-model";
	String essentialMetaDataModelGlobalId = "model:com.braintribe.gm:essential-meta-data-model";
	String basicMetaModelGlobalId = "model:com.braintribe.gm:basic-meta-model";
	String deploymentModelGlobalId = "model:com.braintribe.gm:deployment-model";
	String basicValueDescriptorModelGlobalId = "model:com.braintribe.gm:basic-value-descriptor-model";
	String workbenchAccessGlobalId = "hardwired:access/workbench";

	@GlobalId(cortexModelGlobalId)
	GmMetaModel cortexModel();

	@GlobalId(cortexServiceModelGlobalId)
	GmMetaModel cortexServiceModel();

	@GlobalId(workbenchModelGlobalId)
	GmMetaModel workbenchModel();

	@GlobalId(essentialMetaDataModelGlobalId)
	GmMetaModel essentialMetaDataModel();

	@GlobalId(basicMetaModelGlobalId)
	GmMetaModel basicMetaModel();

	@GlobalId(deploymentModelGlobalId)
	GmMetaModel deploymentModel();

	@GlobalId(basicValueDescriptorModelGlobalId)
	GmMetaModel basicValueDescriptorModel();

	@GlobalId(workbenchAccessGlobalId)
	IncrementalAccess workbenchAccess();

	@GlobalId(CortexConfiguration.CORTEX_CONFIGURATION_GLOBAL_ID)
	CortexConfiguration cortexConfiguration();

	@GlobalId(EnvironmentDenotationRegistry.ENVIRONMENT_DENOTATION_REGISTRY__GLOBAL_ID)
	EnvironmentDenotationRegistry environmentDenotationRegistry();

}
