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
package com.braintribe.testing.internal.tribefire.helpers;

import com.braintribe.logging.Logger;

public class CommonModels {
	private static Logger logger = Logger.getLogger(CommonModels.class);

	public final static String ROOT_MODEL_NAME = braintribeModelName("RootModel");
	public final static String FOLDER_MODEL_NAME = braintribeModelName("FolderModel");
	public final static String USER_MODEL_NAME = braintribeModelName("UserModel");
	
	private static String braintribeModelName(String name) {
		return "com.braintribe.model:" + name;
	}
}
