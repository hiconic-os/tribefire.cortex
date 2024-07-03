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
package com.braintribe.model.processing.platformsetup;

import java.io.Writer;

import com.braintribe.codec.marshaller.api.GmSerializationOptions;
import com.braintribe.codec.marshaller.api.OutputPrettiness;
import com.braintribe.exception.Exceptions;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.manipulation.marshaller.ManMarshaller;
import com.braintribe.model.processing.manipulation.marshaller.ResultNaming;

public abstract class ManipulationRecording {

	private static ManMarshaller manMarshaller = new ManMarshaller();

	public static void stringify(Writer writer, GenericEntity entity, String instanceVar, String defaultInstanceTypeVar) {

		try {
			if (defaultInstanceTypeVar != null && isDefaultInstance(entity)) {
				writer.append(defaultInstanceTypeVar);
				writer.append(" = ");
				writer.append(entity.entityType().getTypeSignature());
				return;

			} else {
				manMarshaller.marshall(writer, entity, GmSerializationOptions.deriveDefaults().stabilizeOrder(true)
						.set(ResultNaming.class, instanceVar).outputPrettiness(OutputPrettiness.high).build());
			}

		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Error while stringifying manipulations!");
		}

	}

	private static boolean isDefaultInstance(GenericEntity entity) {
		for (Property property : entity.entityType().getProperties()) {
			Object initializer = property.getInitializer();

			if (initializer != null) {
				if (!nullSafeEquals(property.get(entity), initializer))
					return false;
			} else {
				if (!nullSafeEquals(property.get(entity), property.getDefaultRawValue()))
					return false;
			}
		}

		return true;
	}

	private static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2)
			return true;

		if (o1 == null || o2 == null)
			return false;

		return o1.equals(o2);
	}

}
