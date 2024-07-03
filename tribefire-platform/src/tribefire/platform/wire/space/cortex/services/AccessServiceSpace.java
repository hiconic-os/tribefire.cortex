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
package tribefire.platform.wire.space.cortex.services;

import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_QUERYTRACING_EXECUTIONTHRESHOLD_INFO;
import static com.braintribe.model.processing.bootstrapping.TribefireRuntime.ENVIRONMENT_QUERYTRACING_EXECUTIONTHRESHOLD_WARNING;
import static com.braintribe.wire.api.util.Sets.set;

import com.braintribe.model.access.AccessService;
import com.braintribe.model.processing.access.service.impl.standard.AccessServiceImpl;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.impl.request.InternalAccessService;
import tribefire.platform.wire.space.common.EnvironmentSpace;
import tribefire.platform.wire.space.cortex.GmSessionsSpace;
import tribefire.platform.wire.space.cortex.accesses.CortexAccessSpace;
import tribefire.platform.wire.space.cortex.accesses.SystemAccessesSpace;
import tribefire.platform.wire.space.cortex.deployment.DeploymentSpace;
import tribefire.platform.wire.space.security.AuthContextSpace;

@Managed
public class AccessServiceSpace implements WireSpace {

	private static final String serviceId = "ACCESS";

	// @formatter:off
	@Import private AuthContextSpace authContext; 
	@Import private CortexAccessSpace cortex; 	
	@Import private DeploymentSpace deployment; 
	@Import private EnvironmentSpace environment; 
	@Import private GmSessionsSpace gmSessions; 
	@Import private SystemAccessesSpace systemAccesses; 
	// @formatter:on

	public String serviceId() {
		return serviceId;
	}

	@Managed
	public AccessServiceImpl service() {
		AccessServiceImpl bean = new AccessServiceImpl();
		bean.setUserRolesProvider(authContext.currentUser().rolesProvider());
		bean.setSystemModelAccessoryFactory(gmSessions.systemModelAccessoryFactory());
		bean.setUserModelAccessoryFactory(gmSessions.userModelAccessoryFactory());
		bean.setTrustedRoles(set("tf-internal"));
		bean.setQueryExecutionInfoThreshold(environment.property(ENVIRONMENT_QUERYTRACING_EXECUTIONTHRESHOLD_INFO, Long.class, 10000L));
		bean.setQueryExecutionWarningThreshold(environment.property(ENVIRONMENT_QUERYTRACING_EXECUTIONTHRESHOLD_WARNING, Long.class, 30000L));
		bean.setInternalCortexSessionSupplier(cortex::lowLevelSession);
		return bean;
	}

	@Managed 
	public AccessService internalService() {
		InternalAccessService bean = new InternalAccessService();
		bean.setDelegate(service());
		bean.setInternalSessionProvider(authContext.internalUser().userSessionProvider());
		bean.setCurrentUserSessionStack(authContext.currentUser().userSessionStack());
		
		return bean;
	}

}
