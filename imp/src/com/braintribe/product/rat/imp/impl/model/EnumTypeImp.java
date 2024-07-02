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
package com.braintribe.product.rat.imp.impl.model;

import com.braintribe.model.meta.GmEnumConstant;
import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

/**
 * A {@link SimpleTypeImp} specialized in {@link GmEnumType}
 */
public class EnumTypeImp extends SimpleTypeImp<GmEnumType> {

	EnumTypeImp(PersistenceGmSession session, GmEnumType gmType) {
		super(session, gmType);
	}

	/**
	 * creates new EnumConstants with provided names and adds them to the EnumType managed by this imp
	 */
	public EnumTypeImp addConstants(String... constantNames) {

		for (String constantName : constantNames) {
			GmEnumConstant constant = session().create(GmEnumConstant.T);
			constant.setDeclaringType(this.instance);
			constant.setName(constantName);

			this.instance.getConstants().add(constant);
		}

		return this;
	}

	/**
	 * removes all constants with names equal to the passed ones from the EnumType managed by this imp
	 */
	public EnumTypeImp removeConstants(String... constantNames) {
		for (String constantName : constantNames) {
			instance.getConstants().removeIf(c -> constantName.equals(c.getName()));
		}

		return this;
	}

}
