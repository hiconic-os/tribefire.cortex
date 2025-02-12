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
package tribefire.cortex.module.loading;

import com.braintribe.common.artifact.ArtifactReflection;
import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.configured.ConfigurationModelBuilder;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.module.api.ConfigurationModelFactory;


/**
 * @author peter.gazdik
 */
/* package */ class SessionBasedConfigurationModelFactory implements ConfigurationModelFactory {

	private final ManagedGmSession session;

	public SessionBasedConfigurationModelFactory(ManagedGmSession session) {
		this.session = session;
	}

	@Override
	public ConfigurationModelBuilder create(String groupId, String artifactId, String version) {
		return create(groupId + ":" + artifactId, version);
	}

	@Override
	public ConfigurationModelBuilder create(String name, String version) {
		return new ConfigurationModelBuilderManagedImpl(session, name, version);
	}

}

class ConfigurationModelBuilderManagedImpl implements ConfigurationModelBuilder {

	private final ManagedGmSession session;
	private final GmMetaModel model;

	/**
	 * @param session
	 *            The {@link ManagedGmSession} for queries and the new {@link GmMetaModel}.
	 * @param modelName
	 *            The name of the new model, its globalId will be `model:modelName`.
	 */
	public ConfigurationModelBuilderManagedImpl(ManagedGmSession session, String modelName, String version) {
		this.session = session;
		this.model = session.create(GmMetaModel.T, Model.modelGlobalId(modelName));
		this.model.setName(modelName);
		this.model.setVersion(version);
	}

	/**
	 * @param session
	 *            The {@link ManagedGmSession} for queries.
	 * @param model
	 *            An existing {@link GmMetaModel}, no new model will be created.
	 */
	public ConfigurationModelBuilderManagedImpl(ManagedGmSession session, GmMetaModel model) {
		this.session = session;
		this.model = model;
	}

	@Override
	public ConfigurationModelBuilder addDependency(ArtifactReflection standardArtifactReflection) {

		if (!standardArtifactReflection.archetypes().contains("model"))
			throw new IllegalArgumentException("Artifact " + standardArtifactReflection + " is not a model");

		return addDependencyByName(standardArtifactReflection.name());
	}

	@Override
	public ConfigurationModelBuilder addDependencyByName(String modelName) {
		return addDependencyByGlobalId(Model.modelGlobalId(modelName));
	}

	@Override
	public ConfigurationModelBuilder addDependency(Model model) {
		return addDependencyByGlobalId(model.globalId());
	}

	@Override
	public ConfigurationModelBuilder addDependencyByGlobalId(String globalId) {
		return addDependency((GmMetaModel) session.getEntityByGlobalId(globalId));
	}

	@Override
	public ConfigurationModelBuilder addDependency(GmMetaModel model) {
		this.model.getDependencies().add(model);
		return this;
	}

	@Override
	public GmMetaModel get() {
		return this.model;
	}

}
