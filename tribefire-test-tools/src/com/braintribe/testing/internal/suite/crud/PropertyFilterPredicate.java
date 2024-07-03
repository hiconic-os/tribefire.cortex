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

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.data.constraint.Mandatory;
import com.braintribe.model.meta.data.constraint.Unique;
import com.braintribe.model.processing.meta.cmd.builders.PropertyMdResolver;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.utils.lcd.CommonTools;

/**
 * Lets you state if you want to ignore a property (false) or not (true) during testing
 * 
 * @author Neidhart
 *
 */
@FunctionalInterface
public interface PropertyFilterPredicate {

	/**
	 * Lets you state if you want to ignore a property (false) or not (true) during testing
	 * 
	 * @return
	 */
	Boolean test(Property property, GenericEntity entity, PersistenceGmSession session);
	
//	public static PropertyFilterPredicate ignorePropertiesWithName(String ... names) {
//		
//		return (Property property, GenericEntity entity, PersistenceGmSession _session) -> {
//			return !CommonTools.getSet(names).contains(property.getName());
//		};
//	}

}
