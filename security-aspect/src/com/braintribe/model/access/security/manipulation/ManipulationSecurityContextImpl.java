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
package com.braintribe.model.access.security.manipulation;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;

import com.braintribe.model.processing.manipulation.basic.BasicReferenceResolvingManipulationContext;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.security.manipulation.ManipulationSecurityExpositionContext;
import com.braintribe.model.processing.security.manipulation.SecurityViolationEntry;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * 
 */
class ManipulationSecurityContextImpl extends BasicReferenceResolvingManipulationContext implements ManipulationSecurityExpositionContext {

	private final CmdResolver cmdResolver;
	private Object expertContext;
	private List<SecurityViolationEntry> violationEntries;

	public ManipulationSecurityContextImpl(PersistenceGmSession session, CmdResolver cmdResolver) {
		super(session);

		this.cmdResolver = cmdResolver;
	}

	@Override
	public PersistenceGmSession getSession() {
		return session;
	}

	@Override
	public CmdResolver getCmdResolver() {
		return cmdResolver;
	}

	@Override
	public <T> T getExpertContext() {
		return (T) expertContext;
	}

	public void setExpertContext(Object expertContext) {
		this.expertContext = expertContext;
	}

	@Override
	public void addViolationEntry(SecurityViolationEntry entry) {
		if (violationEntries == null)
			violationEntries = newList();

		violationEntries.add(entry);
	}

	List<SecurityViolationEntry> getViolationEntries() {
		return violationEntries;
	}

}
