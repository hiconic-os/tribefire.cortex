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
package tribefire.platform.impl.service.async;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.braintribe.model.execution.persistence.JobState;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.service.api.AsynchronousRequest;
import com.braintribe.model.service.persistence.ServiceRequestJob;

public class PersistingServiceRequestBlockingQueue implements BlockingQueue<Runnable> {

	protected Supplier<PersistenceGmSession> sessionSupplier;
	private String discriminator;
	private ServiceRequestPersistence serviceRequestPersistence;
	
	private LinkedBlockingQueue<Runnable> delegate;


	public PersistingServiceRequestBlockingQueue(Supplier<PersistenceGmSession> sessionSupplier, String discriminator, ServiceRequestPersistence serverPersistence) {
		this.sessionSupplier = sessionSupplier;
		this.discriminator = discriminator;
		this.serviceRequestPersistence = serverPersistence;
	}


	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean add(Runnable e) {
		return delegate.add(e);
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public Runnable remove() {
		return delegate.remove();
	}

	@Override
	public Runnable element() {
		return delegate.element();
	}

	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	@Override
	public boolean addAll(Collection<? extends Runnable> c) {
		return delegate.addAll(c);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public int remainingCapacity() {
		return delegate.remainingCapacity();
	}

	@Override
	public void put(Runnable e) throws InterruptedException {
		delegate.put(e);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	@Override
	public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException {
		return delegate.offer(e, timeout, unit);
	}

	@Override
	public boolean offer(Runnable e) {
		
		if (!(e instanceof PersistedServiceRequestRunnable)) {
			throw new IllegalArgumentException("Unsupported runnable "+e+", only accepting: PersistedServiceRequestRunnable");
		}
		PersistedServiceRequestRunnable pe = (PersistedServiceRequestRunnable) e;
		
		if (!pe.getPersisted()) {
			PersistenceGmSession session = sessionSupplier.get();
			String correlationId = pe.getCorrelationId();
			AsynchronousRequest asyncRequest = pe.getAsyncRequest();
			ServiceRequestJob requestJob = serviceRequestPersistence.persistServiceRequest(session, correlationId, asyncRequest, discriminator, null);
			requestJob.setState(JobState.enqueued);
			session.commit();
			pe.setPersisted(true);
		}
		
		return delegate.offer(e);
	}

	@Override
	public Runnable take() throws InterruptedException {
		return delegate.take();
	}

	@Override
	public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
		return delegate.poll(timeout, unit);
	}

	@Override
	public Runnable poll() {
		return delegate.poll();
	}

	@Override
	public Runnable peek() {
		return delegate.peek();
	}

	@Override
	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	@Override
	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public int drainTo(Collection<? super Runnable> c) {
		return delegate.drainTo(c);
	}

	@Override
	public int drainTo(Collection<? super Runnable> c, int maxElements) {
		return delegate.drainTo(c, maxElements);
	}

	@Override
	public Iterator<Runnable> iterator() {
		return delegate.iterator();
	}

	@Override
	public Spliterator<Runnable> spliterator() {
		return delegate.spliterator();
	}

	@Override
	public void forEach(Consumer<? super Runnable> action) {
		delegate.forEach(action);
	}

	@Override
	public boolean removeIf(Predicate<? super Runnable> filter) {
		return delegate.removeIf(filter);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}
}
