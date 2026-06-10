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
package tribefire.platform.wire.space.system;

import java.util.Set;

import com.braintribe.gm.logging.level.LogLevelApplicationResolver;
import com.braintribe.gm.logging.level.LogLevelServiceProcessor;
import com.braintribe.gm.logging.level.MulticastingLogLevelUpdateDispatcher;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.logging.level.LogLevelManager;
import com.braintribe.logging.level.LogLevelRuntimeUpdater;
import com.braintribe.logging.level.LogLevelSetup;
import com.braintribe.logging.level.servlet.JavaxLogLevelServlet;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.module.SystemUserRelatedSpace;
import tribefire.platform.wire.space.module.WebPlatformReflectionSpace;

@Managed
public class LogLevelSpace implements WireSpace {
	// @formatter:off
	@Import	private SystemUserRelatedSpace systemUserRelated;
	@Import private WebPlatformReflectionSpace platformReflection;
	@Import private TopologySpace topology;
	
	// @formatter:on

	@Managed
	public JavaxLogLevelServlet logLevelServlet() {
		JavaxLogLevelServlet bean = new JavaxLogLevelServlet();

		bean.setEvaluator(systemUserRelated.evaluator());
		bean.setLocalInstanceId(platformReflection.instanceId());
		bean.setApplicationResolver(logLevelApplicationResolver());
		return bean;
	}
	
	@Managed
	public LogLevelServiceProcessor logLevelProcessor() {
		LogLevelServiceProcessor bean = new LogLevelServiceProcessor();
		bean.setLogLevelManager(logLevelManager());
		bean.setLogLevelRuntimeUpdater(logLevelRuntimeUpdater());
		bean.setEvaluator(systemUserRelated.evaluator());
		bean.setLocalInstanceId(platformReflection.instanceId());
		bean.setApplicationResolver(logLevelApplicationResolver());
		return bean;
	}
	
	@Managed
	private MulticastingLogLevelUpdateDispatcher logLevelUpdateDispatcher() {
		MulticastingLogLevelUpdateDispatcher bean = new MulticastingLogLevelUpdateDispatcher();
		bean.setEvaluator(systemUserRelated.evaluator());
		bean.setLocalInstanceId(platformReflection.instanceId());
		bean.setLogLevelManager(logLevelSetup().logLevelManager());
		return bean;
	}
	
	@Managed
	private LogLevelManager logLevelManager() {
		return logLevelSetup().logLevelManager();
	}

	@Managed
	private LogLevelRuntimeUpdater logLevelRuntimeUpdater() {
		LogLevelRuntimeUpdater bean = new LogLevelRuntimeUpdater();

		bean.setLogLevelManager(logLevelManager());
		bean.setUpdateDispatcher(logLevelUpdateDispatcher());
		return bean;
	}

	@Managed
	private LogLevelApplicationResolver logLevelApplicationResolver() {
		return new LogLevelApplicationResolver() {
			@Override
			public Set<String> liveApplications() {
				return topology.liveInstances().liveApplications();
			}

			@Override
			public Maybe<InstanceId> resolveApplication(String applicationId) {
				InstanceId localInstanceId = platformReflection.instanceId();
				if (applicationId == null || applicationId.equals(localInstanceId.getApplicationId())) {
					return Maybe.complete(localInstanceId);
				}

				Set<String> instances = topology.liveInstances().liveInstances(InstanceId.of(null, applicationId));
				if (instances == null || instances.isEmpty()) {
					return Maybe.empty(InvalidArgument.create("No live instance found for application: " + applicationId));
				}

				return Maybe.complete(InstanceId.parse(instances.iterator().next()));
			}
		};
	}

	private LogLevelSetup logLevelSetup() {
		return LogLevelSetup.instance();
	}
}
