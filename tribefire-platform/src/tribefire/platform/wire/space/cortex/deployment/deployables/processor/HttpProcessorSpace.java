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
package tribefire.platform.wire.space.cortex.deployment.deployables.processor;

import static java.util.Objects.requireNonNull;

import com.braintribe.model.deployment.HttpServer;
import com.braintribe.model.deployment.remote.GmWebRpcRemoteServiceProcessor;
import com.braintribe.model.deployment.remote.RemotifyingInterceptor;
import com.braintribe.model.processing.deployment.api.ExpertContext;
import com.braintribe.model.processing.service.api.ServiceAroundProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.webrpc.client.UserSessionResolver;
import com.braintribe.model.securityservice.credentials.Credentials;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.wire.space.common.ResourceProcessingSpace;
import tribefire.platform.wire.space.cortex.deployment.deployables.DeployableBaseSpace;
import tribefire.platform.wire.space.module.SecuritySpace;

@Managed
public class HttpProcessorSpace extends DeployableBaseSpace {

	@Import
	private ResourceProcessingSpace resourceProcessing;

	@Import
	private SecuritySpace security;

	@Managed
	public ServiceProcessor<ServiceRequest, Object> gmWebRpcRemoteServiceProcessor(ExpertContext<GmWebRpcRemoteServiceProcessor> context) {
		GmWebRpcRemoteServiceProcessor deployable = context.getDeployable();
		HttpServer server = requireNonNull(deployable.getServer(), "'server' cannot be null");
		String url = requireNonNull(server.getBaseUrl(), "'server.baseUrl' cannot be null");
		String uri = requireNonNull(deployable.getUri(), "'uri' property cannot be null");

		String actualUrl = addUrlParts(url, uri);

		com.braintribe.model.processing.webrpc.client.GmWebRpcRemoteServiceProcessor bean = new com.braintribe.model.processing.webrpc.client.GmWebRpcRemoteServiceProcessor();
		bean.setUrl(actualUrl);
		bean.setStreamPipeFactory(resourceProcessing.streamPipeFactory());

		return bean;
	}

	private String addUrlParts(String a, String b) {
		boolean aSlash = a.endsWith("/");
		boolean bSlash = a.startsWith("/");

		if (aSlash != bSlash)
			return a + b;

		if (aSlash)
			return a + b.substring(1);
		else
			return a + "/" + b;
	}

	@Managed
	public ServiceAroundProcessor<ServiceRequest, Object> remotifyingInterceptor(ExpertContext<RemotifyingInterceptor> context) {
		RemotifyingInterceptor deployable = context.getDeployable();

		Credentials credentials = deployable.getCredentials();
		boolean keepLocalSessionId = false;

		validateRemotifyingInterceptor(credentials, keepLocalSessionId);

		com.braintribe.model.processing.webrpc.client.RemotifyingInterceptor bean = new com.braintribe.model.processing.webrpc.client.RemotifyingInterceptor();
		bean.setModelAccessoryFactory(gmSessions.systemModelAccessoryFactory());

		if (credentials != null)
			bean.setUserSessionResolver(userSessionResolver(credentials));

		return bean;
	}

	@Managed
	public UserSessionResolver userSessionResolver(Credentials credentials) {
		UserSessionResolver bean = new UserSessionResolver();
		bean.setCredentials(credentials);
		bean.setDecryptingTransformer(security.decryptingTransformer());

		return bean;
	}

	private void validateRemotifyingInterceptor(Credentials credentials, boolean keepLocalSessionId) {
		if (keepLocalSessionId && credentials != null)
			throw new IllegalStateException("Cannot deploy RemotifyingInterceptor. 'keepLocalSessionId' is true and `credentials` is not null.");

		if (!keepLocalSessionId && credentials == null)
			throw new IllegalStateException(
					"Cannot deploy RemotifyingInterceptor. 'keepLocalSessionId' has to be true, or `credentials` must not be null.");
	}

}
