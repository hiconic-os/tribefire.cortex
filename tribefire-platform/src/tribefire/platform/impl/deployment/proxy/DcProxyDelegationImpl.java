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
package tribefire.platform.impl.deployment.proxy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.braintribe.cfg.InitializationAware;
import com.braintribe.logging.Logger;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.deployment.api.ConfigurableDcProxyDelegation;
import com.braintribe.model.processing.deployment.api.DcProxy;
import com.braintribe.model.processing.deployment.api.DcProxyDelegation;
import com.braintribe.model.processing.deployment.api.DcProxyListener;
import com.braintribe.model.processing.deployment.api.DeployRegistry;
import com.braintribe.model.processing.deployment.api.DeployRegistryListener;
import com.braintribe.model.processing.deployment.api.DeployedUnit;
import com.braintribe.model.processing.deployment.api.DeploymentException;
import com.braintribe.model.processing.deployment.api.ResolvedComponent;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.model.service.api.InstanceId;

/**
 * Internal delegation handler of {@link DcProxy deployable proxies}, which upon deployment notification, can handle it's proxied interface method
 * calls using a deployed component as a delegate.
 * 
 * @see DcProxyDelegation
 * 
 * @author dirk.scheffler
 */
public class DcProxyDelegationImpl implements DeployRegistryListener, InitializationAware, ConfigurableDcProxyDelegation {

	private static final Logger log = Logger.getLogger(DcProxyDelegationImpl.class);

	private final int proxyId;
	private final ReentrantLock lock = new ReentrantLock();

	// There was a real bug where delegate would be accessed and not seen even though it was set a few ms earlier (according to logs) 
	private volatile String externalId;
	private volatile Object delegate;
	private volatile Object defaultDelegate;
	private volatile EntityType<? extends Deployable> componentType;
	private volatile DeployRegistry deployRegistry;
	private volatile InstanceId processingInstanceId;
	private volatile Consumer<String> inDeploymentBlocker = s -> {
		/* noop */ };

	private DeployedUnit deployedUnit;

	private final Set<DcProxyListener> dcProxyListeners = new CopyOnWriteArraySet<>();

	private static AtomicInteger counter = new AtomicInteger(1);

	public DcProxyDelegationImpl() {
		proxyId = counter.getAndIncrement();
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		logDebug(() -> "Setting externalId to " + externalId);
	}

	@Override
	public void changeExternalId(String externalId) {
		this.externalId = externalId;
		this.fetchDelegate();
	}

	public void setComponentType(EntityType<? extends Deployable> componentKey) {
		this.componentType = componentKey;
	}

	@Override
	public void setDefaultDelegate(Object defaultDelegate) {
		this.defaultDelegate = defaultDelegate;

		dcProxyListeners.forEach(l -> l.onDefaultDelegateSet(defaultDelegate));
	}

	public void setDeployRegistry(DeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;

		this.deployRegistry.addListener(this);
	}

	public void setProcessingInstanceId(InstanceId processingInstanceId) {
		this.processingInstanceId = processingInstanceId;
	}

	public void setInDeploymentBlocker(Consumer<String> inDeploymentBlocker) {
		this.inDeploymentBlocker = inDeploymentBlocker;
	}

	@Override
	public void addDcProxyListener(DcProxyListener listener) {
		dcProxyListeners.add(listener);
	}

	@Override
	public void removeDcProxyListener(DcProxyListener listener) {
		dcProxyListeners.remove(listener);
	}

	@Override
	public final Object getDelegate() throws DeploymentException {
		if (delegate == null) {
			// wait for deployment if externalId is currently in deployment
			logDebug(() -> "Waiting for deployment of [" + externalId + "]");
			inDeploymentBlocker.accept(externalId);
		}

		Object component = delegate;
		if (component != null)
			return component;

		if (defaultDelegate != null)
			return defaultDelegate;

		throw new DeploymentException(deployableCannotBeAccessedMsg());
	}

