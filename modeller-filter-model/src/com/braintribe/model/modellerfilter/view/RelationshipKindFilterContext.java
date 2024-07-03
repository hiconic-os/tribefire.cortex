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
package com.braintribe.model.modellerfilter.view;


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.modellerfilter.JunctionRelationshipFilter;


public interface RelationshipKindFilterContext extends JunctionRelationshipFilter {

	EntityType<RelationshipKindFilterContext> T = EntityTypes.T(RelationshipKindFilterContext.class);
	
	public void setAbstract(boolean useAbstract);
	public boolean getAbstract();
	
	public void setGeneralization(boolean generalization);
	public boolean getGeneralization();
	
	public void setSpecialization(boolean specialization);
	public boolean getSpecialization();
	
	public void setAggregation(boolean aggregation);
	public boolean getAggregation();
	
	public void setInverseAggregation(boolean inverseAggregation);
	public boolean getInverseAggregation();
	
	public void setMapping(boolean mapping);
	public boolean getMapping();
	
}
