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
package com.braintribe.model.access.collaboration.distributed.api;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.model.access.smood.collaboration.distributed.api.sharedstorage.AbstractSharedStorageTest;
import com.braintribe.testing.category.SpecialEnvironment;

@Category(SpecialEnvironment.class)
public class JdbcSharedStorageTest extends AbstractSharedStorageTest {

	protected static DbHandler dbHandler;

	@BeforeClass
	public static void beforeClass() throws Exception {
		dbHandler = new DerbyDbHandler();
		dbHandler.initialize();
	}

	@AfterClass
	public static void destroyDatabase() throws Exception {
		if (dbHandler != null) {
			dbHandler.destroy();
		}
	}

	@Override
	protected DcsaSharedStorage newDcsaSharedStorage() {

		JdbcDcsaStorage storage = new JdbcDcsaStorage();
		storage.setProjectId("storage-test-" + UUID.randomUUID().toString());
		storage.setDataSource(dbHandler.dataSource());
		storage.setLocking(dbHandler.locking());
		JsonStreamMarshaller marshaller = new JsonStreamMarshaller();
		storage.setMarshaller(marshaller);
		storage.postConstruct();

		return storage;
	}

}
