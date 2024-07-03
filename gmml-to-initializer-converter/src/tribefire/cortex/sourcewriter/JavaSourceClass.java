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
package tribefire.cortex.sourcewriter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author peter.gazdik
 */
public class JavaSourceClass {

	public static final JavaSourceClass StringJsc = JavaSourceClass.create(String.class);
	public static final JavaSourceClass IntegerJsc = JavaSourceClass.create(Integer.class);
	public static final JavaSourceClass LongJsc = JavaSourceClass.create(Long.class);
	public static final JavaSourceClass FloatJsc = JavaSourceClass.create(Float.class);
	public static final JavaSourceClass DoubleJsc = JavaSourceClass.create(Double.class);
	public static final JavaSourceClass BooleanJsc = JavaSourceClass.create(Boolean.class);
	public static final JavaSourceClass DateJsc = JavaSourceClass.create(Date.class);
	public static final JavaSourceClass BigDecimalJsc = JavaSourceClass.create(BigDecimal.class);

	public final String packageName;
	public final String simpleName;
	public final boolean isInterface;
	public final boolean isAnnotation;

	public static JavaSourceClass create(Class<?> clazz) {
		return new JavaSourceClass(clazz.getPackage().getName(), clazz.getSimpleName(), clazz.isInterface(), clazz.isAnnotation());
	}

	public static JavaSourceClassBuilder build(String fullName) {
		int i = fullName.lastIndexOf('.');
		String packageName = fullName.substring(0, i);
		String shortName = fullName.substring(i + 1);
		return build(packageName, shortName);
	}

	public static JavaSourceClassBuilder build(String packageName, String shortName) {
		return new JavaSourceClassBuilder(packageName, shortName);
	}

	public static class JavaSourceClassBuilder {
		private final String packageName;
		private final String shortName;
		private boolean isInterface;
		private boolean isAnnotation;

		public JavaSourceClassBuilder(String packageName, String shortName) {
			this.packageName = packageName;
			this.shortName = shortName;
		}

		public JavaSourceClassBuilder isInterface(boolean isInterface) {
			this.isInterface = isInterface;
			return this;
		}

		public JavaSourceClassBuilder isAnnotation(boolean isAnnotation) {
			this.isAnnotation = isAnnotation;
			return this;
		}

		public JavaSourceClass please() {
			return new JavaSourceClass(packageName, shortName, isInterface, isAnnotation);
		}
	}

	private JavaSourceClass(String packageName, String shortName, boolean isInterface, boolean isAnnotation) {
		this.packageName = packageName;
		this.simpleName = shortName;
		this.isInterface = isInterface;
		this.isAnnotation = isAnnotation;
	}

	public boolean requiresImport() {
		return !isJavaLang();
	}

	public boolean isJavaLang() {
		return "java.lang".equals(packageName);
	}

	public String fullName() {
		return packageName + "." + simpleName;
	}

}
