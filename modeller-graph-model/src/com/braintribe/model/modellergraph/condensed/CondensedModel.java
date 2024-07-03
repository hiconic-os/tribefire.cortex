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
package com.braintribe.model.modellergraph.condensed;

import java.util.Set;


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmMetaModel;


public interface CondensedModel extends CondensedModelElement {

	final EntityType<CondensedModel> T = EntityTypes.T(CondensedModel.class);

	// @formatter:off
	GmMetaModel getModel();
	void setModel(GmMetaModel model);

	Set<CondensedType> getTypes();
	void setTypes(Set<CondensedType> types);
	// @formatter:on

}
