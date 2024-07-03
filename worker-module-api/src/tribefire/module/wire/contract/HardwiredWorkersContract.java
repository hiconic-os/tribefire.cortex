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
package tribefire.module.wire.contract;

import java.util.function.Supplier;

import com.braintribe.model.extensiondeployment.HardwiredWorker;
import com.braintribe.model.processing.worker.api.Worker;

/**
 * Offers methods for binding {@link Worker}s.
 * 
 * @see HardwiredDeployablesContract
 */
public interface HardwiredWorkersContract extends HardwiredDeployablesContract {

	default HardwiredWorker bindWorker(String externalId, String name, Worker worker) {
		return bindWorker(externalId, name, () -> worker);
	}

	/** ExternalId convention: hardwired:worker/${description} (e.g. hardwired:worker/update-checker) */
	HardwiredWorker bindWorker(String externalId, String name, Supplier<Worker> workerSuplier);

}
