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
package tribefire.cortex.initializer.tools;

import static com.braintribe.utils.lcd.CollectionTools2.newSet;

import java.util.Set;

import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public class CommonGmModelsResolver {

	public static Set<GmMetaModel> findCoveringModels(Iterable<GmMetaModel> models) {
		CommonGmModelsResolver instance = new CommonGmModelsResolver(models);
		return instance.findCoveringModelsImpl();
	}

	private final Iterable<GmMetaModel> models;

	private final Set<GmMetaModel> result = newSet();
	private final Set<GmMetaModel> coverage = newSet();

	private CommonGmModelsResolver(Iterable<GmMetaModel> models) {
		this.models = models;
	}

	private Set<GmMetaModel> findCoveringModelsImpl() {
		for (GmMetaModel model : models)
			ensureCovered(model);

		return result;
	}

	private void ensureCovered(GmMetaModel model) {
		if (coverage.contains(model))
			return;

		Set<GmMetaModel> modelCoverage = computeCoverageOf(model);
		result.removeAll(modelCoverage);
		result.add(model);

		coverage.addAll(modelCoverage);
	}

	private static Set<GmMetaModel> computeCoverageOf(GmMetaModel model) {
		Set<GmMetaModel> result = newSet();
		computeCoverage(model, result);

		return result;
	}

	private static void computeCoverage(GmMetaModel model, Set<GmMetaModel> result) {
		if (result.add(model))
			for (GmMetaModel dependency : model.getDependencies())
				computeCoverage(dependency, result);
	}

}
