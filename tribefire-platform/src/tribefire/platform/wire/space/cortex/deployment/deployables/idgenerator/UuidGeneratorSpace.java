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
package tribefire.platform.wire.space.cortex.deployment.deployables.idgenerator;

import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.model.processing.idgenerator.basic.UuidGenerator;
import com.braintribe.model.processing.idgenerator.basic.UuidMode;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.wire.space.cortex.deployment.deployables.DeployableBaseSpace;

@Managed
public class UuidGeneratorSpace extends DeployableBaseSpace {

	@Managed
	public UuidGenerator uuidGenerator(ExpertContext<com.braintribe.model.idgendeployment.UuidGenerator> context) {
		com.braintribe.model.idgendeployment.UuidGenerator deployable = context.getDeployable();
		UuidGenerator bean = new UuidGenerator();

		if (deployable != null && deployable.getMode() != null) {
			switch (deployable.getMode()) {
				case compact:
					bean.setMode(UuidMode.compact);
					break;
				case compactWithTimestampPrefix:
					bean.setMode(UuidMode.compactWithTimestampPrefix);
					break;
				case standard:
					bean.setMode(UuidMode.standard);
					break;
				default:
					break;

			}
		}
		return bean;
	}
}
