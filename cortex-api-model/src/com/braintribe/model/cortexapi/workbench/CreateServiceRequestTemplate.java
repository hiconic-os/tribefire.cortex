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
package com.braintribe.model.cortexapi.workbench;

import java.util.Set;

import com.braintribe.model.accessapi.AccessRequest;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.service.api.ServiceRequest;

public interface CreateServiceRequestTemplate extends AccessRequest {

	EntityType<CreateServiceRequestTemplate> T = EntityTypes.T(CreateServiceRequestTemplate.class);

	@Mandatory
	ServiceRequest getTemplateRequest();
	void setTemplateRequest(ServiceRequest templateRequest);

	@Mandatory
	String getActionName();
	void setActionName(String actionName);
	
	@Initializer("'actionbar/more'")
	String getFolderPath();
	void setFolderPath(String folderPath);
	
	Set<String> getIgnoreProperties();
	void setIgnoreProperties(Set<String> ignoreProperties);

	@Initializer("true")
	boolean getIgnoreStandardProperties();
	void setIgnoreStandardProperties(boolean ignoreStandardProperties);
	
	GmEntityType getCriterionType();
	void setCriterionType(GmEntityType criterionType);
	
	boolean getMultiSelectionSupport();
	void setMultiSelectionSupport(boolean multiSelectionSupport);	

	boolean getInstantiationAction();
	void setInstantiationAction(boolean instantiationAction);
	
	String getWorkbenchAccessId();
	void setWorkbenchAccessId(String workbenchAccessId);
	
}
