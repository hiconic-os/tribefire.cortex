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

import java.util.Date;

import javax.sql.DataSource;

import org.junit.Ignore;

import com.braintribe.model.processing.locking.db.impl.DbLocking;
import com.braintribe.utils.DateTools;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Special DBHandler that connects to a local, running MySQL/MariaDB. This is for local test purposes only.
 */
@Ignore
public class MariaDbHandler implements DbHandler {

	protected DataSource dataSource = null;
	protected DbLocking dbLocking;

	@Override
	public void initialize() throws Exception {
		dataSource();
		locking();
	}

	@Override
	public DataSource dataSource() {
		if (dataSource == null) {
			HikariDataSource bean = new HikariDataSource();
			try {
				bean.setDriverClassName("org.mariadb.jdbc.Driver");
			} catch (Exception e) {
				throw new RuntimeException("Could not set driver class.", e);
			}
			bean.setJdbcUrl("jdbc:mariadb://localhost:3306/TF");
			bean.setUsername("cortex");
			bean.setPassword("cortex");
			dataSource = bean;
		}
		return dataSource;
	}

	@Override
	public DbLocking locking() {
		if (dbLocking == null) {
			DbLocking bean = new DbLocking();
			bean.setDataSource(dataSource());
			bean.setAutoUpdateSchema(true);
			bean.postConstruct();
			dbLocking = bean;
		}
		return dbLocking;
	}

	/* package */ static void print(String text) {
		System.out.println(DateTools.encode(new Date(), DateTools.LEGACY_DATETIME_WITH_MS_FORMAT) + " [Master]: " + text);
	}

	@Override
	public void destroy() {
		// Do nothing
	}

}
