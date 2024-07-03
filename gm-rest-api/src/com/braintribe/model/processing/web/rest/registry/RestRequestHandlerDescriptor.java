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
package com.braintribe.model.processing.web.rest.registry;

import java.util.Set;

import com.braintribe.model.processing.web.rest.RestRequestHandler;
import com.braintribe.model.rest.RequestMethod;
import com.braintribe.model.rest.RestRequest;

/**
 * Defines the structure for a {@link RestRequestHandlerRegistry} entry.
 * @param <T>
 */
public interface RestRequestHandlerDescriptor<T extends RestRequest> {

	/**
	 * Gets the REST request URL path.
	 * @return
	 */
	String getUrlPath();
	

	/**
	 * Gets a {@link Set} of {@link RequestMethod}(s) that should be supported by the given URL path.  
	 * @return
	 */
	Set<RequestMethod> getSupportedMethods();
	

	/**
	 * Gets the {@link RestRequestHandler} configured to the given URL path.
	 * @return
	 */
	RestRequestHandler<T> getRestRequestHandler();
	

	/**
	 * Gets the {@link RestRequest} entity type to be created from the request parameters.
	 * @return
	 */
	Class<T> getRestRequestType();
	
	
	/**
	 * Gets the authorization context type.
	 * @return The authorization context type.
	 */
	AuthorizationContextType getAuthorizationContext();
	
	/**
	 * returns the default assembly which is there to hold default values that should be used when no value was given explicitly
	 * @return
	 */
	T getDefaultRequest();
}
