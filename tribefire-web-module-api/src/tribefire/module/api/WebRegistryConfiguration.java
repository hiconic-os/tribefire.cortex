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
package tribefire.module.api;

import javax.servlet.ServletRequest;

import com.braintribe.thread.impl.ThreadLocalStack;
import com.braintribe.web.api.registry.ConfigurableWebRegistry;
import com.braintribe.web.api.registry.FilterConfiguration;

public interface WebRegistryConfiguration extends ConfigurableWebRegistry {

	/**
	 * This filter tries to authenticate the current thread (via a {@link ThreadLocalStack}) and HttpRequest (via an {@link ServletRequest#setAttribute(String, Object) Attribute}) but just continues the filter chain unauthenticated if it fails.
	 */
	FilterConfiguration lenientAuthFilter();
	
	/**
	 * This filter requires to authenticate the current thread (via a {@link ThreadLocalStack}) and HttpRequest (via an {@link ServletRequest#setAttribute(String, Object) Attribute}).
	 */
	FilterConfiguration strictAuthFilter();
	
	/**
	 * This filter tries to authenticate the current thread (via a {@link ThreadLocalStack}) and HttpRequest (via an {@link ServletRequest#setAttribute(String, Object) Attribute}) and redirects to the login page if it fails.
	 */
	FilterConfiguration loginRedirectingAuthFilter();
	
	/**
	 *	This filter enables compression for a servlet request 
	 */
	FilterConfiguration compressionFilter();
	
	/**
	 *	This filter names the thread after the path info from a servlet request 
	 */
	FilterConfiguration threadRenamingFilter();
}
