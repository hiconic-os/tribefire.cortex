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
package com.braintribe.model.processing.securityservice.basic.test.common;

public class TestConfig {
	
	private Boolean generatingUserSessionAccesses = true;
	private Boolean enableLongRunning = true;
	private Boolean enableExpiration = true;

	public Boolean getGeneratingUserSessionAccesses() {
		return generatingUserSessionAccesses;
	}
	
	public void setGeneratingUserSessionAccesses(Boolean generatingUserSessionAccesses) {
		this.generatingUserSessionAccesses = generatingUserSessionAccesses;
	}

	public Boolean getEnableLongRunning() {
		return enableLongRunning;
	}

	public void setEnableLongRunning(Boolean enableLongRunning) {
		this.enableLongRunning = enableLongRunning;
	}
	
	public Boolean getEnableExpiration() {
		return enableExpiration;
	}

	public void setEnableExpiration(Boolean enableExpiration) {
		this.enableExpiration = enableExpiration;
	}

}
