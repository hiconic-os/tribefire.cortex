// ============================================================================
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
package com.braintribe.testing.internal.suite.crud;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.SimpleTypes;
import com.braintribe.model.processing.meta.cmd.builders.PropertyMdResolver;

public class PropertyDefaultValueProvider implements PropertyValueProvider {
	private GenericModelType getType(PropertyMdResolver propertyMd) {
		return propertyMd.getGmProperty().type();
	}

	@Override
	public Object getSimple(PropertyMdResolver propertyMd) {
		GenericModelType propertyType = getType(propertyMd);

		Map<GenericModelType, Object> defaultValues = new HashMap<>();
		defaultValues.put(SimpleTypes.TYPE_STRING, getString(propertyMd));
		defaultValues.put(SimpleTypes.TYPE_BOOLEAN, true);
		defaultValues.put(SimpleTypes.TYPE_DATE, new Date());
		defaultValues.put(SimpleTypes.TYPE_DECIMAL, new BigDecimal(2.4));
		defaultValues.put(SimpleTypes.TYPE_DOUBLE, 2.4d);
		defaultValues.put(SimpleTypes.TYPE_FLOAT, 2.4f);
		defaultValues.put(SimpleTypes.TYPE_INTEGER, getInteger(propertyMd));
		defaultValues.put(SimpleTypes.TYPE_LONG, 2l);

		// logger.debug("Set to " + defaultValues.get(propertyType));
		return defaultValues.get(propertyType);
	}

	@Override
	public Enum<?> getEnum(PropertyMdResolver propertyMd) {
		GenericModelType propertyType = getType(propertyMd);

		EnumType enumType = ((EnumType) propertyType);
		Enum<?> enumConstantJava = enumType.getEnumValues()[0];

		return enumConstantJava;
	}

	@Override
	public String getString(PropertyMdResolver propertyMd) {
		return "Hallo";
	}

	@Override
	public Integer getInteger(PropertyMdResolver propertyMd) {
		return 2;
	}

}
