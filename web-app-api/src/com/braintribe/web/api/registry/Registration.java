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
package com.braintribe.web.api.registry;

import java.util.Map;

public interface Registration extends Comparable<Registration> {

	Map<String, String> getInitParameters();

	Integer getOrder();

	@Override
	public default int compareTo(Registration o) {
		Integer f = getOrder();
		Integer s = o.getOrder();
		if (f == null && s == null)
			return 0;
		if (f == null)
			return -1;
		if (s == null)
			return 1;
		return Integer.compare(f, s);
	}

}
