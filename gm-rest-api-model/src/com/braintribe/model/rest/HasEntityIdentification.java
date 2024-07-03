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
package com.braintribe.model.rest;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.generic.value.EntityReference;

@Abstract
public interface HasEntityIdentification extends HasEntityType {

	EntityType<HasEntityIdentification> T = EntityTypes.T(HasEntityIdentification.class);

	void setEntityId(Object id);
	Object getEntityId();

	String getPart();
	void setPart(String part);

	/**
	 * Returns a partition value to use as a {@link EntityReference#getRefPartition() entity reference partition} value.
	 * It's either the value of the {@link #getPart() part}, if not <tt>null</tt>, or
	 * {@link EntityReference#ANY_PARTITION} otherwise.
	 */
	default String partitionForReference() {
		String part = getPart();
		return part != null ? part : EntityReference.ANY_PARTITION;
	}

}
