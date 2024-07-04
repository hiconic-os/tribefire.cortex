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
package tribefire.extension.js.model.deployment;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

public enum KnownViewPositionUseCase implements EnumBase {
	
	gmeViewTop("gmeViewTop"),
	gmeViewRight("gmeViewRight"),
	gmeViewLeft("gmeViewLeft"),
	gmeViewCenter("gmeViewCenter"),
	gmeViewBottom("gmeViewBottom");
	
	public static final EnumType<KnownViewPositionUseCase> T = EnumTypes.T(KnownViewPositionUseCase.class);
	
	@Override
	public EnumType<KnownViewPositionUseCase> type() {
		return T;
	}
	
	private String defaultValue;

	KnownViewPositionUseCase(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

}
