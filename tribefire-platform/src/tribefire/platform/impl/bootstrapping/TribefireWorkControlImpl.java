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
package tribefire.platform.impl.bootstrapping;

import java.util.HashSet;
import java.util.Set;

import com.braintribe.utils.LongIdGenerator;

public class TribefireWorkControlImpl implements TribefireWorkControl {
	
	public static TribefireWorkControlImpl instance = new TribefireWorkControlImpl();

	protected Set<String> monitors = new HashSet<String>();
	protected volatile boolean permissionGranted = false;

	@Override
	public void waitForWorkPermission() throws InterruptedException {

		if (this.permissionGranted) {
			return;
		}

		String monitorObject = new String(Thread.currentThread().getName())+LongIdGenerator.provideLongId();

		synchronized(monitorObject) {
			
			synchronized(this) {
				if (this.permissionGranted) {
					return;
				}
				monitors.add(monitorObject);
			}

			monitorObject.wait();
		}
	}

	public void giveWorkPermission() {
		this.permissionGranted = true;

		Set<String> monitorsToNotify = null; 

		synchronized(this) {
			monitorsToNotify = monitors;
			monitors = new HashSet<String>();
		}

		for (String monitorObject : monitorsToNotify) {
			synchronized (monitorObject) {				
				monitorObject.notify();
			}
		}

	}

}
