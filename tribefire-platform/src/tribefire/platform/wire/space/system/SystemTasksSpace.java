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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.braintribe.execution.ExtendedScheduledThreadPoolExecutor;
import com.braintribe.execution.virtual.CountingVirtualThreadFactory;
import com.braintribe.utils.system.SystemTools;
import com.braintribe.utils.system.exec.CommandExecutionImpl;
import com.braintribe.utils.system.exec.ProcessTerminatorImpl;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.SystemToolsContract;
import tribefire.platform.wire.space.cortex.services.WorkerSpace;

@Managed
public class SystemTasksSpace implements SystemToolsContract {

	@Import
	private SystemInformationSpace systemInformation;

	@Import
	private WorkerSpace worker;

	public void startTasks() {
		worker.taskScheduler() //
				.scheduleAtFixedRate("Process-Terminator", processTerminator(), 0, 10, TimeUnit.SECONDS) //
				.interruptOnCancel(false) //
				.done();
	}

	@Managed
	public ScheduledExecutorService scheduledExecutor() {
		// @formatter:off
		ExtendedScheduledThreadPoolExecutor bean = 
				new ExtendedScheduledThreadPoolExecutor(
						10, 				// corePoolSize
						threadFactory() 	// threadFactory
				);
		bean.setDescription("Scheduled System Executor");
		return bean;
		// @formatter:on
	}

	@Managed
	private ThreadFactory threadFactory() {
		return new CountingVirtualThreadFactory(executorId() + "-");
	}

	private String executorId() {
		return "tribefire.system.executor";
	}

	@Override
	@Managed
	public SystemTools systemTools() {
		SystemTools bean = new SystemTools();
		bean.setCommandExecution(commandExecution());
		return bean;
	}

	@Override
	@Managed
	public CommandExecutionImpl commandExecution() {
		CommandExecutionImpl bean = new CommandExecutionImpl();
		bean.setProcessTerminator(processTerminator());
		return bean;
	}

	@Override
	@Managed
	public ProcessTerminatorImpl processTerminator() {
		ProcessTerminatorImpl bean = new ProcessTerminatorImpl();
		return bean;
	}

}
