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
package tribefire.platform.wire.space.module;

import java.util.function.Supplier;

import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.wire.contract.UserRelated;

/**
 * @author peter.gazdik
 */
@Managed
public abstract class UserRelatedSpace implements UserRelated, WireSpace {

	@Override
	@Managed
	public Supplier<PersistenceGmSession> cortexSessionSupplier() {
		return () -> sessionFactory().newSession("cortex");
	}

	@Override
	@Managed
	public Supplier<ModelAccessory> cortexModelAccessorySupplier() {
		return () -> modelAccessoryFactory().getForAccess("cortex");
	}
}
