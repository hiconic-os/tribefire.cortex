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
package com.braintribe.tribefire.jdbc;

/**
 * The Class TfJdbcException.
 *
 */
public class TfJdbcException extends RuntimeException {

	/**
	 * Instantiates a new tf jdbc exception.
	 *
	 * @param string
	 *            the string
	 * @param ex
	 *            the ex
	 */
	public TfJdbcException(String string, Exception ex) {
		super(string, ex);
	}
	
	/**
	 * Instantiates a new tf jdbc exception.
	 *
	 * @param ex
	 *            the ex
	 */
	public TfJdbcException(Exception ex) {
		super(ex);
	}

	/**
	 * Instantiates a new tf jdbc exception.
	 *
	 * @param string
	 *            the string
	 */
	public TfJdbcException(String string) {
		super(string);
	}
	
	private static final long serialVersionUID = 6324046887125100303L;

}
