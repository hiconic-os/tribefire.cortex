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
package tribefire.cortex.testing.junit.formatter;

import java.util.function.Supplier;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * A simple way to enable thread safety when tests are executed in parallel. Works fine in combination with {@link JUnitResultFormatterAsRunListener}
 *
 * @author Neidhart.Orlich
 *
 */
public class ThreadLocalRunListener extends RunListener {

	private ThreadLocal<RunListener> delegateThreadLocal;

	public ThreadLocalRunListener(Supplier<RunListener> delegateFactory) {
		super();
		delegateThreadLocal = ThreadLocal.withInitial(delegateFactory);
	}

	private RunListener delegate() {
		return delegateThreadLocal.get();
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		delegate().testRunStarted(description);
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		delegate().testRunFinished(result);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		delegate().testStarted(description);
	}

	@Override
	public void testFinished(Description description) throws Exception {
		delegate().testFinished(description);
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		delegate().testFailure(failure);
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		delegate().testAssumptionFailure(failure);
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		delegate().testIgnored(description);
	}

	@Override
	public String toString() {
		return delegate().toString();
	}
}
