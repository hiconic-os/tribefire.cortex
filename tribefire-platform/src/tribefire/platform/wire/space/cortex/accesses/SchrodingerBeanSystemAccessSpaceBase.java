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
package tribefire.platform.wire.space.cortex.accesses;

import java.io.File;
import java.util.function.Supplier;

import com.braintribe.model.access.smood.bms.BinaryManipulationStorage;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.utils.QueryingModelProvider;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public abstract class SchrodingerBeanSystemAccessSpaceBase extends SystemAccessSpaceBase {

	@Import
	protected CortexAccessSpace cortexAccess;

	@Managed
	public BinaryManipulationStorage manipulationStorage() {
		BinaryManipulationStorage bean = new BinaryManipulationStorage();
		bean.setStorageFile(manipulationStorageFile());
		return bean;
	}

	@Managed
	public File dataStorageFile() {
		File file = resources.database(id() + "/data/current.xml").asFile();
		return file;
	}

	@Managed
	private File manipulationStorageFile() {
		File file = resources.database(id() + "/data/current.buffer").asFile();
		return file;
	}

	@Managed
	public Supplier<GmMetaModel> metaModelProvider() {
		QueryingModelProvider bean = new QueryingModelProvider();
		bean.setSessionProvider(cortexAccess::lowLevelSession);
		bean.setModelName(modelName());
		return bean;
	}

}
