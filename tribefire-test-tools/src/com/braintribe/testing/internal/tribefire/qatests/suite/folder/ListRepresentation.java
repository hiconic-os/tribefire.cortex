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
package com.braintribe.testing.internal.tribefire.qatests.suite.folder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class ListRepresentation<E> implements GmInstanceRepresentation<List<E>> {
	private static Logger logger = Logger.getLogger(ListRepresentation.class);
	
	private final List<GmInstanceRepresentation<Object>> content;

	public ListRepresentation() {
		content = new ArrayList<>();
	}

	@Override
	public boolean matches(List<E> actual) {
		if (actual.size() != content.size())
			return false;
			
		Iterator<E> actualIterator = actual.iterator();
		Iterator<GmInstanceRepresentation<Object>> contentIterator = content.iterator();
		
		while (actualIterator.hasNext()) {
			E actualElement = actualIterator.next();
			GmInstanceRepresentation<Object> contentElement = contentIterator.next();
			
			if (!contentElement.matches(actualElement))
				return false;
		}
		
		return true;
	}

	@Override
	public List<E> create(PersistenceGmSession session) {
		return null;
		
	}

}
