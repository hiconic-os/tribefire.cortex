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
package tribefire.cortex.manipulation.conversion.code;

import com.braintribe.utils.StringTools;

import tribefire.cortex.sourcewriter.JavaSourceClass;
import tribefire.cortex.sourcewriter.JavaSourceWriter;

/**
 * @author peter.gazdik
 */
public class InitializerWritingContext {

	public final JscPool jscPool;
	public final JavaSourceWriter spaceWriter;
	public final JavaSourceWriter lookupWriter;

	public boolean newDateFunctionUsed = false;

	public final String lookupContractInstanceName;

	public InitializerWritingContext(JavaSourceWriter spaceWriter, JavaSourceWriter lookupWriter, JscPool jscPool) {
		this.jscPool = jscPool;
		this.spaceWriter = spaceWriter;
		this.lookupWriter = lookupWriter;
		this.lookupContractInstanceName = contractInstanceName(lookupWriter.sourceClass);
	}

	private static String contractInstanceName(JavaSourceClass contractClass) {
		String s = contractClass.simpleName;
		s = StringTools.removeSuffix(s, "Contract");
		s = StringTools.uncapitalize(s);
		return s;
	}

	/** Returns either the simple name or the fully-qualified name (if import failed) of this bean in given */
	public String typeNameInWriter(JavaSourceClass jsc, JavaSourceWriter writer) {
		boolean imported = writer.tryImport(jsc);
		return imported ? jsc.simpleName : jsc.fullName();
	}

}
