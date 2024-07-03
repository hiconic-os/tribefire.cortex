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
package tribefire.platform.wire.space.cortex.services;

import java.util.concurrent.ExecutorService;

import com.braintribe.common.concurrent.TaskScheduler;
import com.braintribe.common.concurrent.TaskSchedulerImpl;
import com.braintribe.execution.ExtendedScheduledThreadPoolExecutor;
import com.braintribe.execution.virtual.CountingVirtualThreadFactory;
import com.braintribe.execution.virtual.VirtualThreadExecutor;
import com.braintribe.execution.virtual.VirtualThreadExecutorBuilder;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.WorkerContract;
import tribefire.platform.impl.worker.impl.BasicWorkerManager;
import tribefire.platform.wire.space.security.AuthContextSpace;

@Managed
public class WorkerSpace implements WorkerContract {

	private static final Logger logger = Logger.getLogger(WorkerSpace.class);

	@Import
	private AuthContextSpace authContext;

	@Import
	private ClusterSpace cluster;

	@Override
	@Managed
	public BasicWorkerManager manager() {
		BasicWorkerManager bean = new BasicWorkerManager();
		bean.setLeadershipManagerSuppier(cluster::leadershipManager);
		bean.setSystemUserSessionProvider(authContext.internalUser().userSessionProvider());
		bean.setUserSessionScoping(authContext.masterUser().userSessionScoping());
		bean.setExecutorService(threadPool());
		return bean;
	}

	@Override
	@Managed
	public ExecutorService threadPool() {

		int threadPoolSize = 250;
		String threadPoolSizeString = TribefireRuntime.getProperty("TRIBEFIRE_PLATFORM_THREAD_POOL_SIZE");
		if (!StringTools.isBlank(threadPoolSizeString)) {
			try {
				threadPoolSize = Integer.parseInt(threadPoolSizeString);
			} catch (NumberFormatException nfe) {
				logger.error("Could not parse the value " + threadPoolSizeString + " defined in variable TRIBEFIRE_PLATFORM_THREAD_POOL_SIZE", nfe);
			}
		}

		VirtualThreadExecutor bean = VirtualThreadExecutorBuilder.newPool().concurrency(threadPoolSize).threadNamePrefix("tf.platform-")
				.description("Platform Thread-Pool").build();
		return bean;
	}

	@Override
	@Managed
	public ExtendedScheduledThreadPoolExecutor scheduledThreadPool() {
		ExtendedScheduledThreadPoolExecutor bean = new ExtendedScheduledThreadPoolExecutor( //
				5, //
				new CountingVirtualThreadFactory("tf.scheduled-") //
		);
		bean.setAddThreadContextToNdc(true);
		bean.allowCoreThreadTimeOut(true);
		bean.setDescription("Platform Scheduled Thread-Pool");

		return bean;
	}

	@Override
	@Managed
	public TaskScheduler taskScheduler() {
		TaskSchedulerImpl bean = new TaskSchedulerImpl();
		bean.setName("Platform-Task-Scheduler");
		bean.setExecutor(scheduledThreadPool());

		return bean;
	}

}
