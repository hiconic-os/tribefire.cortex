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
package com.braintribe.model.processing.check.utils;

public class ExceptionUtil {

	private static final String EXCEPTION_SUFIX = "Exception:";
	
	private ExceptionUtil() {
		//private constructor to ensure only static usage of this class.
	}
	
	public static String getLastMessage(Throwable throwable) {
		if (throwable == null)
			return null;
		
		String message = throwable.getMessage();
		while ((throwable = throwable.getCause()) != null) {
			if (throwable.getMessage() != null) {
				message = throwable.getMessage();
			}
		}
		
		if (message != null) {
			int index = message.indexOf(EXCEPTION_SUFIX);
			if (index != -1) {
				String initialPart = message.substring(0, index);
				if (!initialPart.contains(" ") && initialPart.length() > EXCEPTION_SUFIX.length()) {
					message = message.substring(index + EXCEPTION_SUFIX.length()).trim();
				}
			}
		}
		
		return message;
	}
}
