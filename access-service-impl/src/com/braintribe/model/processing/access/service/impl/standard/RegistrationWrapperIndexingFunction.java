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
package com.braintribe.model.processing.access.service.impl.standard;

import java.util.function.Function;

import com.braintribe.common.lcd.annotations.NonNull;

/**
 * {@link Function} implementation for indexing an {@link OriginAwareAccessRegistrationInfo} based on its
 * <code>accessId</code>.
 * 
 * 
 */
public class RegistrationWrapperIndexingFunction implements Function<OriginAwareAccessRegistrationInfo, String> {

	/**
	 * Returns {@link OriginAwareAccessRegistrationInfo#getAccessId()} of the input.
	 */
	@Override
	public String apply(@NonNull OriginAwareAccessRegistrationInfo input) {
		return input.getAccessId();
	}
}
