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
package com.braintribe.model.processing.cortex.service.model;

import java.util.List;

import com.braintribe.model.cortexapi.model.AddDependencies;
import com.braintribe.model.cortexapi.model.DependenciesAdded;
import com.braintribe.model.cortexapi.model.DependenciesNotAdded;
import com.braintribe.model.cortexapi.model.MergeModelsResponse;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.notification.Level;
import com.braintribe.model.processing.cortex.service.ServiceBase;

public class DependenciesAdder extends ServiceBase {

	private AddDependencies request;
	
	public DependenciesAdder(AddDependencies request) {
		this.request = request;
	}
	
	public MergeModelsResponse run() {
		
		GmMetaModel model = request.getModel();
		List<GmMetaModel> dependencies = request.getDependencies();
		if (model == null) {
			return createConfirmationResponse("Please provide a model!", Level.WARNING, DependenciesNotAdded.T);
		}
		if (dependencies.isEmpty()) {
			return createConfirmationResponse("Please provide dependencies that should be added to model: "+model.getName(), Level.WARNING, DependenciesNotAdded.T);
		}
		List<GmMetaModel> modelDependencies = model.getDependencies();
		int count = 0;
		for (GmMetaModel dependency : dependencies) {
			if (modelDependencies.contains(dependency)) {
				notifyWarning("Model: "+model.getName()+" already has a dependency to model: "+dependency.getName());
				continue;
			}
			modelDependencies.add(dependency);
			count++;
		}
		
		
		DependenciesAdded response = null;
		switch (count) {
			case 0: 
				return createResponse("No dependencies added to model: "+model.getName(), DependenciesNotAdded.T);
			case 1: 
				response = createResponse("Added "+count+" dependency to model: "+model.getName(), DependenciesAdded.T);
				break;
			default:
				response = createResponse("Added "+count+" dependencies to model: "+model.getName(), DependenciesAdded.T);
		}
		response.setCount(count);		
		return response;
		
		
	}
}
