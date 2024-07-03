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
package tribefire.platform.wire.space.system;

import java.io.File;

import com.braintribe.utils.paths.PathCollectors;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.PreLoader;
import tribefire.platform.wire.space.MasterResourcesSpace;

/**
 * @see PreLoader
 * 
 * @author peter.gazdik
 */
@Managed
public class PreLoadingSpace implements WireSpace {

	@Import
	private MasterResourcesSpace resources;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		preLoader().startWiredPreLoading();
	}

	private PreLoader preLoader() {
		PreLoader bean = new PreLoader();
		bean.setCortexStorageBase(cortexStorageBase());

		return bean;
	}

	private File cortexStorageBase() {
		String configFilePath = PathCollectors.filePath.join("cortex", "data");
		return resources.database(configFilePath).asFile();
	}

}
