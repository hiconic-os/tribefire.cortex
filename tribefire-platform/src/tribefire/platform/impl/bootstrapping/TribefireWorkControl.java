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

/**
 * This is useful when threads in a cartridge need to know when they can actually start
 * their work. Sometimes, it is necessary to wait for the server startup before they should
 * start their work (e.g., because they need the tribefire-services up and running).
 * 
 * By invoking {@link #waitForWorkPermission()}, the caller is automatically blocked
 * until the worker should start its work.
 */
public interface TribefireWorkControl {

	public final static TribefireWorkControl instance = TribefireWorkControlImpl.instance;
	
	/**
	 * Blocks until the tribefire-services are available. When the worker should start its
	 * work, this method will return. If the services are already running, this method
	 * will return immediately.
	 * 
	 * @throws InterruptedException Thrown if there was an InterruptedException during waiting
	 * 	for the services.
	 */
	void waitForWorkPermission() throws InterruptedException;
	
}
