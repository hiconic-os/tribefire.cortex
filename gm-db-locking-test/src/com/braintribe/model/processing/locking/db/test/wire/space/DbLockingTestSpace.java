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
package com.braintribe.model.processing.locking.db.test.wire.space;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.braintribe.codec.marshaller.bin.BinMarshaller;
import com.braintribe.codec.marshaller.common.BasicConfigurableMarshallerRegistry;
import com.braintribe.common.concurrent.TaskScheduler;
import com.braintribe.common.concurrent.TaskSchedulerImpl;
import com.braintribe.common.db.DbVendor;
import com.braintribe.common.db.wire.contract.DbTestDataSourcesContract;
import com.braintribe.model.processing.locking.db.impl.DbLocking;
import com.braintribe.model.processing.locking.db.test.wire.contract.DbLockingTestContract;
import com.braintribe.transport.messaging.api.MessagingContext;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.scope.InstanceConfiguration;

@Managed
public class DbLockingTestSpace implements DbLockingTestContract {

	private static int LOCK_REFRESH_INTERVAL_SEC = LOCK_EXPIRATION_SEC / 2;

	@Import
	private DbTestDataSourcesContract dataSources;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		DbLockingTestContract.super.onLoaded(configuration);
	}

	@Override
	@Managed
	public DbLocking locking(DbVendor vendor) {
		DbLocking bean = new DbLocking();
		bean.setDataSource(dataSources.dataSource(vendor));
		// For now we test this without messaging.
		// Used to be active-mq messaging, but that needs a worker from tf.extension
		// messaging is not needed for correct behavior anyway, it's just an optimization.
		bean.setLockExpirationInSecs(LOCK_EXPIRATION_SEC);

		taskScheduler().scheduleAtFixedRate("locking-refresher", bean::refreshLockedLocks, //
				LOCK_REFRESH_INTERVAL_SEC, LOCK_REFRESH_INTERVAL_SEC, TimeUnit.SECONDS) //
				.done();

		return bean;
	}

	@Managed
	private MessagingContext messagingContext() {
		MessagingContext bean = new MessagingContext();
		bean.setMarshaller(binMarshaller());
		return bean;
	}

	@Managed
	private BasicConfigurableMarshallerRegistry marshallerRegistry() {
		BasicConfigurableMarshallerRegistry bean = new BasicConfigurableMarshallerRegistry();
		bean.registerMarshaller("application/gm", binMarshaller());
		bean.registerMarshaller("gm/bin", binMarshaller());
		return bean;
	}

	@Managed
	private BinMarshaller binMarshaller() {
		BinMarshaller bean = new BinMarshaller();
		bean.setWriteRequiredTypes(false);
		return bean;
	}

	@Managed
	private TaskScheduler taskScheduler() {
		TaskSchedulerImpl bean = new TaskSchedulerImpl();
		bean.setName("platform-scheduler");
		bean.setExecutor(scheduledThreadPool());

		return bean;
	}

	@Managed
	private ScheduledExecutorService scheduledThreadPool() {
		ScheduledExecutorService bean = Executors.newScheduledThreadPool(5);

		InstanceConfiguration.currentInstance().onDestroy(() -> 
			bean.shutdownNow()
		);

		return bean;
	}

}
