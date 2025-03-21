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
package tribefire.cortex.testing.junit.impl;

import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import com.braintribe.utils.lcd.StringTools;

/**
 * @author peter.gazdik
 */
public abstract class TestFilter extends Filter {

	@Override
	public boolean shouldRun(Description description) {
		if (description.isTest())
			return shouldRunTest(description);
		else
			return description.getChildren().stream().filter(this::shouldRun).findAny().isPresent();
	}

	protected abstract boolean shouldRunTest(Description description);

	@Override
	public String describe() {
		return getClass().getSimpleName();
	}

	// ####################################################
	// ## . . . . . . . . Actual Filters . . . . . . . . ##
	// ####################################################

	public static class ClassNameFilter extends TestFilter {
		private final Set<String> classNames;

		public ClassNameFilter(Set<String> classNames) {
			this.classNames = classNames;
		}

		@Override
		public boolean shouldRunTest(Description description) {
			String testClassName = description.getClassName();

			return classNames.contains(testClassName) || classNames.contains(toSimpleName(testClassName));
		}

		private static String toSimpleName(String className) {
			return StringTools.findSuffix(className, ".");
		}
	}

}
