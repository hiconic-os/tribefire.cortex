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
package tribefire.platform.impl.deployment.proxy;

/**
 * @author peter.gazdik
 */
public class DelegatedClass {

	public String foobar() {
		return "String";
	}

	@Override
	public String toString() {
		return "Delegated String";
	}

	/* The getFinalFoobar cannot be proxied, and thus changing the value via delegate.setFinalFoobar has no effect on proxy.getFinalFoobar, as that
	 * method won't e delegated */

	private String finalFoobar;

	public void setFinalFoobar(String finalFoobar) {
		this.finalFoobar = finalFoobar;
	}

	public final String getFinalFoobar() {
		return finalFoobar;
	}

}
