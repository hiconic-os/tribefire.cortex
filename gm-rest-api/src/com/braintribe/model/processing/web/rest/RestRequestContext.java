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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.rest.ParameterValue;
import com.braintribe.model.rest.RestRequest;
import com.braintribe.model.workbench.WorkbenchAction;

/**
 * Context of a REST request
 */
public interface RestRequestContext {
	
	/**
	 * Retrieves the business/data {@link PersistenceGmSession} associated with the REST request context
	 * @return
	 */
	PersistenceGmSession getSession() throws RestRequestException;
	
	/**
	 * Retrieves the workbench {@link PersistenceGmSession} associated with the REST request context
	 * @return
	 */
	PersistenceGmSession getWorkbenchSession() throws RestRequestException;
	
	/**
	 * Retrieves the {@link HttpServletRequest} associated with the REST request context
	 * @return
	 */
	HttpServletRequest getHttpServletRequest();
	
	/**
	 * Retrieves the {@link HttpServletResponse} associated with the REST request context
	 * @return
	 */
	HttpServletResponse getHttpServletResponse();

	/**
	 * return the normalized list on parameter value no matter if simple or multi
	 * @param value
	 * @return
	 */
	List<String> getValues(ParameterValue value);
	
	/**
	 * return the normalized first parameter value no matter if simple or multi
	 * @param value
	 * @return
	 */
	String getFirstValue(ParameterValue value);
	
	/**
	 * handlers can write their results conveniently with that method
	 * @param assembly
	 * @throws RestRequestException
	 */
	void writeResponse(Object assembly) throws RestRequestException;
	
	/**
	 * returns a TraversingCriterion configured for the given name
	 * @param name
	 * @return
	 */
	TraversingCriterion getNamedTraversingCriterion(String name);
	
	/**
	 * returns a WorkbenchAction configured for the given name
	 * @param name
	 * @return
	 */
	WorkbenchAction getNamedWorkbenchAction(String name);

	/**
	 * returns the optional default request which hosts default values that are being used when no explicit value was given in the url or form
	 * @return
	 */
	<R extends RestRequest> R getDefaultRequest();
	
	/**
	 * returns the Internet protocol address associated with the request.
	 */
	String getInternetAddress();
	
}
