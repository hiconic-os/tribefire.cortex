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
package tribefire.platform.impl.worker.impl;

import com.braintribe.logging.Logger;

/**
 * This is a helper class to be used by the BasicWorkerManager to provide
 * a meaningful thread name and push the same information to the logging NDC.
 */
public class WorkerExecutionContext {

	protected static Logger logger = Logger.getLogger(WorkerExecutionContext.class);
	
	private String context;
	private String originalThreadName;
	private int maxLength = 100;
	
	public WorkerExecutionContext(String prefix, Object executable) {
		context = prefix != null ? (prefix + ">") : "";
		context += executable != null ? executable.toString() : "unknown";
		int idx = context.indexOf("$$Lambda$");
		if (idx > 0) {
			context = context.substring(0, idx);
		}
		if (context.length() > maxLength) {
			context = context.substring(0, maxLength);
		}
	}
	
	public void push() {
		originalThreadName = Thread.currentThread().getName();
		context = originalThreadName + ">" + context;
		logger.pushContext(context);
		Thread.currentThread().setName(context);
	}
	
	public void pop() {
		if (originalThreadName != null) {
			Thread.currentThread().setName(originalThreadName);
			logger.popContext();
		}
	}
	
	@Override
	public String toString() {
		return context;
	}
	
}
