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
package com.braintribe.model.deploymentapi.check.data;

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

import tribefire.cortex.model.check.CheckCoverage;
import tribefire.cortex.model.check.CheckWeight;

public interface CheckBundleFilters extends GenericEntity {

	EntityType<CheckBundleFilters> T = EntityTypes.T(CheckBundleFilters.class);
	
	Set<String> getModule();
	void setModule(Set<String> module);
	
	Set<String> getDeployable();
	void setDeployable(Set<String> deployable);
	
	Set<String> getLabel();
	void setLabel(Set<String> label);
	
	Set<String> getName();
	void setName(Set<String> name);
	
	Set<String> getNode();
	void setNode(Set<String> node);

	CheckWeight getWeight();
	void setWeight(CheckWeight weight);
	
	Set<CheckCoverage> getCoverage();
	void setCoverage(Set<CheckCoverage> coverage);
	
	Set<String> getRole();
	void setRole(Set<String> role);
	

	/** @deprecated Allegedly made sense with cartridges. Ignored now. */
	@Deprecated
	Boolean getIsPlatformRelevant();
	/** @deprecated Allegedly made sense with cartridges. Ignored now. */
	@Deprecated
	void setIsPlatformRelevant(Boolean isPlatformRelevant);
	
}
