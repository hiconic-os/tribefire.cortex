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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.braintribe.cfg.LifecycleAware;

/**
 * @author christina.wilpernig
 */
public class TestWorkerExpert implements Callable<Void>, LifecycleAware {

	private Supplier<String> generator;
	private Future<?> future;
	private BlockingQueue<String> testQueue;

	public void setGenerator(Supplier<String> generator) {
		this.generator = generator;
	}

	@Override
	public void postConstruct() {
		future = Executors.newSingleThreadExecutor().submit(this);
	}

	@Override
	public void preDestroy() {
		future.cancel(true);
	}

	@Override
	public Void call() throws Exception {

		do {
			
			String generatorOutput = null;
			
			try {
				generatorOutput = generator.get();
			} catch (Exception e) {
				// noop
			}

			testQueue.offer((generatorOutput != null) ? generatorOutput : "test-failed");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return null;
			}

		} while (true);

	}

	public void setTestQueue(BlockingQueue<String> testQueue) {
		this.testQueue = testQueue;
		
	}

}
