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
package tribefire.platform.impl.deployment;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import com.braintribe.cfg.Required;

import tribefire.module.wire.contract.DeploymentContract;

/**
 * The name might change, it might later implement an interface (SystemDeployment?), currently it just serves the
 * purpose to support {@link DeploymentContract#runWhenSystemIsDeployed(Runnable)}
 * 
 * @author peter.gazdik
 */
public class SystemDeploymentListenerRegistry {

	private ExecutorService executor;
	private ReentrantLock lock = new ReentrantLock();

	private volatile List<Runnable> runnables = newList();

	@Required
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public void onSystemDeployed() {
		lock.lock();
		try {
			for (Runnable r : runnables)
				executor.submit(r);

			runnables = null;
		} finally {
			lock.unlock();
		}
	}

	public void runWhenSystemIsDeployed(Runnable r) {
		if (runnables != null)
			if (rememberToRunLater(r))
				return;

		executor.submit(r);
	}

	private boolean rememberToRunLater(Runnable r) {
		lock.lock();
		try {
			return runnables != null && runnables.add(r);
		} finally {
			lock.unlock();
		}
	}

}
