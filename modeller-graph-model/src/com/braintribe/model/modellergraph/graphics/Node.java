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
import com.braintribe.model.modellergraph.condensed.CondensedType;


public interface Node extends GraphElement {

	final EntityType<Node> T = EntityTypes.T(Node.class);

	// @formatter:off
	Object getCustomData();
	void setCustomData(Object customData);

	Point getCenter();
	void setCenter(Point center);

	Double getRadius();
	void setRadius(Double radius);

	String getText();
	void setText(String text);

	Color getColor();
	void setColor(Color color);

	double getOrder();
	void setOrder(double order);

	String getTypeSignature();
	void setTypeSignature(String typeSignature);

	CondensedType getType();
	void setType(CondensedType type);

	boolean getPinned();
	void setPinned(boolean pinned);

	boolean getSelected();
	void setSelected(boolean selected);
	// @formatter:on
}
