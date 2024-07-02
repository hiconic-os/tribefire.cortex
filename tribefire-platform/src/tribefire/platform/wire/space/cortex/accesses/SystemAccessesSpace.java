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
package tribefire.platform.wire.space.cortex.accesses;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.messaging.accesses.TransientMessagingDataAccessSpace;
import tribefire.platform.wire.space.messaging.accesses.TransientMessagingDataWorkbenchAccessSpace;
import tribefire.platform.wire.space.security.accesses.AuthAccessSpace;
import tribefire.platform.wire.space.security.accesses.AuthWorkbenchAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserSessionsAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserSessionsWorkbenchAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserStatisticsAccessSpace;
import tribefire.platform.wire.space.security.accesses.UserStatisticsWorkbenchAccessSpace;

@Managed
public class SystemAccessesSpace implements WireSpace {

	@Import
	private CortexAccessSpace cortexAccess;

	@Import
	private CortexWorkbenchAccessSpace cortexWorkbenchAccess;

	@Import
	private WorkbenchAccessSpace workbenchAccess;

	@Import
	private AuthAccessSpace authAccess;

	@Import
	private AuthWorkbenchAccessSpace authWorkbenchAccess;

	@Import
	private UserSessionsAccessSpace userSessionsAccess;

	@Import
	private UserSessionsWorkbenchAccessSpace userSessionsWorkbenchAccess;

	@Import
	private UserStatisticsAccessSpace userStatisticsAccess;

	@Import
	private UserStatisticsWorkbenchAccessSpace userStatisticsWorkbenchAccess;

	@Import
	private PlatformSetupAccessSpace platformSetupAccess;

	@Import
	private PlatformSetupWorkbenchAccessSpace platformSetupWorkbenchAccess;
	
	@Import 
	private TransientMessagingDataAccessSpace transientMessagingDataAccess;
	
	@Import 
	private TransientMessagingDataWorkbenchAccessSpace transientMessagingDataWorkbenchAccess;
	
	public CortexAccessSpace cortex() {
		return cortexAccess;
	}

	public CortexWorkbenchAccessSpace cortexWorkbench() {
		return cortexWorkbenchAccess;
	}

	public WorkbenchAccessSpace workbench() {
		return workbenchAccess;
	}

	public AuthAccessSpace auth() {
		return authAccess;
	}

	public AuthWorkbenchAccessSpace authWorkbench() {
		return authWorkbenchAccess;
	}

	public UserSessionsAccessSpace userSessions() {
		return userSessionsAccess;
	}

	public UserSessionsWorkbenchAccessSpace userSessionsWorkbench() {
		return userSessionsWorkbenchAccess;
	}

	public UserStatisticsAccessSpace userStatistics() {
		return userStatisticsAccess;
	}

	public UserStatisticsWorkbenchAccessSpace userStatisticsWorkbench() {
		return userStatisticsWorkbenchAccess;
	}

	public PlatformSetupAccessSpace platformSetup() {
		return platformSetupAccess;
	}
	
	public PlatformSetupWorkbenchAccessSpace platformSetupWorkbench() {
		return platformSetupWorkbenchAccess;
	}
	
	public TransientMessagingDataAccessSpace transientMessagingDataAccess() {
		return transientMessagingDataAccess;
	}
	
	public TransientMessagingDataWorkbenchAccessSpace transientMessagingDataWorkbenchAccess() {
		return transientMessagingDataWorkbenchAccess;
	}

}
