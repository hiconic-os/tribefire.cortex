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
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Model;

/**
 * @author peter.gazdik
 */
public class CommonModelsResolver {

	public static Set<Model> getModelsOf(Iterable<EntityType<?>> entityTypes) {
		requireNonNull(entityTypes, "Cannot get models of null (should be EntityTypes).");
		
		Set<Model> result = newSet();
		for (EntityType<?> et: entityTypes)
			result.add(getModelOf(et));
			
		return result;
		
	}

	public static Model getModelOf(EntityType<?> entityType) {
		requireNonNull(entityType, "Cannot get model of null (should be EntityType).");

		Model result = entityType.getModel();
		if (result == null)
			throw new IllegalArgumentException("Model not found for type: " + entityType.getTypeSignature()
					+ ". Make sure the type is declared in a model (not standalone), and this model is on the classpath.");
		return result;
	}

	public static Set<Model> findCoveringModels(Model... models) {
		return findCoveringModels(Arrays.asList(models));
	}

	public static Set<Model> findCoveringModels(Stream<Model> models) {
		return findCoveringModels(models::iterator);
	}

	public static Set<Model> findCoveringModels(Iterable<Model> models) {
		CommonModelsResolver instance = new CommonModelsResolver(models);
		return instance.findCoveringModelsImpl();
	}

	private final Iterable<Model> models;

	private final Set<Model> result = newSet();
	private final Set<Model> coverage = newSet();

	private CommonModelsResolver(Iterable<Model> models) {
		this.models = models;
	}

	private Set<Model> findCoveringModelsImpl() {
		for (Model model : models)
			ensureCovered(model);

		return result;
	}

	private void ensureCovered(Model model) {
		if (coverage.contains(model))
			return;

		Set<Model> modelCoverage = computeCoverageOf(model);
		result.removeAll(modelCoverage);
		result.add(model);

		coverage.addAll(modelCoverage);
	}

	private static Set<Model> computeCoverageOf(Model model) {
		Set<Model> result = newSet();
		computeCoverage(model, result);

		return result;
	}

	private static void computeCoverage(Model model, Set<Model> result) {
		if (result.add(model))
			for (Model dependency : model.getDependencies())
				computeCoverage(dependency, result);
	}

}
