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
package tribefire.cortex.initializer.support.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.SetGlobalIds;
import tribefire.cortex.initializer.support.impl.GlobalIdAssigner;
import tribefire.cortex.initializer.support.wire.contract.InitializerSupportContract;

/**
 * The sole purpose of this space class is setting the global id for GE instances created inside initializer spaces,
 * which are marked by the {@link SetGlobalIds} interface. <br>
 * 
 * globalId format: {globalIdScheme}://${globalIdPrefix}/${initializerSpaceId}/${managedInstanceName}. <br>

 */
@Managed
public class MetaSpace implements WireSpace {

	@Import
	private InitializerSupportContract initializerSpace;
	
	@Import
	private WireContext<?> wireContext;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		configuration.addLifecycleListener(globalIdAssigner());
	}

	@Managed
	private GlobalIdAssigner globalIdAssigner() {
		GlobalIdAssigner bean = new GlobalIdAssigner();
		
		bean.setInitializerSpace(initializerSpace);
		bean.setWireContext(wireContext);
		
		return bean;
	}
}
