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
package com.braintribe.model.instancegraphlayout;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.graphlayout.GraphLayout;
import com.braintribe.model.graphlayout.Node;
import com.braintribe.model.graphlayout.PagedGraphLayout;
import com.braintribe.model.generic.annotation.SelectiveInformation;

@SelectiveInformation("instanceGraphLayout: ${identification}")
public interface AccessGraphLayout extends PagedGraphLayout {

	final EntityType<AccessGraphLayout> T = EntityTypes.T(AccessGraphLayout.class);
	
	public Node getFocus();
	public void setFocus(Node node);
	
	public Node getBacklink();
	public void setBacklink(Node node);
	
}
