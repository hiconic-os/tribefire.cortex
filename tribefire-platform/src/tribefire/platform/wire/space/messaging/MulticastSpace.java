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
package tribefire.platform.wire.space.messaging;

import java.util.concurrent.ExecutorService;

import com.braintribe.execution.virtual.VirtualThreadExecutor;
import com.braintribe.execution.virtual.VirtualThreadExecutorBuilder;
import com.braintribe.logging.Logger;
import com.braintribe.model.extensiondeployment.HardwiredWorker;
import com.braintribe.model.messaging.Topic;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.mqrpc.server.GmMqRpcServer;
import com.braintribe.model.processing.service.api.aspect.EndpointExposureAspect;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.multicast.MulticastProcessor;
import tribefire.platform.wire.space.common.CartridgeInformationSpace;
import tribefire.platform.wire.space.common.EnvironmentSpace;
import tribefire.platform.wire.space.common.MessagingSpace;
import tribefire.platform.wire.space.common.RuntimeSpace;
import tribefire.platform.wire.space.rpc.RpcSpace;
import tribefire.platform.wire.space.system.TopologySpace;

@Managed
public class MulticastSpace implements WireSpace {

	private static final Logger logger = Logger.getLogger(MulticastSpace.class);

	// @formatter:on
	@Import
	private CartridgeInformationSpace cartridgeInformation;
	@Import
	private EnvironmentSpace environment;
	@Import
	private MessagingSpace messaging;
	@Import
	private RpcSpace rpc;
	@Import
	private RuntimeSpace runtime;
	@Import
	private TopologySpace topology;
	// @formatter:off

	@Managed
	public MulticastProcessor processor() {
		MulticastProcessor bean = new MulticastProcessor();
		bean.setMessagingSessionProvider(messaging.sessionProvider());
		bean.setRequestTopicName(messaging.destinations().multicastRequestTopicName());
		bean.setResponseTopicName(messaging.destinations().multicastResponseTopicName());
		bean.setSenderId(cartridgeInformation.instanceId());
		bean.setLiveInstances(topology.liveInstances());
		bean.setMetaDataProvider(rpc.clientMetaDataProvider());
		return bean;
	}

	@Managed
	public GmMqRpcServer consumer() {
		GmMqRpcServer bean = new GmMqRpcServer();
		bean.setRequestEvaluator(rpc.serviceRequestEvaluator());
		bean.setMessagingSessionProvider(messaging.sessionProvider());
		bean.setRequestDestinationName(messaging.destinations().multicastRequestTopicName());
		bean.setRequestDestinationType(Topic.T);
		bean.setConsumerId(cartridgeInformation.instanceId());
		bean.setExecutor(threadPool());
		if (logger.isDebugEnabled()) {
			bean.setThreadRenamer(runtime.threadRenamer());
		}
		bean.setTrusted(false);
		bean.setKeepAliveInterval(environment.property(TribefireRuntime.ENVIRONMENT_MULTICAST_KEEP_ALIVE_INTERVAL, Long.class, 10000L));
		bean.setMetaDataResolverProvider(rpc.metaDataResolverProvider());
		bean.setEndpointExposure(EndpointExposureAspect.MULTICAST);
		return bean;
	}

	@Managed
	public HardwiredWorker consumerDeployable() {
		HardwiredWorker bean = HardwiredWorker.T.create();
		bean.setExternalId("multicast-consumer");
		bean.setName("Multicast Consumer");
		bean.setGlobalId("hardwired:worker/" + bean.getExternalId());
		return bean;
	}

	@Managed
	private ExecutorService threadPool() {
		VirtualThreadExecutor bean = VirtualThreadExecutorBuilder.newPool().concurrency(250).threadNamePrefix("tribefire.multicast-master-").description("Master Multicast Consumer").build();
		return bean;
	}
}
