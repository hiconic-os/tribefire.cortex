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
package tribefire.platform.wire.space.messaging.accesses;

import com.braintribe.common.MutuallyExclusiveReadWriteLock;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.access.impl.XmlAccess;
import com.braintribe.model.access.smood.basic.SmoodAccess;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.deployment.resource.sql.SqlBinaryProcessor;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.impl.denotrans.TransientMessagingAccessWithSqlBinaryProcessorEnricher;
import tribefire.platform.wire.space.SchrodingerBeansSpace;
import tribefire.platform.wire.space.cortex.accesses.SchrodingerBeanSystemAccessSpaceBase;
import tribefire.platform.wire.space.cortex.accesses.TribefireProductModels;
import tribefire.platform.wire.space.cortex.accesses.TribefireProductModels.TribefireProductModel;

/**
 * A companion {@link SqlBinaryProcessor} may be configured, see {@link TransientMessagingAccessWithSqlBinaryProcessorEnricher}.
 */
@Managed
public class TransientMessagingDataAccessSpace extends SchrodingerBeanSystemAccessSpaceBase {

	public static final String id = TribefireConstants.ACCESS_TRANSIENT_MESSAGING_DATA;
	public static final String name = TribefireConstants.ACCESS_TRANSIENT_MESSAGING_DATA_NAME;

	public static final TribefireProductModel model = TribefireProductModels.transientMessagingDataAccessModel;
	public static final String modelName = model.modelName;

	@Import
	private SchrodingerBeansSpace schrodingerBeans;

	// @formatter:off
	@Override public String id() { return id; }
	@Override public String name() { return name; }
	@Override public String modelName() { return modelName; }
	// @formatter:on

	@Managed
	@Override
	public IncrementalAccess access() {
		return accessSchrodingerBean().proxy();
	}

	@Managed
	public SchrodingerBean<IncrementalAccess> accessSchrodingerBean() {
		return schrodingerBeans.newBean("TransientMessagingDataAccess", CortexConfiguration::getTransientMessagingAccess,
				binders.incrementalAccess());
	}

	public SmoodAccess defaultAccess() {
		SmoodAccess bean = new SmoodAccess();
		bean.setAccessId(id());
		bean.setReadWriteLock(new MutuallyExclusiveReadWriteLock());
		bean.setDataDelegate(fileAccess());
		bean.setManipulationBuffer(manipulationStorage());
		bean.setBufferFlushThresholdInBytes(52428800L); // 5 MB
		return bean;
	}

	@Managed
	private XmlAccess fileAccess() {
		XmlAccess bean = new XmlAccess();
		bean.setFilePath(dataStorageFile());
		bean.setModelProvider(metaModelProvider());
		return bean;
	}

}
