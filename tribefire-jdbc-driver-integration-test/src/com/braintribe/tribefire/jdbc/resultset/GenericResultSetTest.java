// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.tribefire.jdbc.resultset;

import org.junit.Test;

public class GenericResultSetTest {

	@Test
	public void simpleTests() throws Exception {
		GenericResultSet grs = new GenericResultSet("TABLE_SCHEM", "TABLE_CATALOG");
		grs.addData("tableScheme1", null);
		grs.addData("tableScheme2", "catalog2");
		
		while (grs.next()) {
			String s = grs.getString(1);
			String c = grs.getString("TABLE_CATALOG");
			System.out.println("Schema: "+s+", Catalog: "+c);
		}
		grs.close();
	}
	
	@Test
	public void emptySetTest() throws Exception {
		GenericResultSet grs = new GenericResultSet("TABLE_SCHEM", "TABLE_CATALOG");
		try {
			while (grs.next()) {
				throw new Exception("Unexpected.");
			}
		} finally {
			grs.close();
		}
	}
}
