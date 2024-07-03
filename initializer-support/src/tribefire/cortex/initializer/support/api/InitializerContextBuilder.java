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
package tribefire.cortex.initializer.support.api;

import java.util.function.Supplier;

import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.module.WireTerminalModule;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.impl.InitializerContextImpl;
import tribefire.cortex.initializer.support.wire.InitializerSupportWireModule;

public interface InitializerContextBuilder {

	/**
	 * Method for building {@link InitializerContext} that includes building of wire context which wires the initializer spaces.
	 */
	static <S extends WireSpace> WiredInitializerContext<S> build(WireTerminalModule<S> initializerModule, String initializerId,
			PersistenceInitializationContext context, WireContext<?> parentContext) {

		return build(initializerModule, () -> initializerId, context, parentContext);
	}

	/**
	 * Method for building {@link InitializerContext} that includes building of wire context which wires the initializer spaces.
	 */
	static <S extends WireSpace> WiredInitializerContext<S> build(WireTerminalModule<S> initializerModule, Supplier<String> initializerIdSupplier,
			PersistenceInitializationContext context, WireContext<?> parentContext) {

		InitializerSupportWireModule<S> iswm = new InitializerSupportWireModule<>(initializerModule, initializerIdSupplier, context, parentContext);
		WireContext<S> wireContext = Wire.context(iswm);

		return new InitializerContextImpl<S>(context.getSession(), wireContext);
	}

}
