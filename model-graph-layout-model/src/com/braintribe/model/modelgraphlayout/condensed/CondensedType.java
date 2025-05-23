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
package com.braintribe.model.modelgraphlayout.condensed;

import java.util.Set;


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmType;


public interface CondensedType extends CondensedModelElement {

	final EntityType<CondensedType> T = EntityTypes.T(CondensedType.class);

	// @formatter:off
	GmType getGmType();
	void setGmType(GmType gmType);

	CondensedModel getModel();
	void setModel(CondensedModel model);

	Set<CondensedRelationship> getRelationships();
	void setRelationships(Set<CondensedRelationship> relationships);
	// @formatter:on

}
