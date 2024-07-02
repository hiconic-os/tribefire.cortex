// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.cortex.module.loading;

import com.braintribe.model.generic.session.GmSession;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;
import com.braintribe.model.processing.meta.cmd.CmdResolverImpl;
import com.braintribe.model.processing.meta.editor.BasicMdEditorBuilder;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.MetaDataEditorBuilder;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.module.api.ConfigurationModelFactory;
import tribefire.module.wire.contract.ModelApiContract;

/**
 * @author peter.gazdik
 */
public class PlatformModelApiSpace implements ModelApiContract {

	private final PlatformHardwiredExpertsRegistry hardwiredExpertsSpace;

	public PlatformModelApiSpace(PlatformHardwiredExpertsRegistry hardwiredExpertsSpace) {
		this.hardwiredExpertsSpace = hardwiredExpertsSpace;
	}

	@Override
	public CmdResolverBuilder newCmdResolver(GmMetaModel model) {
		return newCmdResolver(newModelOracle(model));
	}

	@Override
	public CmdResolverBuilder newCmdResolver(ModelOracle modelOracle) {
		return initializeCmdResolver(CmdResolverImpl.create(modelOracle));
	}

	@Override
	public ModelOracle newModelOracle(GmMetaModel model) {
		return new BasicModelOracle(model);
	}

	@Override
	public MetaDataEditorBuilder newMetaDataEditor(GmMetaModel model) {
		BasicMdEditorBuilder result = BasicModelMetaDataEditor.create(model);

		GmSession session = model.session();
		if (session != null)
			result.withSession(session);

		return result;
	}

	/** Initializes given {@link CmdResolverBuilder} with all the (MD Selector) experts from modules. */
	public CmdResolverBuilder initializeCmdResolver(CmdResolverBuilder builder) {
		builder.addExperts(hardwiredExpertsSpace.mdSelectorExperts);
		return builder;
	}

	@Override
	public ConfigurationModelFactory newConfigurationModelFactory(ManagedGmSession session) {
		return new SessionBasedConfigurationModelFactory(session);
	}

}
