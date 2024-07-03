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

import java.util.function.Supplier;

import com.braintribe.model.deployment.HardwiredDeployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.utils.StringTools;
import com.braintribe.wire.api.scope.InstanceQualification;

public class HardwiredComponent<D extends HardwiredDeployable, E> {
	private final Supplier<E> expertSupplier;
	private final D deployable;
	private final String idSuffix;

	public HardwiredComponent(Supplier<E> expertSupplier, EntityType<D> deployableType, InstanceQualification qualification) {
		this.expertSupplier = expertSupplier;
		this.idSuffix = StringTools.camelCaseToDashSeparated(qualification.space().getClass().getSimpleName()) + "."
				+ StringTools.camelCaseToDashSeparated(qualification.name());
		this.deployable = createDeployable(deployableType);
	}

	private D createDeployable(EntityType<D> deployableType) {
		D deployable = deployableType.create();
		deployable.setExternalId("hardwired." + idSuffix);
		deployable.setGlobalId("hardwired:" + idSuffix);
		return deployable;
	}

	public String getIdSuffix() {
		return idSuffix;
	}

	public D getTransientDeployable() {
		return deployable;
	}

	public D lookupDeployable(ManagedGmSession session) {
		return session.query().getEntity(deployable.getGlobalId());
	}

	public Supplier<E> getExpertSupplier() {
		return expertSupplier;
	}
}
