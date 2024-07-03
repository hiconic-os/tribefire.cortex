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
package com.braintribe.tribefire.jdbc.statement;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;

/**
 * The Class TargetExpression.
 *
 */
public class TargetExpression {

	private Type type;
	private String alias;
	private String name;
	private Object value;
	private Integer index;
	
	/**
	 * The Enum Type.
	 */
	public enum Type { value, property, namedparameter, indexedparameter }
	
	/**
	 * Instantiates a new target expression.
	 *
	 * @param type
	 *            the type
	 */
	public TargetExpression(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Property getProperty(EntityType<?> entityType) {		
		if (name.contains(".")) {
			// workaround for FK-ID properties
			return entityType.getProperty(name.substring(0, name.indexOf(".")));
		} else {
			return entityType.getProperty(name);
		}
	}
	
}
