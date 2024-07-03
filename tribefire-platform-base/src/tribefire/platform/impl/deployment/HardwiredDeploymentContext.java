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

import java.util.Optional;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.AbstractExpertContext;
import com.braintribe.model.processing.deployment.api.MutableDeploymentContext;
import com.braintribe.model.processing.deployment.api.ResolvedComponent;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * <p>
 * A {@link MutableDeploymentContext} providing values if hardwired.
 *
 * @param <D>
 *            The {@link Deployable} type bound to this context.
 * @param <T>
 *            The expert type bound to this context.
 */
public class HardwiredDeploymentContext<D extends Deployable, T> extends AbstractExpertContext<D> implements MutableDeploymentContext<D, T> {

	private T instance;
	private D deployable;
	private PersistenceGmSession session;

	public HardwiredDeploymentContext(T instance) {
		this.instance = instance;
	}

	@Configurable
	public void setDeployable(D deployable) {
		this.deployable = deployable;
	}

	@Configurable
	public void setSession(PersistenceGmSession session) {
		this.session = session;
	}

	@Override
	public PersistenceGmSession getSession() {
		if (session != null) {
			return session;
		}
		throw unsupported("session");
	}

	@Override
	public <I extends D> I getDeployable() {
		if (deployable != null) {
			return (I) deployable;
		}
		throw unsupported("deployable");
	}

	@Override
	public <I extends T> I getInstanceToBeBound() {
		return (I) instance;
	}

	@Override
	public <E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType) {
		throw new UnsupportedOperationException("resolve() not supported by this "+this.getClass().getSimpleName());
	}

	@Override
	public <E> E resolve(String externalId, EntityType<? extends Deployable> componentType) {
		throw new UnsupportedOperationException("resolve() not supported by this "+this.getClass().getSimpleName());
	}

	@Override
	public <E> E resolve(Deployable deployable, EntityType<? extends Deployable> componentType, Class<E> expertInterface, E defaultDelegate) {
		throw new UnsupportedOperationException("resolve() not supported by this "+this.getClass().getSimpleName());
	}

	@Override
	public <E> E resolve(String externalId, EntityType<? extends Deployable> componentType, Class<E> expertInterface, E defaultDelegate) {
		throw new UnsupportedOperationException("resolve() not supported by this "+this.getClass().getSimpleName());
	}
	
	@Override
	public <E> Optional<ResolvedComponent<E>> resolveOptional(String externalId, EntityType<? extends Deployable> componentType,
			Class<E> expertInterface) {
		throw new UnsupportedOperationException("resolveOptional() not supported by this "+this.getClass().getSimpleName());
	}
	
	

	@Override
	public void setInstanceToBeBoundSupplier(Supplier<? extends T> instanceToBeBoundSupplier) {
		// noop
	}

	@Override
	public T getInstanceToBoundIfSupplied() {
		return instance;
	}
	
	private UnsupportedOperationException unsupported(String property) {
		return new UnsupportedOperationException("No "+property+" configured to this "+this.getClass().getSimpleName());
	}

}
