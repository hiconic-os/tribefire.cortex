// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.spapi;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.manipulation.EntityProperty;
import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.generic.value.EntityReference;

/**
 * a generic entity to hold all information required to execute a StateChangeProcessor <br/>
 * contains :<br/>
 * {@link com.braintribe.model.generic.manipulation.Manipulation} manipulation: the manipulation that lead to the
 * triggering <br/>
 * {@link com.braintribe.model.generic.manipulation.EntityProperty} entityProperty : the entity property involved if
 * given<br/>
 * {@link com.braintribe.model.generic.GenericEntity} customData : custom data from the
 * StateChangeProcessor.onBeforeStateChange <br/>
 * String accessId : the id of the access that that the StateChangeProcessor is associated with <br/>
 * String processorId : the id of the processor<br/>
 * 
 * @author pit
 * @author dirk
 */
public interface StateChangeProcessorInvocation extends GenericEntity {

	EntityType<StateChangeProcessorInvocation> T = EntityTypes.T(StateChangeProcessorInvocation.class);

	Manipulation getManipulation();
	void setManipulation(Manipulation manipulation);

	EntityProperty getEntityProperty();
	void setEntityProperty(EntityProperty entityProperty);

	EntityReference getEntityReference();
	void setEntityReference(EntityReference entityReference);

	GenericEntity getCustomData();
	void setCustomData(GenericEntity customData);

	String getAccessId();
	void setAccessId(String accessId);

	String getProcessorId();
	void setProcessorId(String processorId);

	String getRuleId();
	void setRuleId(String ruleId);

}
