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

public class RestRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4510106683044095657L;
	
	private int restHttpStatusCode = 500;

	public RestRequestException() {
	}

	public RestRequestException(String message) {
		super(message);
	}

	public RestRequestException(Throwable cause) {
		super(cause);
	}

	public RestRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestRequestException(Throwable cause, int restHttpStatusCode) {
		this(cause);
		setRestHttpStatusCode(restHttpStatusCode);
	}

	public RestRequestException(String message, int restHttpStatusCode) {
		this(message);
		setRestHttpStatusCode(restHttpStatusCode);
	}

	public RestRequestException(String message, Throwable cause, int restHttpStatusCode) {
		this(message, cause);
		setRestHttpStatusCode(restHttpStatusCode);
	}

	public int getRestHttpStatusCode() {
		return restHttpStatusCode;
	}

	public void setRestHttpStatusCode(int restHttpStatusCode) {
		this.restHttpStatusCode = restHttpStatusCode;
	}

}
