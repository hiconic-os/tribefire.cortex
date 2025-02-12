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
package tribefire.platform.wire.space.cortex.deployment.deployables.access;

import java.io.File;
import java.util.Optional;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.DeploymentContext;
import com.braintribe.model.processing.deployment.api.ResolvedComponent;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.wire.space.cortex.deployment.deployables.DeployableBaseSpace;

@Managed
public class SimulatedAccessSpace extends DeployableBaseSpace {
	
	@Import
	private CollaborativeSmoodAccessSpace collarborativeSmood;

	public IncrementalAccess access(
			DeploymentContext<? extends com.braintribe.model.accessdeployment.IncrementalAccess, ? extends IncrementalAccess> context) {
		return rawSmoodAccess(context);
	}
	
	@Managed
	private com.braintribe.model.access.collaboration.CollaborativeSmoodAccess rawSmoodAccess(
			DeploymentContext<? extends com.braintribe.model.accessdeployment.IncrementalAccess, ? extends IncrementalAccess> context) {

		com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess csaDeployable = com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess.T.create();
		
		com.braintribe.model.accessdeployment.IncrementalAccess deployable = context.getDeployable();
		csaDeployable.setExternalId(deployable.getExternalId());
		csaDeployable.setName(deployable.getName());
		csaDeployable.setMetaModel(deployable.getMetaModel());
		csaDeployable.setServiceModel(deployable.getServiceModel());
		csaDeployable.setStorageDirectory(new File(resources.database(".").asPath().toString(),deployable.getExternalId()+"/data").getPath());
		
		
		DeploymentContext<com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess, IncrementalAccess> csaContext = new DeploymentContext<com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess, IncrementalAccess>() {

			@Override
			public IncrementalAccess getInstanceToBeBound(){
				return context.getInstanceToBeBound();
			}

			@Override
			public PersistenceGmSession getSession() {
				return context.getSession();
			}

			@Override
			public com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess getDeployable(){return csaDeployable;}

			@Override
			public <E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType) {
				return context.resolve(deployable, componentType);
			}

			@Override
			public <E> E resolve(String externalId, EntityType<? extends Deployable> componentType) {
				return context.resolve(externalId, componentType);
			}

			@Override
			public <E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType,
					Class<E> expertInterface, E defaultDelegate) {
				return context.resolve(deployable, componentType, expertInterface, defaultDelegate);
			}

			@Override
			public <E> E resolve(String externalId, EntityType<? extends Deployable> componentType,
					Class<E> expertInterface, E defaultDelegate) {
				return context.resolve(externalId, componentType, expertInterface, defaultDelegate);
			}

			@Override
			public <E> Optional<ResolvedComponent<E>> resolveOptional(String externalId,
					EntityType<? extends Deployable> componentType, Class<E> expertInterface) {
				return context.resolveOptional(externalId, componentType, expertInterface);
			}
			
		};
		
		return collarborativeSmood.rawSmoodAccess(csaContext);
	}

}
