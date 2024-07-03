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

import com.braintribe.model.generic.reflection.Property;

/**
 * The Class PropertyWrapper.
 *
 */
public class PropertyWrapper {

	private Property property;
	private String alias;
	private Object fixedValue;
	private String entityType;

	/**
	 * Instantiates a new property wrapper.
	 *
	 * @param p
	 *            the p
	 * @param alias
	 *            the alias
	 */
	public PropertyWrapper(Property p, String alias) {
		this(p, alias, null);
	}

	/**
	 * Instantiates a new property wrapper.
	 *
	 * @param alias
	 *            the alias
	 * @param fixedValue
	 *            the fixed value
	 */
	public PropertyWrapper(String alias, Object fixedValue) {
		this.alias = (alias == null ? fixedValue.toString() : alias);
		this.fixedValue = fixedValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PropertyWrapper [property=" + property + ", alias=" + alias + ", fixedValue=" + fixedValue
				+ ", entityType=" + entityType + "]";
	}

	/**
	 * Instantiates a new property wrapper.
	 *
	 * @param p
	 *            the p
	 * @param alias
	 *            the alias
	 * @param entityType
	 *            the entity type
	 */
	public PropertyWrapper(Property p, String alias, String entityType) {
		this.property = p;
		this.alias = alias != null ? alias : p.getName();
		this.entityType = entityType;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Object getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Object fixedValue) {
		this.fixedValue = fixedValue;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}
