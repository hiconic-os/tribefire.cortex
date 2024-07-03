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
package com.braintribe.model.processing.deployment.processor;

import java.util.Collections;

import com.braintribe.model.access.ModelAccessException;
import com.braintribe.model.access.NonIncrementalAccess;
import com.braintribe.model.meta.GmMetaModel;

/**
 * This is a {@link NonIncrementalAccess} we use as a data-delegate in our test SMOODs. The point is, that this does not
 * store any data, so we are guaranteed every test starts with an empty smood.
 */
public class TransientAccess implements NonIncrementalAccess {

	private GmMetaModel metaModel;

	public TransientAccess() {

	}

	public TransientAccess(GmMetaModel metaModel) {
		this.metaModel = metaModel;
	}

	public void setMetaModel(GmMetaModel metaModel) {
		this.metaModel = metaModel;
	}

	@Override
	public GmMetaModel getMetaModel() {
		return metaModel;
	}

	@Override
	public Object loadModel() throws ModelAccessException {
		return Collections.emptySet();
	}

	@Override
	public void storeModel(Object model) throws ModelAccessException {
		// do nothing
	}

}
