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
package tribefire.cortex.manipulation.conversion.beans;

import com.braintribe.model.generic.value.PreliminaryEntityReference;

public class NewBean extends EntityBean<PreliminaryEntityReference> {

	public boolean isManaged = false;

	public NewBean(BeanRegistry beanRegistry, PreliminaryEntityReference ref) {
		super(beanRegistry, ref, null);
	}

	@Override
	protected String beanInstanceForSetterInvocation() {
		return "bean";
	}

	@Override
	public void writeYourDeclaration() {
		sb = new StringBuilder();

		String typeName = typeNameIn(ctx.spaceWriter);

		// @Manged
		if (isManaged)
			sb.append("\t@Managed\n");
		else
			sb.append("\t// Managed\n");

		// public MyType myType1() {
		sb.append("\tprivate ");
		sb.append(typeName);
		sb.append(" ");
		sb.append(beanName);
		sb.append("() {\n");

		// MyType bean = create(MyType.T, "globalId");
		sb.append("\t\t");
		sb.append(typeName);
		sb.append(" ");
		sb.append(beanInstanceForSetterInvocation());
		sb.append(" = session().createRaw(");
		sb.append(typeName);
		sb.append(".T, \"");
		sb.append(globalId);
		sb.append("\");\n");

		writePropertyChanges();

		// return bean;;
		sb.append("\t\treturn bean;\n");

		// } // (end of bean method)
		sb.append("\t}\n\n");

		ctx.spaceWriter.addMethod(sb.toString());
	}

}
