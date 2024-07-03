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
package com.braintribe.model.deployment.remote;

import com.braintribe.model.extensiondeployment.ServiceAroundProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.securityservice.credentials.Credentials;

/**
 * Denotation type for a {@link ServiceAroundProcessor} which handles the authentication against the remote server (when relevant) and sets the
 * correct remote domain id based on the configured {@link RemoteDomainIdMapping}.
 * <p>
 * The remote authentication is only done in case {@link #getCredentials() credentials} are set, otherwise we assume the remote server shares the
 * session with the local one (i.e. the one where this interceptor is running).
 * 
 * @author peter.gazdik
 */
public interface RemotifyingInterceptor extends ServiceAroundProcessor {

	EntityType<RemotifyingInterceptor> T = EntityTypes.T(RemotifyingInterceptor.class);

	boolean getDecryptCredentials();
	void setDecryptCredentials(boolean decryptCredentials);

	Credentials getCredentials();
	void setCredentials(Credentials credentials);

}
