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
package tribefire.cortex.remotes.model;

import java.util.List;

import com.braintribe.model.descriptive.HasExternalId;
import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.domain.ServiceDomain;

/**
 * @author peter.gazdik
 */
public interface RemoteServiceDomain extends HasExternalId, HasName {

	EntityType<RemoteServiceDomain> T = EntityTypes.T(RemoteServiceDomain.class);

	/** externalId of the remote {@link ServiceDomain} */
	@Mandatory
	String getRemoteDomainId();
	void setRemoteDomainId(String remoteDomainId);

	/** Names of models to include in our domain. */
	@Mandatory
	List<String> getModels();
	void setModels(List<String> models);
}
