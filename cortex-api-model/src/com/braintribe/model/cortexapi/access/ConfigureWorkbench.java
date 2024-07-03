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
package com.braintribe.model.cortexapi.access;

import java.util.List;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.workbench.instruction.WorkbenchInstruction;

public interface ConfigureWorkbench extends SetupAccessRequest {

	EntityType<ConfigureWorkbench> T = EntityTypes.T(ConfigureWorkbench.class);

	@Initializer("true")
	boolean getEnsureStandardFolders();

	void setEnsureStandardFolders(boolean ensureStandardFolders);

	ExplorerStyle getExplorerStyle();

	void setExplorerStyle(ExplorerStyle explorerStyle);

	List<WorkbenchInstruction> getInstructions();

	void setInstructions(List<WorkbenchInstruction> instructions);

	// TODO visibility based on whether the workbench had been setup already
	// Extension point for when a core decision is made to optionally combine
	// setup and configure workbench.
	
	// boolean getResetExistingAccess();
	// void setResetExistingAccess(boolean resetExistingAccess);
	//
	// @Initializer("true")
	// boolean getResetExistingModel();
	// void setResetExistingModel(boolean resetExistingModel);

}
