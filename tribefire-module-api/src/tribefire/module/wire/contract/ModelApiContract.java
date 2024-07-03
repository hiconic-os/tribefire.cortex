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

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;
import com.braintribe.model.processing.meta.cmd.context.experts.SelectorExpert;
import com.braintribe.model.processing.meta.editor.MetaDataEditorBuilder;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.api.ConfigurationModelFactory;

/**
 * @author peter.gazdik
 */
public interface ModelApiContract extends WireSpace {

	/**
	 * Returns a {@link CmdResolverBuilder} for given model which is pre-configured by the platform.
	 * <p>
	 * For example custom {@link SelectorExpert}s bound via {@link HardwiredExpertsContract#bindMetaDataSelectorExpert(EntityType, SelectorExpert)}
	 * are passed to the returned builder.
	 */
	CmdResolverBuilder newCmdResolver(GmMetaModel model);

	CmdResolverBuilder newCmdResolver(ModelOracle modelOracle);

	/** Returns a {@link ModelOracle} for given {@link GmMetaModel model}. */
	ModelOracle newModelOracle(GmMetaModel model);

	/**
	 * Returns a {@link MetaDataEditorBuilder}. If given model is attached to a session, the builder's {@link MetaDataEditorBuilder#withSession} is
	 * called with that session (thus all the created overrides are created with the same session).
	 */
	MetaDataEditorBuilder newMetaDataEditor(GmMetaModel model);

	/**
	 * Returns a {@link ConfigurationModelFactory} which can be used to create new (configuration) models and configure it's dependencies using given
	 * {@link ManagedGmSession session}.
	 */
	ConfigurationModelFactory newConfigurationModelFactory(ManagedGmSession session);

}
