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
package com.braintribe.model.graphlayout;

import com.braintribe.model.generic.annotation.Abstract;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

import javax.net.ssl.HandshakeCompletedListener;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.StandardIdentifiable;

@Abstract
public interface GraphLayoutElement extends GenericEntity, HasElementIdentifcation{

	EntityType<GraphLayoutElement> T = EntityTypes.T(GraphLayoutElement.class);
	
	public void setDenotation(GenericEntity denotation);
	public GenericEntity getDenotation();
	
	public void setLayout(GraphLayout graphLayout);
	public GraphLayout getLayout();
	
	public void setOpacity(Double opacity);
	public Double getOpacity();

}
