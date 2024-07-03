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

import com.braintribe.model.deployment.HttpServer;
import com.braintribe.model.deployment.remote.GmWebRpcRemoteServiceProcessor;
import com.braintribe.model.deployment.remote.RemotifyingInterceptor;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.securityservice.credentials.Credentials;

/**
 * Based on this entity a new {@link GmWebRpcRemoteServiceProcessor} and {@link RemotifyingInterceptor} is configured, and for each
 * {@link RemoteServiceDomain} (contained in the {@link #getDomains() domains} property) a single domain is configured, where each request is
 * configured to be processed with this service processor and i.
 * 
 * @author peter.gazdik
 */
public interface Remotes extends GenericEntity {

	EntityType<Remotes> T = EntityTypes.T(Remotes.class);

	/**
	 * Remote server URL. This value is set as the {@link HttpServer#getBaseUrl() base URL} of the {@link GmWebRpcRemoteServiceProcessor}'s
	 * {@link GmWebRpcRemoteServiceProcessor#getServer() HTTP server}.
	 */
	@Mandatory
	String getServerUrl();
	void setServerUrl(String serverUrl);

	/**
	 * Optional remote server URI, which is appended to the {@link #getServerUrl() server's URL} to get the actual URL to call the remote service.
	 * Typically this value would be 'rpc' for a remote tribefire server.
	 * <p>
	 * This is set as {@link GmWebRpcRemoteServiceProcessor#getUri() uri} property of our remote service processor.
	 */
	@Mandatory
	String getServerUri();
	void setServerUri(String serverUri);

	// whether or not credentials are
	// TODO explain
	boolean getDecryptCredentials();
	void setDecryptCredentials(boolean decryptCredentials);

	/** Credentials for the remote server. If no credentials are configured, we assume the remote server shares sessions with ours. */
	// relevant globalId better be set
	// the credentials are expected not to contain the sensitive values like passwords...
	Credentials getServerCredentials();
	void setServerCredentials(Credentials serverCredentials);

	List<RemoteServiceDomain> getDomains();
	void setDomains(List<RemoteServiceDomain> domains);

}
