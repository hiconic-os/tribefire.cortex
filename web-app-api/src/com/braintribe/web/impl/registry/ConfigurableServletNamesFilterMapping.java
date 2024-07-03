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

import java.util.Arrays;
import java.util.List;

import com.braintribe.cfg.Required;
import com.braintribe.web.api.registry.ServletNamesFilterMapping;

public class ConfigurableServletNamesFilterMapping extends ConfigurableFilterMapping implements ServletNamesFilterMapping {

	@Required
	public void setNames(List<String> names) {
		super.keys = names;
	}

	@Override
	public String[] getNamesArray() {
		return super.keys.toArray(new String[super.keys.size()]);
	}

	@Override
	public String toString() {
		return "ServletName: " + super.toString();
	}

	/* builder methods */

	public ConfigurableServletNamesFilterMapping names(String... names) {
		setNames(Arrays.asList(names));
		return this;
	}

	/* // builder methods */

}
