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
package tribefire.platform.impl.thread;

import java.util.function.Supplier;

import com.braintribe.utils.collection.api.MinimalStack;

public class StandardThreadContextScopeSupplier <T> implements Supplier<StandardThreadContextScope<T>> {

	private Supplier<T> contextSupplier;
	private MinimalStack<T> contextConsumer;
	
	public void setContextSupplier(Supplier<T> contextSupplier) {
		this.contextSupplier = contextSupplier;
	}

	public void setContextConsumer(MinimalStack<T> contextConsumer) {
		this.contextConsumer = contextConsumer;
	}
	
	@Override
	public StandardThreadContextScope<T> get() {
		T context = contextSupplier.get();
		return new StandardThreadContextScope<T>(context, contextConsumer);
	}

}
