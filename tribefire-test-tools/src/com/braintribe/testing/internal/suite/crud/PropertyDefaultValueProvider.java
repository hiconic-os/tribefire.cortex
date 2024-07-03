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
