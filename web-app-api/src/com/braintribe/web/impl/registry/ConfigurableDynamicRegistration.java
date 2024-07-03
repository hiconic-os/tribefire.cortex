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
package com.braintribe.web.impl.registry;

import com.braintribe.web.api.registry.DynamicRegistration;

public abstract class ConfigurableDynamicRegistration extends ConfigurableRegistration implements DynamicRegistration {

	protected boolean asyncSupported;

	@Override
	public boolean isAsyncSupported() {
		return asyncSupported;
	}
	
	public void setAsyncSupported(boolean asyncSupported) {
		this.asyncSupported = asyncSupported;
	}

	@Override
	public String toString() {
		return super.toString()+";asyncSupported="+this.asyncSupported;
	}
}
