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
package tribefire.platform.wire.space.security.accesses;

import java.util.function.Supplier;

import com.braintribe.common.MutuallyExclusiveReadWriteLock;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.access.impl.XmlAccess;
import com.braintribe.model.access.smood.basic.SmoodAccess;
import com.braintribe.model.cortex.deployment.CortexConfiguration;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.deployment.api.SchrodingerBean;
import com.braintribe.model.processing.tfconstants.TribefireConstants;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.platform.wire.space.SchrodingerBeansSpace;
import tribefire.platform.wire.space.cortex.accesses.SchrodingerBeanSystemAccessSpaceBase;
import tribefire.platform.wire.space.cortex.accesses.TribefireProductModels;
import tribefire.platform.wire.space.cortex.accesses.TribefireProductModels.TribefireProductModel;

@Managed
public class UserSessionsAccessSpace extends SchrodingerBeanSystemAccessSpaceBase {

	public static final String id = TribefireConstants.ACCESS_USER_SESSIONS;
	public static final String name = TribefireConstants.ACCESS_USER_SESSIONS_NAME;
	public static final TribefireProductModel model = TribefireProductModels.userSessionsAccessModel;
	public static final TribefireProductModel serviceModel = TribefireProductModels.userSessionsAccessServiceModel;
	public static final String modelName = model.modelName;
	public static final String serviceModelName = serviceModel.modelName;

	// @formatter:off
	@Override public String id() { return id; }
	@Override public String name() { return name; }
	@Override public String modelName() { return modelName; }
	@Override public String serviceModelName() { return serviceModelName; }
	// @formatter:on

	@Import
	private SchrodingerBeansSpace schrodingerBeans;

	@Managed
	@Override
	public IncrementalAccess access() {
		return accessSchrodingerBean().proxy();
	}

	@Managed
	public SchrodingerBean<IncrementalAccess> accessSchrodingerBean() {
		return schrodingerBeans.newBean("UserSessionsAccess", CortexConfiguration::getUserSessionsAccess, binders.incrementalAccess());
	}

	@Managed
	public SmoodAccess defaultAccess() {
		SmoodAccess bean = new SmoodAccess();
		bean.setAccessId(id());
		bean.setReadWriteLock(new MutuallyExclusiveReadWriteLock());
		bean.setDataDelegate(fileAccess());
		bean.setManipulationBuffer(manipulationStorage());
		bean.setBufferFlushThresholdInBytes(52428800L);
		return bean;
	}

	@Managed
	private XmlAccess fileAccess() {
		XmlAccess bean = new XmlAccess();
		bean.setFilePath(dataStorageFile());
		bean.setModelProvider(metaModelProvider());
		return bean;
	}

	@Override
	@Managed
	public Supplier<GmMetaModel> metaModelProvider() {
		return () -> {
			GmMetaModel virtualModel = GmMetaModel.T.create();
			virtualModel.setName(modelName);
			for (String dependency : model.dependencies) {
				GmMetaModel classPathModel = GMF.getTypeReflection().getModel(dependency).getMetaModel();
				virtualModel.getDependencies().add(classPathModel);
			}
			return virtualModel;
		};
	}

}