	private String deployableCannotBeAccessedMsg() {
		String msg = proxyUniqueName() + processingInstanceId.getApplicationId() + " cannot access deployable with external id [" + externalId
				+ "]. Component type: " + componentType.getTypeSignature() + " It is probably not deployed.";

		if (SchrodingerBean.isSchrodingerBeanId(externalId))
			msg += " As this is a Schrodinger bean, it can only be accessed after cortex has been initialied. Maybe it's accessed too early? "
					+ "Seems probabale, as later it's externalId would be changed to that of the deployable, and we would not even be able to tell it's a Schrodinger Bean";

		return msg;
	}

	@Override
	public DeployedUnit getDeployedUnit() {
		return deployedUnit;
	}

	@Override
	public final <E> ResolvedComponent<E> getDelegateOptional() {
		if (delegate == null)
			// wait for deployment if externalId is currently in deployment
			inDeploymentBlocker.accept(externalId);

		DeployedUnit resolvedUnit;
		Object resolvedDelegate;

		lock.lock();
		try {
			resolvedUnit = deployedUnit;
			resolvedDelegate = delegate;
		} finally {
			lock.unlock();
		}

		if (resolvedUnit == null)
			return null;

		return new ResolvedComponent<E>() {
			// @formatter:off
			@Override public E component() { return (E) resolvedDelegate; }
			@Override public DeployedUnit deployedUnit() { return deployedUnit; }
			// @formatter:on
		};
	}

	@Override
	public void onDeploy(Deployable deployable, DeployedUnit deployedUnit) {
		if (haltCallback(deployable, deployedUnit))
			return;

		if (externalId.equals(deployable.getExternalId())) {
			lock.lock();
			try {
				takeDelegate(deployedUnit, "deployment");
			} finally {
				lock.unlock();
			}

		} else if (log.isTraceEnabled()) {
			log.trace(proxyUniqueName() + "Proxy for [ " + externalId + " ] got notification from unrelated deployment: [ "
					+ deployable.getExternalId() + " ]");
		}
	}

	@Override
	public void onUndeploy(Deployable deployable, DeployedUnit deployedUnit) {
		if (haltCallback(deployable, deployedUnit))
			return;

		if (externalId.equals(deployable.getExternalId())) {
			lock.lock();
			try {
				clearDelegate();
			} finally {
				lock.unlock();
			}

		} else if (log.isTraceEnabled()) {
			log.trace("Proxy for [ " + externalId + " ] got notification from unrelated undeployment: [ " + deployable.getExternalId() + " ]");
		}

	}

	@Override
	public void postConstruct() {
		fetchDelegate();
	}

	private void fetchDelegate() {
		lock.lock();
		try {
			DeployedUnit deployedUnit = deployRegistry.resolve(externalId);

			if (deployedUnit != null)
				takeDelegate(deployedUnit, "construction");
			else
				clearDelegate();
		} finally {
			lock.unlock();
		}
	}

	private void takeDelegate(DeployedUnit deployedUnit, String source) {
		this.deployedUnit = deployedUnit;
		this.delegate = deployedUnit.getComponent(componentType);

		logDebug(() -> "Delegate set for [" + externalId + "] upon " + source + ": " + delegate);

		dcProxyListeners.forEach(l -> l.onDelegateSet(delegate));
	}

	private void clearDelegate() {
		Object oldDelegate = delegate;

		this.deployedUnit = null;
		this.delegate = null;


		if (oldDelegate != null) {
			logDebug(() -> "Cleared delegate for [" + externalId + "]: " + oldDelegate);
			dcProxyListeners.forEach(l -> l.onDelegateCleared(oldDelegate));
		}
	}

	private boolean haltCallback(Deployable deployable, DeployedUnit deployedUnit) {
		if (deployable != null && deployedUnit != null)
			return false;

		log.error(proxyUniqueName() + "Invalid callback state. deployable: [ " + deployable + " ] deployedUnit: [ " + deployedUnit + " ]",
				new NullPointerException());
		return true;
	}

	private void logDebug(Supplier<String> msg) {
		if (log.isDebugEnabled())
			log.debug(proxyUniqueName() + msg.get());
	}

	private String proxyUniqueName() {
		return "Proxy[" + proxyId + "] ";
	}
}
