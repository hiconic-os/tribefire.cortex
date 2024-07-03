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

import com.braintribe.model.cortexapi.model.ModelChangeResponse;
import com.braintribe.model.cortexapi.model.NotifyModelChanged;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.modelnotification.OnModelChanged;
import com.braintribe.model.notification.Level;
import com.braintribe.model.processing.cortex.service.ServiceBase;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class ModelChangeNotifier extends ServiceBase {

	private NotifyModelChanged request;
	private PersistenceGmSession session;
	
	public ModelChangeNotifier(NotifyModelChanged request, PersistenceGmSession session) {
		this.request = request;
		this.session = session;
	}
	
	public ModelChangeResponse run() {
		
		GmMetaModel model = request.getModel();
		if (model == null) {
			return createConfirmationResponse("Please provide a model.", Level.WARNING, ModelChangeResponse.T);
		}
		
		String modelName = model.getName();
		OnModelChanged onModelChanged = OnModelChanged.T.create();
		onModelChanged.setModelName(modelName);
		onModelChanged.eval(session).get();
		
		return createResponse("Notified internal caches about changes in model: "+modelName, ModelChangeResponse.T);
	}
	
	
}
