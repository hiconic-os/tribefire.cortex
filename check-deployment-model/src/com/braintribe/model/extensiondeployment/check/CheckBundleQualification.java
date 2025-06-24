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
package com.braintribe.model.extensiondeployment.check;

import java.util.Set;

import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.deployment.Module;
import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

import tribefire.cortex.model.check.CheckCoverage;
import tribefire.cortex.model.check.CheckWeight;

@Description("Bundles, qualifies and associates checks.")
public interface CheckBundleQualification extends HasName {
	
	EntityType<CheckBundleQualification> T = EntityTypes.T(CheckBundleQualification.class);

	String module = "module";
	String deployable = "deployable";
	String coverage = "coverage";
	String weight = "weight";
	String labels = "labels";
	String roles = "roles";
	String isPlatformRelevant = "isPlatformRelevant";
	
	Module getModule();
	void setModule(Module module);

	Deployable getDeployable();
	void setDeployable(Deployable deployable);
	
	CheckCoverage getCoverage();
	void setCoverage(CheckCoverage coverage);
	
	CheckWeight getWeight();
	void setWeight(CheckWeight weight);
	
	Set<String> getLabels();
	void setLabels(Set<String> labels);
	
	Set<String> getRoles();
	void setRoles(Set<String> roles);
	
	/** @deprecated Allegedly made sense with cartridges. Ignored now. */
	@Deprecated
	boolean getIsPlatformRelevant();
	/** @deprecated Allegedly made sense with cartridges. Ignored now. */
	@Deprecated
	void setIsPlatformRelevant(boolean isPlatformRelevant);
}
