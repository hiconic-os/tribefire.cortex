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
package com.braintribe.web.servlet;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * Extension of the standard VelocityContext class that keeps additional information
 * on which template should be used for this context. This allows servlets
 * to have greater flexibility.
 */
public class TypedVelocityContext extends VelocityContext {

	private static final long serialVersionUID = -3588470424191442735L;

	private String type = BasicTemplateBasedServlet.DEFAULT_TEMPLATE_KEY;
	
	public TypedVelocityContext() {
		super();
	}

	public TypedVelocityContext(Context innerContext) {
		super(innerContext);
	}

	public TypedVelocityContext(Map<String,Object> context, Context innerContext) {
		super(context, innerContext);
	}

	public TypedVelocityContext(Map<String,Object> context) {
		super(context);
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TypedVelocityContext with type ");
		sb.append(type);
		String[] keys = super.internalGetKeys();
		if (keys != null && keys.length > 0) {
			for (String key : keys) {
				Object value = super.internalGet(key);
				if (value != null) {
					sb.append('\n');
					sb.append(key);
					sb.append('=');
					sb.append(value);
				}
			}
		}
		return sb.toString();
	}
	
}
