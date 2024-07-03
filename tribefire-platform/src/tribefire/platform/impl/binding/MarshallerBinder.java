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
package tribefire.platform.impl.binding;

import com.braintribe.cfg.LifecycleAware;
import com.braintribe.cfg.Required;
import com.braintribe.codec.marshaller.api.ConfigurableMarshallerRegistry;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.marshallerdeployment.HardwiredMarshaller;
import com.braintribe.model.marshallerdeployment.Marshaller;
import com.braintribe.model.processing.deployment.api.DeployRegistry;
import com.braintribe.model.processing.deployment.api.DeployRegistryListener;
import com.braintribe.model.processing.deployment.api.DeployedUnit;
import com.braintribe.model.processing.deployment.api.PlainComponentBinder;

/**
 * @author peter.gazdik
 */
public class MarshallerBinder extends PlainComponentBinder<Marshaller, com.braintribe.codec.marshaller.api.Marshaller> implements LifecycleAware {

	private ConfigurableMarshallerRegistry marshallerRegistry;
	private DeployRegistry deployRegistry;
	private MarshallerDeploymentListener deploymentListener;

	@Required
	public void setMarshallerRegistry(ConfigurableMarshallerRegistry marshallerRegistry) {
		this.marshallerRegistry = marshallerRegistry;
	}

	@Required
	public void setDeployRegistry(DeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;
	}

	public MarshallerBinder() {
		super(Marshaller.T, com.braintribe.codec.marshaller.api.Marshaller.class);
	}

	@Override
	public void postConstruct() {
		deploymentListener = new MarshallerDeploymentListener();
		deployRegistry.addListener(deploymentListener);
	}

	@Override
	public void preDestroy() {
		deployRegistry.removeListener(deploymentListener);
	}

	private class MarshallerDeploymentListener implements DeployRegistryListener {

		@Override
		public void onDeploy(Deployable deployable, DeployedUnit deployedUnit) {
			if (!(deployable instanceof Marshaller))
				return;

			// Maybe temporary solution for the fact that we need to eagerly register core platform marshallers
			if (deployable instanceof HardwiredMarshaller && deployable.getModule() == null)
				return;
			
			Marshaller denotation = (Marshaller) deployable;
			com.braintribe.codec.marshaller.api.Marshaller expert = deployedUnit.getComponent(Marshaller.T);

			for (String mimeType : denotation.getMimeTypes())
				marshallerRegistry.registerMarshaller(mimeType, expert);
		}

		@Override
		public void onUndeploy(Deployable deployable, DeployedUnit deployedUnit) {
			if (!(deployable instanceof Marshaller))
				return;

			Marshaller denotation = (Marshaller) deployable;
			com.braintribe.codec.marshaller.api.Marshaller expert = deployedUnit.getComponent(Marshaller.T);

			for (String mimeType : denotation.getMimeTypes())
				marshallerRegistry.unregisterMarshaller(mimeType, expert);
		}

	}

}
