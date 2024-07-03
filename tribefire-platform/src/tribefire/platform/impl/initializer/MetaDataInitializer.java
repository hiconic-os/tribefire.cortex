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
package tribefire.platform.impl.initializer;

import java.util.function.BiConsumer;

import com.braintribe.cfg.Required;
import com.braintribe.common.artifact.ArtifactReflection;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.query.tools.PreparedQueries;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.collaboration.SimplePersistenceInitializer;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

/**
 * @author peter.gazdik
 */
public class MetaDataInitializer extends SimplePersistenceInitializer {

	private String modelName;
	private BiConsumer<ModelMetaDataEditor, ManagedGmSession> metaDataConfigurer;

	private final String stageName;

	public MetaDataInitializer() {
		this(null);
	}

	public MetaDataInitializer(String stageName) {
		this.stageName = stageName;
	}

	public void setModelAr(ArtifactReflection modelAr) {
		setModelName(modelAr.name());
	}

	@Required
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Override
	protected String stageName() {
		return stageName != null ? stageName : "MetaData-of-" + modelName;
	}
	
	public void setMetaDataConfigurer(BiConsumer<ModelMetaDataEditor, ManagedGmSession> metaDataConfigurer) {
		this.metaDataConfigurer = metaDataConfigurer;
	}

	@Override
	public void initializeData(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		GmMetaModel gmModel = queryModel(context);

		ManagedGmSession session = context.getSession();
		ModelMetaDataEditor editor = BasicModelMetaDataEditor.create(gmModel).withSession(session).done();
		metaDataConfigurer.accept(editor, session);
	}

	private GmMetaModel queryModel(PersistenceInitializationContext context) throws ManipulationPersistenceException {
		GmMetaModel result = context.getSession().query().select(PreparedQueries.modelByName(modelName)).unique();
		if (result == null)
			throw new ManipulationPersistenceException("Model not found in the context session: " + modelName);

		return result;
	}

}
