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
package com.braintribe.model.cortexapi.model;

import java.util.List;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmMetaModel;

@Abstract
public interface CreateModelRequest extends ModelRequest {
	
	EntityType<CreateModelRequest> T = EntityTypes.T(CreateModelRequest.class);

	@Mandatory
	@Description("The name of the model.")
	public String getName();
	public void setName(String name);
	
	@Initializer("'custom.model'")
	@Mandatory
	@Description("The group id of the model.")
	public String getGroupId();
	public void setGroupId(String groupId);
	
	@Initializer("'1.0'")
	@Mandatory
	@Description("The version of the model.")
	public String getVersion();
	public void setVersion(String version);
	
	@Description("An optional list of model dependencies. If not specified the root-model will be added as dependency to the new created model.")
	public List<GmMetaModel> getDependencies();
	public void setDependencies(List<GmMetaModel> dependencies);

}
