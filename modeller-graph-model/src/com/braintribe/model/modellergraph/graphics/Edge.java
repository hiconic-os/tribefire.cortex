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


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmProperty;


public interface Edge extends GraphElement {

	final EntityType<Edge> T = EntityTypes.T(Edge.class);

	// @formatter:off
	String getName();
	void setName(String name);
	
	String getInverseName();
	void setInverseName(String inverseName);

	String getDescription();
	void setDescription(String description);

	Point getStart();
	void setStart(Point start);

	Point getEnd();
	void setEnd(Point end);

	Point getTurning();
	void setTurning(Point turning);

	Point getStartControl();
	void setStartControl(Point startControl);

	// Point getTurningControl();
	// void setTurningControl(Point turningControl);
	Point getEndControl();
	void setEndControl(Point endControl);

	// DockingKind getStartDockingKind();
	// void setStartDockingKind(DockingKind startDockingKind);

	// DockingKind getEndDockingKind();
	// void setEndDockingKind(DockingKind endDockingKind);

	Color getColor();
	void setColor(Color color);
	
	Color getStartColor();
	void setStartColor(Color color);
	
	Color getEndColor();
	void setEndColor(Color color);

	AggregationKind getStartAggregationKind();
	void setStartAggregationKind(AggregationKind startAggregationKind);

	AggregationKind getEndAggregationKind();
	void setEndAggregationKind(AggregationKind endAggregationKind);

	GeneralizationKind getGeneralizationKind();
	void setGeneralizationKind(GeneralizationKind generalizationKind);

	Node getFromNode();
	void setFromNode(Node fromNode);

	Node getToNode();
	void setToNode(Node toNode);

	double getOrder();
	void setOrder(double order);

	GmProperty getGmProperty();
	void setGmProperty(GmProperty gmProperty);

	boolean getCircular();
	void setCircular(boolean circular);
	
	boolean getAbove();
	void setAbove(boolean above);
	
	int getIndex();
	void setIndex(int index);
	// @formatter:on

}
