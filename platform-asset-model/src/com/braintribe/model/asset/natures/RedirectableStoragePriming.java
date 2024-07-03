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
package com.braintribe.model.asset.natures;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface RedirectableStoragePriming extends StoragePriming {
	
	EntityType<RedirectableStoragePriming> T = EntityTypes.T(RedirectableStoragePriming.class);

	Map<String, AccessIds> getRedirects();
	void setRedirects(Map<String, AccessIds> redirects);
	
	@Override
	default Stream<String> effectiveAccessIds() {
		Set<String> effectiveAccessIds = new HashSet<>();
		Map<String, AccessIds> redirects = getRedirects();
		
		configuredAccesses().forEach(a -> {
			AccessIds accessIds = redirects.get(a);
			if (accessIds != null)
				effectiveAccessIds.addAll(accessIds.getAccessIds());
			else
				effectiveAccessIds.add(a);
		});
		
		return effectiveAccessIds.stream();
	}

	
	default Stream<String> configuredAccesses() {
		throw new UnsupportedOperationException("This RedirectableStoragePriming - " + entityType().getTypeSignature()
				+ " - doesn't implement the required method 'configuredAccesses'.");
	}
}
