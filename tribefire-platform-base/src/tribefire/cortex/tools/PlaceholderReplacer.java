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
package tribefire.cortex.tools;

import java.util.function.Function;

import com.braintribe.logging.Logger;
import com.braintribe.utils.template.Template;
import com.braintribe.utils.template.model.MergeContext;

public class PlaceholderReplacer {

	private static final Logger log = Logger.getLogger(PlaceholderReplacer.class);

	public static String resolve(String value, Function<String, String> resolver) {
		String resolved = resolve(value, mergeContext(resolver));
		return resolved;
	}

	protected static String resolve(String value, MergeContext mergeContext) {
		try {
			Template template = Template.parse(value);
			String result = template.merge(mergeContext);
			return result;
		} catch (Exception e) {
			log.error("Failed to resolve [ " + value + " ]", e);
		}
		return null;
	}

	protected static MergeContext mergeContext(Function<String, String> delegateFunction) {
		final MergeContext mergeContext = new MergeContext();
		mergeContext.setVariableProvider(new Function<String, String>() {
			@Override
			public String apply(String index) {

				String value = delegateFunction.apply(index);

				if (value == null) {
					return "";
				}

				if (value.isEmpty()) {
					return value;
				}

				return resolve(value, mergeContext);

			}

		});
		return mergeContext;
	}

}
