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
package com.braintribe.model.processing.deployment.api;

import com.braintribe.common.lcd.AbstractUncheckedBtException;

/**
 * Signals an exception during the deployment process.
 * 
 * @author michael.lafite
 */
public class DeploymentException extends AbstractUncheckedBtException {

	private static final long serialVersionUID = 8903092162828215650L;

	public DeploymentException(final String message) {
		super(message);
	}

	public DeploymentException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
