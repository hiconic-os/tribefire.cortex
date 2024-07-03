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
package com.braintribe.model.processing.web.rest;

import com.braintribe.model.rest.RestRequest;

/**
 * Handler of REST requests.
 * @param <T>
 */
public interface RestRequestHandler<T extends RestRequest> {
	
	/**
	 * Handles REST requests.
	 * @param restRequest
	 * @param restRequestContext
	 */
	void handleRequest(T restRequest, RestRequestContext restRequestContext) throws RestRequestException;
	
	/*
	Set<String> getRequiredParameters() throws RestRequestException;
	
	Set<String> getSupportedParameters() throws RestRequestException;
	
	Set<String> getSupportedProjections() throws RestRequestException; 
	*/

}
