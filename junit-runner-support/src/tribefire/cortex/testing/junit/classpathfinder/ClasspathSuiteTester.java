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
package tribefire.cortex.testing.junit.classpathfinder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * ClassTester implementation to retrieve JUnit38 & 4.x test classes in the classpath. You can specify if you want to
 * include jar files in the search and you can give a set of regex expression to specify the class names to include.
 *
 */
public class ClasspathSuiteTester {

	private final List<JavaStyleClassnameMatcher> positiveFilters;
	private final List<JavaStyleClassnameMatcher> negationFilters;
	private final Class<?>[] baseTypes;
	private final Class<?>[] excludedBaseTypes;

	/**
	 * @param filterPatterns
	 *            A set of regex expression to specify the class names to include (included if any pattern matches); use
	 *            null to include all test classes in all packages.
	 */
	public ClasspathSuiteTester(String[] filterPatterns, Class<?>[] baseTypes, Class<?>[] excludedBaseTypes) {
		this.positiveFilters = findPositiveFilters(filterPatterns);
		this.negationFilters = findNegationFilters(filterPatterns);
		this.baseTypes = baseTypes;
		this.excludedBaseTypes = excludedBaseTypes;
	}

	public boolean acceptClass(Class<?> clazz) {
		return acceptTestClass(clazz);
	}

	private boolean acceptTestClass(Class<?> clazz) {
		if (isAbstractClass(clazz)) {
			return false;
		}
		if (hasExcludedBaseType(clazz)) {
			return false;
		}
		if (!hasCorrectBaseType(clazz)) {
			return false;
		}
		for (Method method : clazz.getMethods()) {
			if (method.getAnnotation(Test.class) != null) {
				return true;
			}
		}
		return false;
	}

	private boolean hasExcludedBaseType(Class<?> clazz) {
		for (Class<?> excludedBaseType : excludedBaseTypes) {
			if (excludedBaseType.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasCorrectBaseType(Class<?> clazz) {
		for (Class<?> baseType : baseTypes) {
			if (baseType.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAbstractClass(Class<?> clazz) {
		return (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
	}

	public boolean acceptClassName(String className) {
		return acceptInPositiveFilers(className) && acceptInNegationFilters(className);
	}

	private boolean acceptInNegationFilters(String className) {
		for (JavaStyleClassnameMatcher pattern : negationFilters) {
			if (pattern.matches(className)) {
				return false;
			}
		}

		return true;
	}

	private boolean acceptInPositiveFilers(String className) {
		if (positiveFilters.isEmpty()) {
			return true;
		}

		for (JavaStyleClassnameMatcher pattern : positiveFilters) {
			if (pattern.matches(className)) {
				return true;
			}
		}

		return false;
	}

	private List<JavaStyleClassnameMatcher> findPositiveFilters(String[] filterPatterns) {
		List<JavaStyleClassnameMatcher> filters = new ArrayList<JavaStyleClassnameMatcher>();
		if (filterPatterns != null) {
			for (String pattern : filterPatterns) {
				if (!pattern.startsWith("!")) {
					filters.add(new JavaStyleClassnameMatcher(pattern));
				}
			}
		}
		return filters;
	}

	private List<JavaStyleClassnameMatcher> findNegationFilters(String[] filterPatterns) {
		List<JavaStyleClassnameMatcher> filters = new ArrayList<JavaStyleClassnameMatcher>();
		for (String pattern : filterPatterns) {
			if (pattern.startsWith("!")) {
				filters.add(new JavaStyleClassnameMatcher(pattern.substring(1)));
			}
		}
		return filters;
	}

	public boolean acceptInnerClass() {
		return true;
	}

	public List<JavaStyleClassnameMatcher> getPositiveClassnameFilters() {
		return positiveFilters;
	}

	public List<JavaStyleClassnameMatcher> getNegationClassnameFilters() {
		return negationFilters;
	}

	public Class<?>[] getBaseTypes() {
		return baseTypes;
	}

	public Class<?>[] getExcludedBaseTypes() {
		return excludedBaseTypes;
	}
}