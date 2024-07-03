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


import java.util.Set;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface IncludesFilterContext extends ExtendedJunctionRelationshipFilter {
	
	EntityType<IncludesFilterContext> T = EntityTypes.T(IncludesFilterContext.class);

	public void setDeclaredTypes(boolean declaredTypes);
	public boolean getDeclaredTypes();
	
	public void setExplicitTypes(boolean explicitTypes);
	public boolean getExplicitTypes();
	
	public void setAllIncludedTypes(boolean allIncludedTypes);
	public boolean getAllIncludedTypes();
	
	public void setAddedTypes(Set<String> addedTypes);
	public Set<String> getAddedTypes();
	
}
