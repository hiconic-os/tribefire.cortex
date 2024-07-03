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
package tribefire.cortex.dcsa.analysis.wire.space;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.dcsa.analysis.impl.CortexDcsaSsFiller;
import tribefire.cortex.model.api.dcsa.FillCortexDcsaSharedStorage;
import tribefire.module.wire.contract.HardwiredDeployablesContract;
import tribefire.module.wire.contract.ResourceProcessingContract;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class DcsaAnalysisModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;
	
	@Import
	private HardwiredDeployablesContract hardwiredDeployables;

	@Import
	private ResourceProcessingContract resourceProcessing;
	
	@Override
	public void bindHardwired() {
		hardwiredDeployables.bindOnExistingServiceDomain("cortex") //
				.serviceProcessor("cortexDcsaFiller", "Cortex DCSA SS Filler", FillCortexDcsaSharedStorage.T,
						(ctx, request) -> dcsaFiller().doIt(request));
	}

	private CortexDcsaSsFiller dcsaFiller() {
		CortexDcsaSsFiller bean = new CortexDcsaSsFiller();
		bean.setSharedStorageSupplier(tfPlatform.hardwiredDeployables().sharedStorageSupplier());
		bean.setMarshaller(new JsonStreamMarshaller());
		bean.setResourceBuilder(resourceProcessing.transientResourceBuilder());

		return bean;
	}
}
