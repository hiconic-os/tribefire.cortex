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
package com.braintribe.model.deploymentreflection;

import java.util.List;
import java.util.Map;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.InstanceId;

public interface DeploymentSummary extends GenericEntity {

	EntityType<DeploymentSummary> T = EntityTypes.T(DeploymentSummary.class);

	QualifiedDeployedUnits getTotalUnits();
	void setTotalUnits(QualifiedDeployedUnits totalUnits);
	
	Map<String,QualifiedDeployedUnits> getUnitsByNode();
	void setUnitsByNode(Map<String,QualifiedDeployedUnits> unitsByNode);
	
	Map<String,QualifiedDeployedUnits> getUnitsByCartridge();
	void setUnitsByCartridge(Map<String,QualifiedDeployedUnits> unitsByCartridge);
	
	Map<Deployable,QualifiedDeployedUnits> getUnitsByDeployable();
	void setUnitsByDeployable(Map<Deployable,QualifiedDeployedUnits> unitsByDeployable);
	
	List<QualifiedDeployedUnits> getSourceUnits();
	void setSourceUnits(List<QualifiedDeployedUnits> sourceUnits);
	
	Map<InstanceId,QualifiedDeployedUnits> getUnitsByInstanceId();
	void setUnitsByInstanceId(Map<InstanceId,QualifiedDeployedUnits> unitsByInstanceId);
	
}
