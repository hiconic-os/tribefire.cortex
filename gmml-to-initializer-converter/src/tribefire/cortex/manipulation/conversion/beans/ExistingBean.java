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

import com.braintribe.model.generic.value.GlobalEntityReference;

public class ExistingBean extends EntityBean<GlobalEntityReference> {

	public ExistingBean(BeanRegistry beanRegistry, GlobalEntityReference ref) {
		super(beanRegistry, ref, (String) ref.getRefId());
	}

	@Override
	public void writeYourDeclaration() {
		StringBuilder sb = new StringBuilder();

		String typeName = typeNameIn(ctx.lookupWriter);

		sb.append("\t@GlobalId(\"");
		sb.append(ref.getRefId());
		sb.append("\")\n");
		sb.append("\t");
		sb.append(typeName);
		sb.append(" ");
		sb.append(beanName);
		sb.append("();\n\n");

		ctx.lookupWriter.addMethod(sb.toString());
	}

	@Override
	protected String beanInstanceForSetterInvocation() {
		return ctx.lookupContractInstanceName + "." + beanName + "()";
	}

	public void writeYourInitialization(StringBuilder strinBuilder) {
		sb = strinBuilder;

		writePropertyChanges();
	}

}
