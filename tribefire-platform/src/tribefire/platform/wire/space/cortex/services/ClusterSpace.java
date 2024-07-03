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

import java.util.concurrent.TimeUnit;

import com.braintribe.common.concurrent.ScheduledTask;
import com.braintribe.common.concurrent.TaskScheduler;
import com.braintribe.exception.Exceptions;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.processing.deployment.api.DcProxyListener;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.model.processing.lock.api.Locking;
import com.braintribe.model.processing.lock.impl.SimpleCdlLocking;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.leadership.api.LeadershipManager;
import tribefire.cortex.leadership.impl.LockingBasedLeadershipManager;
import tribefire.module.wire.contract.ClusterContract;
import tribefire.platform.wire.space.SchrodingerBeansSpace;
import tribefire.platform.wire.space.common.BindersSpace;

@Managed
public class ClusterSpace implements ClusterContract {

	public static final String DEFAULT_LOCKING_EXTERNAL_ID = "default.Locking";

	private static int LEADERSHIP_REFRESH_INTERVAL_MS = 10_000;

	@Import
	private BindersSpace binders;

	@Import
	private SchrodingerBeansSpace schrodingerBeans;

	@Import
	private WorkerSpace worker;

	@Override
	@Managed
	public Locking locking() {
		return lockingSchrodingerBean().proxy();
	}

	@Override
	@Managed
	public LeadershipManager leadershipManager() {
		LockingBasedLeadershipManager bean = new LockingBasedLeadershipManager();
		bean.setLocking(locking());
		bean.setName("Default Platform Leadership Manager");

		configureLeadershipRefreshOnceLockingAvailable(bean);

		return bean;
	}

	private void configureLeadershipRefreshOnceLockingAvailable(LockingBasedLeadershipManager bean) {
		LeadershipRefresher refresher = new LeadershipRefresher(bean, worker.taskScheduler());

		lockingSchrodingerBean() //
				.proxyDelegation() //
				.addDcProxyListener(refresher);
	}

	static class LeadershipRefresher implements DcProxyListener {
		private final LockingBasedLeadershipManager leadershipManager;
		private final TaskScheduler taskScheduler;

		private ScheduledTask task;

		public LeadershipRefresher(LockingBasedLeadershipManager leadershipManager, TaskScheduler taskScheduler) {
			this.leadershipManager = leadershipManager;
			this.taskScheduler = taskScheduler;
		}

		@Override
		public void onDefaultDelegateSet(Object defaultDelegate) {
			onDelegateSet(defaultDelegate);
		}

		@Override
		public void onDelegateSet(Object delegate) {
			task = taskScheduler.scheduleAtFixedRate( //
					"leadership-refresher", //
					() -> leadershipManager.refreshLeadershipsForEligibleDomains(), //
					LEADERSHIP_REFRESH_INTERVAL_MS, LEADERSHIP_REFRESH_INTERVAL_MS, TimeUnit.MILLISECONDS) //
					.done();
		}

		@Override
		public void onDelegateCleared(Object delegate) {
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
	}

	@Managed
	public SchrodingerBean<Locking> lockingSchrodingerBean() {
		return schrodingerBeans.newBean("Locking", CortexConfiguration::getLocking, binders.locking());
	}

	@Managed
	public Locking defaultLocking() {
		try {
			return new SimpleCdlLocking();

		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Failed to create LockManager.");
		}
	}

}