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
package com.braintribe.product.rat.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * A MultiImp follows the same rules as the {@link AbstractImp imp}, but can manage a list of instances instead of just
 * one. It can hold zero up to multiple instances.
 *
 * @param <T>
 *            Type of the instances that will be managed by this imp
 * @param <I>
 *            Type of the imps that will manage these instances
 */
public class GenericMultiImp<T extends GenericEntity, I extends Imp<T>> extends AbstractHasSession implements Iterable<I> {

	private final List<I> memberImps;

	public GenericMultiImp(PersistenceGmSession session, Collection<I> memberImps) {
		super(session);
		this.memberImps = new ArrayList<I>(memberImps);
	}

	/**
	 * @return the list of instances managed by this multi-imp
	 */
	public List<T> get() {
		return memberImps.stream().map(imp -> imp.get()).collect(Collectors.toList());
	}

	/**
	 * deletes all the instances/entities managed by this multi-imp from the access/session
	 */
	public void delete() {
		memberImps.stream().forEach(imp -> imp.delete());
	}

	@Override
	public Iterator<I> iterator() {
		return memberImps.iterator();
	}
}
