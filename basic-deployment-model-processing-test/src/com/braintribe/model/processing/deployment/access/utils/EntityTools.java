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
package com.braintribe.model.processing.deployment.access.utils;

import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_BOOLEAN;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_DATE;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_DECIMAL;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_DOUBLE;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_FLOAT;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_INTEGER;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_LONG;
import static com.braintribe.model.generic.reflection.GenericModelTypeReflection.TYPE_STRING;

import java.math.BigDecimal;
import java.util.Date;

import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;
import com.braintribe.model.generic.reflection.SimpleType;

/**
 * 
 */
public class EntityTools {

	public static boolean isIntegerType(GenericModelType type) {
		if (!(type instanceof SimpleType)) {
			return false;
		}

		String ts = type.getTypeSignature();
		return ts.equals(GenericModelTypeReflection.TYPE_INTEGER.getTypeSignature())
				|| ts.equals(GenericModelTypeReflection.TYPE_LONG.getTypeSignature());
	}

	public static Object getValueForSimpleType(SimpleType pt) {
		if (pt == TYPE_BOOLEAN) {
			return true;
		} else if (pt == TYPE_DATE) {
			return new Date();
		} else if (pt == TYPE_DECIMAL) {
			return new BigDecimal("10000000000000" + System.currentTimeMillis());
		} else if (pt == TYPE_DOUBLE) {
			return (double) System.currentTimeMillis();
		} else if (pt == TYPE_FLOAT) {
			return (float) System.currentTimeMillis();
		} else if (pt == TYPE_INTEGER) {
			return (int) System.currentTimeMillis() % (1 << 30);
		} else if (pt == TYPE_LONG) {
			return System.currentTimeMillis();
		} else if (pt == TYPE_STRING) {
			return "str: " + System.currentTimeMillis();
		}
		return null;
	}

}
