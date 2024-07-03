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

import com.braintribe.model.deployment.Deployable;

public abstract class AbstractExpertContext<D extends Deployable> implements ExpertContext<D> {

	@Override
	public int hashCode() {
		String externalId = getDeployableExternalId();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalId == null) ? 0 : externalId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExpertContext<?>))
			return false;

		ExpertContext<?> other = (ExpertContext<?>) obj;
		String externalId = getDeployableExternalId();
		String otherExternalId = other.getDeployableExternalId();
		if (externalId == null) {
			if (otherExternalId != null)
				return false;
		} else if (!externalId.equals(otherExternalId))
			return false;
		return true;
	}

}
