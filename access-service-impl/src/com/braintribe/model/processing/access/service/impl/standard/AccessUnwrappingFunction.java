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

import com.braintribe.common.lcd.annotations.Nullable;
import com.braintribe.model.access.IncrementalAccess;

/**
 * {@link Function} implementation for unwrapping an {@link IncrementalAccess} from an
 * {@link OriginAwareAccessRegistrationInfo}.
 * 
 * 
 */
public class AccessUnwrappingFunction implements Function<OriginAwareAccessRegistrationInfo, IncrementalAccess> {

	/**
	 * Unwraps the {@link IncrementalAccess} from an {@link OriginAwareAccessRegistrationInfo}. In case <code>access</code>
	 * is <code>null</code>, this method returns <code>null</code>.
	 */
	@Override
	@Nullable
	public IncrementalAccess apply(@Nullable OriginAwareAccessRegistrationInfo wrapper) {
		if (wrapper == null) {
			return null;
		}
		return wrapper.getAccess();
	}
}
