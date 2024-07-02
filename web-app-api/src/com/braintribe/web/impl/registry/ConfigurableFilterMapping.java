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
package com.braintribe.web.impl.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.DispatcherType;

import com.braintribe.web.api.registry.FilterMapping;

public abstract class ConfigurableFilterMapping implements FilterMapping {

	protected EnumSet<DispatcherType> dispatcherTypes;
	protected boolean isMatchAfter;
	protected List<String> keys = Collections.emptyList();

	@Override
	public EnumSet<DispatcherType> getDispatcherTypes() {
		return dispatcherTypes;
	}

	public void setDispatcherTypes(Set<DispatcherType> dispatcherTypes) {
		if (dispatcherTypes != null) {
			List<DispatcherType> copyList = new ArrayList<DispatcherType>(dispatcherTypes);
			if (copyList.size() > 0) {
				DispatcherType first = copyList.remove(0);
				this.dispatcherTypes = EnumSet.<DispatcherType> of(first, copyList.toArray(new DispatcherType[copyList.size()]));
			}
		}
	}

	@Override
	public boolean isMatchAfter() {
		return isMatchAfter;
	}

	public void setMatchAfter(boolean isMatchAfter) {
		this.isMatchAfter = isMatchAfter;
	}

	@Override
	public List<String> getKeys() {
		return keys;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ConfigurableFilterMapping[");
		if (this.dispatcherTypes != null) {
			sb.append("DispatcherTypes:");
			sb.append(dispatcherTypes);
			sb.append(';');
		}
		sb.append("isMatchAfter:");
		sb.append(this.isMatchAfter);
		if (this.keys != null) {
			sb.append(";Keys=");
			sb.append(this.keys.toString());
		}
		sb.append(']');
		return sb.toString();
	}

}
