// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.graphlayout;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

import java.util.List;


import com.braintribe.model.graphlayout.geom.Point;


public interface Edge extends GraphLayoutElement {

	EntityType<Edge> T = EntityTypes.T(Edge.class);
	
	public void setKnots(List<Point> knots);
	public List<Point> getKnots();
	
	public void setFrom(Node from);
	public Node getFrom();
	
	public void setTo(Node to);
	public Node getTo();
	
	public void setMain(boolean main);
	public boolean getMain();

}
