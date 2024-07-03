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
package tribefire.cortex.testing.junit.impl;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import com.braintribe.logging.Logger;

/**
 * @author peter.gazdik
 */
public class LoggingRunListener extends RunListener {

	private static final Logger log = Logger.getLogger(LoggingRunListener.class);

	ThreadLocal<Long> testStart = new ThreadLocal<>();

	@Override
	public void testStarted(Description description) throws Exception {
		testStart.set(System.nanoTime());
		log(description, "started");
	}

	@Override
	public void testFinished(Description description) throws Exception {
		long nano = System.nanoTime() - testStart.get();
		log(description, "finished in " + (nano / 1000_000) + " ms");
	}

	private void log(Description description, String msg) {
		log.info("[" + descriptor(description) + "] " + msg + ".    Thread: " + Thread.currentThread().getName());
	}

	private String descriptor(Description d) {
		return d.getTestClass().getSimpleName() + "->" + d.getMethodName();
	}

}
