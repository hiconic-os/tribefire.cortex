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

import java.util.concurrent.Callable;

import org.junit.Ignore;

@Ignore
public class PermissionGiver implements Callable<Void> {

	private final long delay;
	
	public PermissionGiver(long delay) {
		this.delay = delay;
	}
	
	@Override
	public Void call() throws Exception {
		Thread.sleep(this.delay);
		System.out.println("Giving work permission NOW");
		TribefireWorkControlImpl.instance.giveWorkPermission();
		return null;
	}

}
