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

import static com.braintribe.utils.lcd.CollectionTools2.newLinkedSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;

/**
 * @author peter.gazdik
 */
public class ModelInitializingTools {

	public static void extendModelToCoverModels(ManagedGmSession session, GmMetaModel modelToExtend, Set<Model> extensionModels) {
		extendModelToCoverModels(modelToExtend, extensionModels, m -> (GmMetaModel) session.getEntityByGlobalId(m.globalId()));
	}

	public static void extendModelToCoverModels(GmMetaModel modelToExtend, Set<Model> extensionModels, Function<Model, GmMetaModel> modelLookup) {
		Set<Model> coveringModels = CommonModelsResolver.findCoveringModels(extensionModels);

		List<GmMetaModel> deps = modelToExtend.getDependencies();

		coveringModels.stream() //
				.map(modelLookup) //
				.filter(model -> !deps.contains(model)) //
				.forEach(deps::add);
	}

	public static void extendModelToCoverModels(GmMetaModel modelToExtend, Set<Model> extensionModels, Set<String> extensionModelNames,
			Function<Model, GmMetaModel> modelLookup, Function<String, GmMetaModel> modelByNameLookup) {

		Set<GmMetaModel> gmModels = newLinkedSet();
		for (Model model : extensionModels)
			gmModels.add(modelLookup.apply(model));
		for (String modelName : extensionModelNames)
			gmModels.add(modelByNameLookup.apply(modelName));

		Set<GmMetaModel> coveringModels = CommonGmModelsResolver.findCoveringModels(gmModels);

		List<GmMetaModel> deps = modelToExtend.getDependencies();

		coveringModels.stream() //
				.filter(model -> !deps.contains(model)) //
				.forEach(deps::add);

	}

}
