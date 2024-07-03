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
package com.braintribe.model.processing.cortex.service.workbench;

import java.util.Collections;

import com.braintribe.logging.Logger;
import com.braintribe.model.access.AccessService;
import com.braintribe.model.accessapi.ModelEnvironmentServices;
import com.braintribe.model.cortexapi.workbench.CreateServiceRequestTemplate;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.generic.typecondition.TypeConditions;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.notification.Level;
import com.braintribe.model.notification.Notifications;
import com.braintribe.model.processing.cortex.service.ServiceBase;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.processing.workbench.WorkbenchInstructionProcessor;
import com.braintribe.model.workbench.TemplateInstantiationServiceRequestAction;
import com.braintribe.model.workbench.TemplateServiceRequestAction;
import com.braintribe.model.workbench.instruction.CreateTemplateBasedAction;

public class TemplateCreator extends ServiceBase {
	
	private static Logger logger = Logger.getLogger(TemplateCreator.class);

	private AccessService accessService;
	private PersistenceGmSessionFactory sessionFactory;
	private CreateServiceRequestTemplate request;

	
	public TemplateCreator(PersistenceGmSessionFactory sessionFactory, CreateServiceRequestTemplate request, AccessService accessService) {
		this.sessionFactory = sessionFactory;
		this.request = request;
		this.accessService = accessService;
	}
	
	public Notifications run() throws Exception {
	
		String workbenchAccessId = getWorkbenchAccessId();
		if (workbenchAccessId == null) {
			return createConfirmationResponse("No workbench access found!", Level.ERROR, Notifications.T);
		}
		PersistenceGmSession workbenchSession = sessionFactory.newSession(workbenchAccessId);
		
		CreateTemplateBasedAction instruction = CreateTemplateBasedAction.T.create();
		instruction.setActionName(request.getActionName());
		instruction.setPrototype(request.getTemplateRequest());
		instruction.setActionType((request.getInstantiationAction() ? TemplateInstantiationServiceRequestAction.T.getTypeSignature() : TemplateServiceRequestAction.T.getTypeSignature()));
		instruction.setPath(request.getFolderPath());
		instruction.setMultiSelectionSupport(request.getMultiSelectionSupport());
		instruction.setIgnoreProperties(request.getIgnoreProperties());
		instruction.setIgnoreStandardProperties(request.getIgnoreStandardProperties());
		
		GmEntityType criterionType = request.getCriterionType();
		if (criterionType != null) {
			instruction.setCriterion(
					TC.create()
						.typeCondition(
								TypeConditions.isAssignableTo(criterionType.getTypeSignature())
								).done());
		}
		
		
		
		WorkbenchInstructionProcessor instructionProcessor = new WorkbenchInstructionProcessor();
		instructionProcessor.setSession(workbenchSession);
		instructionProcessor.setCommitAfterProcessing(true);
		
		instructionProcessor.processInstructions(Collections.singletonList(instruction));
		
		
		addNotifications(com.braintribe.model.processing.notification.api.builder.Notifications.build()
						.add()
							.message().confirmInfo("Please consider reloading the ControlCenter! \n Do you want to reload now?")
							.command().reload("Reload ControlCenter")
						.close()
					.list());
		
		return createResponse("Added action template for: "+request.getTemplateRequest().entityType().getShortName()+" to workbench.", Notifications.T);
	}
	
	private String getWorkbenchAccessId() {
		String wbAccessId = request.getWorkbenchAccessId();
		if (wbAccessId == null) {
			wbAccessId = getDefaultWorkbenchAccessId();
		}
		return wbAccessId;
	}

	private String getDefaultWorkbenchAccessId() {
		String domainId = request.getDomainId();
		try {
			ModelEnvironmentServices modelEnvironmentServices = accessService.getModelEnvironmentServices(domainId);
			return modelEnvironmentServices.getWorkbenchModelAccessId();
		} catch (Exception e) {
			logger.warn("Could not determine workbenchAccess for domain: "+domainId,e);
			return null;
		}
	}
}
