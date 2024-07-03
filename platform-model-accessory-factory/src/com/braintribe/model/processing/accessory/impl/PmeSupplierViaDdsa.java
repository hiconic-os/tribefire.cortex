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
package com.braintribe.model.processing.accessory.impl;

import static java.util.Collections.emptyList;

import com.braintribe.cfg.Required;
import com.braintribe.model.accessory.GetAccessModel;
import com.braintribe.model.accessory.ModelRetrievingRequest;
import com.braintribe.model.accessory.GetModelByName;
import com.braintribe.model.accessory.GetServiceModel;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.accessory.api.PlatformModelEssentials;
import com.braintribe.model.processing.accessory.api.PlatformModelEssentialsSupplier;
import com.braintribe.model.processing.accessory.api.PmeKey;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * {@link PlatformModelEssentialsSupplier} which resolves the relevant models via DDSA.
 *
 * @see PmeSupplierFromCortex
 * 
 * @author peter.gazdik
 */
public class PmeSupplierViaDdsa extends PmeSupplierBase {

	private Evaluator<ServiceRequest> evaluator;

	/**
	 * The evaluator for retrieving the models for the {@link PlatformModelEssentials}.
	 * <p>
	 * It must be authorized to evaluate {@link ModelRetrievingRequest}s.
	 */
	@Required
	public void setEvaluator(Evaluator<ServiceRequest> evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	protected PlatformModelEssentials createNewAccessPme(String accessId, String perspective, boolean extended) {
		return createBasedOn(GetAccessModel.create(accessId, perspective, extended));
	}

	@Override
	protected PlatformModelEssentials createNewServiceDomainPme(String serviceDomainId, String perspective, boolean extended) {
		return createBasedOn(GetServiceModel.create(serviceDomainId, perspective, extended));
	}

	@Override
	public PlatformModelEssentials getForModelName(String modelName, String perspective) {
		return createBasedOn(GetModelByName.create(modelName, perspective));
	}

	private PlatformModelEssentials createBasedOn(ModelRetrievingRequest getModel) {
		GmMetaModel model = getModel.eval(evaluator).get();
		
		return new BasicPmeBuilder(model, PmeKey.create(model.getName(), getModel.getPerspective(), null, emptyList())).build();
	}

}
