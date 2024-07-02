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
package com.braintribe.model.processing.accessory.impl;

import static com.braintribe.model.processing.accessory.impl.ModelAccessoryHelper.modelByNameWithTc;

import java.util.function.Consumer;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolverBuilder;
import com.braintribe.model.processing.meta.cmd.CmdResolverImpl;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.impl.managed.BasicManagedGmSession;
import com.braintribe.utils.lcd.StopWatch;

/**
 * Standard model bound {@link AbstractModelAccessory}.
 * 
 */
public class BasicModelAccessory extends AbstractModelAccessory {

	// constants
	private static final Logger log = Logger.getLogger(BasicModelAccessory.class);

	// configurable
	private String modelName;
	private Consumer<CmdResolverBuilder> cmdInitializer;

	public BasicModelAccessory() {
	}

	@Required
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setCmdInitializer(Consumer<CmdResolverBuilder> cmdInitializer) {
		this.cmdInitializer = cmdInitializer;
	}
	
	@Override
	protected void build() {
		StopWatch sw = new StopWatch();

		BasicManagedGmSession session = new BasicManagedGmSession();

		GmMetaModel loadedModel = loadModel();

		GmMetaModel mergedMetaModel = session.merge().adoptUnexposed(adoptLoadedModel()).doFor(loadedModel);

		modelOracle = new BasicModelOracle(mergedMetaModel);

		cmdResolver = CmdResolverImpl.create(modelOracle) //
				.setSessionProvider(getSessionProvider()) //
				.initialize(cmdInitializer) //
				.done();

		modelSession = session;

		log.debug(() -> "Built " + this + " in " + sw.getElapsedTime() + "ms.");
	}

	protected boolean adoptLoadedModel() {
		return true;
	}

	protected GmMetaModel loadModel() {
		PersistenceGmSession cortexSession = cortexSessionProvider.get();

		return cortexSession.queryDetached().entities(modelByNameWithTc(modelName)).unique();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [modelName='" + modelName + "']";
	}

}
