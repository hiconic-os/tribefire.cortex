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
package com.braintribe.product.rat.imp.impl.model;

import java.util.Collection;

import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.product.rat.imp.GenericMultiImp;

/**
 * A {@link GenericMultiImp} specialized in {@link GmMetaModel}
 */
public class MultiModelImp extends GenericMultiImp<GmMetaModel, ModelImp> {
	private final Collection<ModelImp> modelImps;

	public MultiModelImp(PersistenceGmSession session, Collection<ModelImp> modelImps) {
		super(session, modelImps);
		this.modelImps = modelImps;
	}

	/**
	 * {@link ModelImp#deleteRecursively()} every model managed by this imp
	 */
	public void deleteRecursively() {
		modelImps.forEach(imp -> imp.deleteRecursively());
	}

}
