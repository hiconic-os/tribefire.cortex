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
package com.braintribe.model.processing.deployment.hibernate;

import org.junit.Test;

import com.braintribe.model.dbs.DbColumn;
import com.braintribe.utils.junit.assertions.BtAssertions;

/**
 * @see DbTableProcessingUtils
 */
public class DbTableProcessingUtilsTests {

	@Test
	public void propertyNameDerivations() throws Exception {
		assertPropertyName("my_property", "myProperty");
		assertPropertyName("MyProperty", "myProperty");
		assertPropertyName("ID", "id");
		assertPropertyName("iD", "id");
		assertPropertyName("URL", "url");
		assertPropertyName("uRL", "url");
		assertPropertyName("URL_property", "urlProperty");
		assertPropertyName("URL property", "urlProperty");
		assertPropertyName("URL Property", "urlProperty");
		assertPropertyName("AVI2MPG", "avi2MPG");
	}

	private void assertPropertyName(String columnName, String expectedPropertyName) {
		DbColumn dbColumn = DbColumn.T.create();
		dbColumn.setName(columnName);

		String actualPropertyName = DbTableProcessingUtils.getPropertyName(dbColumn);

		BtAssertions.assertThat(actualPropertyName).isEqualTo(expectedPropertyName);
	}

}
