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
package com.braintribe.model.modellergraph.graphics;

import java.util.Map;


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface ModelGraphState extends GraphElement {

	final EntityType<ModelGraphState> T = EntityTypes.T(ModelGraphState.class);

	// @formatter:off
	Map<String, Edge> getEdges();
	void setEdges(Map<String, Edge> edges);

	Map<String, Node> getNodes();
	void setNodes(Map<String, Node> nodes);
	
	boolean getHasMore();
	void setHasMore(boolean hasMore);
	
	boolean getHasLess();
	void setHasLess(boolean hasLess);

	// List<Node> getNodes();
	// void setNodes(List<Node> nodes);
	//
	// List<Edge> getEdges();
	// void setEdges(List<Edge> edges);
	// @formatter:on

}
