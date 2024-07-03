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
package tribefire.cortex.initializer.support.impl;

import com.braintribe.wire.api.scope.InstanceQualification;

public class GlobalIdGenerator {
	private int seq = 0;
	private StringBuilder globalIdBuilder = new StringBuilder();
	
	public void initialize(InstanceQualification qualification, String globalIdPrefix) {
		// reset id suffix sequence
		seq = 0;
		
		// reset globalId prefixing
		globalIdBuilder.setLength(0);
		globalIdBuilder
			.append("wire://")
			.append(globalIdPrefix)
			.append('/')
			.append(qualification.space().getClass().getSimpleName())
			.append('/')
			.append(qualification.name())
			.append('/');
	}
	
	public String nextGlobalId() {
		int trimBackTo = globalIdBuilder.length();
		globalIdBuilder.append(seq++);
		String globalIdResult = globalIdBuilder.toString();
		globalIdBuilder.setLength(trimBackTo);
		return globalIdResult;
	}
}
