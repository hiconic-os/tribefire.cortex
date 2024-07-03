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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.braintribe.model.descriptive.HasLocalizedDescription;
import com.braintribe.model.descriptive.HasLocalizedName;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface GraphLayout extends GenericEntity, HasLocalizedName, HasLocalizedDescription{

	EntityType<GraphLayout> T = EntityTypes.T(GraphLayout.class);
	
	public void setElements(Set<GraphLayoutElement> elements);
	public Set<GraphLayoutElement> getElements();
	
	public void setIdentification(String identification);
	public String getIdentification();
	
	public void setDenotation(GenericEntity denotation);
	public GenericEntity getDenotation();
	
	default Stream<GraphLayoutElement> elements() {
		return (Stream<GraphLayoutElement>) (Object) getElements().stream().filter((element) -> {return element instanceof GraphLayoutElement;});
	}
	
	default Stream<Node> nodes() {
		return (Stream<Node>) (Object) getElements().stream().filter((element) -> {return element instanceof Node;});
	}
	
	default Set<Node> nodesSet() {
		return nodes().collect(Collectors.toSet());
	}
	
	default Stream<Edge> edges() {
		return (Stream<Edge>) (Object) getElements().stream().filter((element) -> {return element instanceof Edge;});
	}
	
	default Set<Edge> edgesSet() {
		return edges().collect(Collectors.toSet());
	}

}
