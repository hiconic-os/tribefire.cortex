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
package com.braintribe.web.impl.registry;

import java.util.Collections;
import java.util.Map;

import com.braintribe.web.api.registry.Registration;

public abstract class ConfigurableRegistration implements Registration {

	protected Map<String, String> initParameters = Collections.emptyMap();
	private Integer order;

	@Override
	public Map<String, String> getInitParameters() {
		return initParameters;
	}

	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters == null ? Collections.emptyMap() : initParameters;
	}

	@Override
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ConfigurableRegistration[");
		if (this.initParameters != null) {
			boolean first = true;
			for (Map.Entry<String, String> entry : this.initParameters.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(entry.getKey() + "=" + entry.getValue());
			}
		}
		sb.append(']');
		return sb.toString();
	}

}
