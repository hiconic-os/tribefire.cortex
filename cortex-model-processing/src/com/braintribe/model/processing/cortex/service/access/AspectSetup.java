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
package com.braintribe.model.processing.cortex.service.access;

import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.accessdeployment.aspect.AspectConfiguration;
import com.braintribe.model.cortexapi.access.SetupAccessResponse;
import com.braintribe.model.extensiondeployment.AccessAspect;
import com.braintribe.model.processing.cortex.service.ServiceBase;
import com.braintribe.model.processing.notification.api.builder.Notifications;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class AspectSetup extends ServiceBase {

	private static final Logger logger = Logger.getLogger(AspectSetup.class);
	private List<AccessAspect> defaultAspects;
	private PersistenceGmSession session;
	private IncrementalAccess access;
	
	public AspectSetup(PersistenceGmSession session, IncrementalAccess access, List<AccessAspect> defaultAspects) {
		this.session = session;
		this.defaultAspects = defaultAspects;
		this.access = access;
	}

	
	public SetupAccessResponse run(boolean resetToDefault) {
		
		AspectConfiguration aspectConfiguration = access.getAspectConfiguration();
		if (aspectConfiguration == null) {
			notifyInfo("Create new AspectConfiguration for access: "+access.getExternalId(), logger);
			aspectConfiguration = session.create(AspectConfiguration.T);
			access.setAspectConfiguration(aspectConfiguration);
		} else {
			notifyInfo("Ensure aspects on existing AspectConfiguration of access: "+access.getExternalId(), logger);
			if (resetToDefault) {
				notifyInfo("Cleaning aspects on existing AspectConfiguration of access: "+access.getExternalId()+" (reset=true)", logger);
				aspectConfiguration.getAspects().clear();
			}
		}
		
		
		List<AccessAspect> aspects = aspectConfiguration.getAspects();
		boolean addedDefaults = false;
		for (AccessAspect defaultAspect : defaultAspects) {
			
			AccessAspect cortexInstance = findAspect(session, defaultAspect.getExternalId());
			if (cortexInstance == null){
				notifyWarning("No instance of default deployable: "+defaultAspect.getExternalId()+" found in cortex", logger);
				continue;
			}
			
			if (!aspects.contains(cortexInstance)) {
				aspects.add(cortexInstance);
				notifyInfo("Added missing default aspect: "+defaultAspect.getExternalId(), logger);
				addedDefaults = true;
			}
			
		}
		
		if (!addedDefaults) {
			notifyInfo("All default aspects were found on AspectConfiguration of access: "+access.getExternalId()+". Nothing added.", logger);
		}
		
		addNotifications(Notifications.build().add().command().refresh("Refresh Access").close().list());
		return createResponse("Ensured default aspects for access: "+access.getExternalId(), SetupAccessResponse.T);
	}


	
}
