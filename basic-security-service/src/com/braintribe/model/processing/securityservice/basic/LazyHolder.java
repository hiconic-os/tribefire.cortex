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
package com.braintribe.model.processing.securityservice.basic;

import java.util.function.Supplier;

public class LazyHolder<T> implements Supplier<T> {
	private T value;
	private boolean initialized = false;
	private Supplier<T> supplier;

	public LazyHolder(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> LazyHolder<T> from(Supplier<T> supplier) {
		return new LazyHolder<>(supplier);
	}

	@Override
	public T get() {
		if (!initialized) {
			value = supplier.get();
			initialized = true;
		}

		return value;
	}

	public boolean isInitialized() {
		return initialized;
	}
}