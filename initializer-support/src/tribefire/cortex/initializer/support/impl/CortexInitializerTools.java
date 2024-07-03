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
package tribefire.cortex.initializer.support.impl;

import java.util.List;
import java.util.stream.Stream;

import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

import tribefire.cortex.initializer.support.integrity.wire.contract.CoreInstancesContract;

/**
 * @author peter.gazdik
 */
public class CortexInitializerTools {

	/** Similar to {@link #addToCortexModel(ManagedGmSession, String...)}, but first extracts globalIds from given {@link Model}s. */
	public static void addToCortexModel(ManagedGmSession session, Model... models) {
		String[] gids = Stream.of(models) //
				.map(Model::globalId) //
				.toArray(n -> new String[n]);

		addToCortexModel(session, gids);
	}

	/**
	 * Adds models with given globalIds to the cortex model (so that types from given models can be instantiated in cortex). This useful for example
	 * for deployment or meta-data models.
	 */
	public static void addToCortexModel(ManagedGmSession session, String... modelGlobalIds) {
		GmMetaModel cortexModel = session.getEntityByGlobalId(CoreInstancesContract.cortexModelGlobalId);
		List<GmMetaModel> cortexDeps = cortexModel.getDependencies();

		for (String mgid : modelGlobalIds) {
			GmMetaModel customModel = session.getEntityByGlobalId(mgid);
			cortexDeps.add(customModel);
		}
	}

}
