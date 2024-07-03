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
package com.braintribe.model.processing.tfconstants;

import com.braintribe.common.lcd.UnknownEnumException;

/**
 * Provides tribefire related constants.
 *
 * @author michael.lafite
 */
public class TribefireConstants {

	public static final String TRIBEFIRE_SERVICES_APPLICATION_ID = "master";

	public static final String TRIBEFIRE_JVM_UUID_KEY = "TRIBEFIRE_JVM_UUID";

	public static final String DEFAULT_TF_COMPONENT_URL_NAME_PREFIX = "tribefire-";
	public static final String DEFAULT_TF_CONTROLCENTER_URL_NAME = DEFAULT_TF_COMPONENT_URL_NAME_PREFIX + "control-center";
	public static final String DEFAULT_TF_EXPLORER_URL_NAME = DEFAULT_TF_COMPONENT_URL_NAME_PREFIX + "explorer";
	public static final String DEFAULT_TF_REPOSITORY_URL_NAME = DEFAULT_TF_COMPONENT_URL_NAME_PREFIX + "repository";
	public static final String DEFAULT_TF_SERVICESE_URL_NAME = DEFAULT_TF_COMPONENT_URL_NAME_PREFIX + "services";

	public static final int DEFAULT_HTTP_PORT = 8080;
	public static final int DEFAULT_HTTPS_PORT = 8443;

	public static final String USER_CORTEX_NAME = "cortex";
	public static final String USER_CORTEX_DEFAULT_PASSWORD = "cortex";

	public static final String ACCESS_CORTEX = "cortex";

	public static final String ACCESS_AUTH = "auth";
	public static final String ACCESS_AUTH_NAME = "Authentication and Authorization";
	public static final String ACCESS_MODEL_AUTH = "tribefire.cortex.services:tribefire-user-model";
	public static final String ACCESS_MODEL_AUTH_WB = "tribefire.cortex.services:tribefire-user-workbench-model";

	public static final String ACCESS_USER_SESSIONS = "user-sessions";
	public static final String ACCESS_USER_SESSIONS_NAME = "User Sessions";
	public static final String ACCESS_MODEL_USER_SESSIONS = "tribefire.cortex.services:tribefire-user-session-model";
	public static final String ACCESS_SERVICE_MODEL_USER_SESSIONS = "tribefire.cortex.services:tribefire-user-session-service-model";
	public static final String ACCESS_MODEL_USER_SESSIONS_WB = "tribefire.cortex.services:tribefire-user-session-workbench-model";

	public static final String ACCESS_USER_STATISTICS = "user-statistics";
	public static final String ACCESS_USER_STATISTICS_NAME = "User Statistics";
	public static final String ACCESS_MODEL_USER_STATISTICS = "tribefire.cortex.services:tribefire-user-statistics-model";
	public static final String ACCESS_MODEL_USER_STATISTICS_WB = "tribefire.cortex.services:tribefire-user-statistics-workbench-model";

	public static final String ACCESS_SETUP = "setup";
	public static final String ACCESS_MODEL_PLATFORM_SETUP = "tribefire.cortex.services:tribefire-platform-setup-model";
	public static final String ACCESS_MODEL_PLATFORM_SETUP_WB = "tribefire.cortex.services:tribefire-platform-setup-workbench-model";

	public static final String ACCESS_TRANSIENT_MESSAGING_DATA = "transient-messaging-data";
	public static final String ACCESS_TRANSIENT_MESSAGING_DATA_NAME = "Transient Messaging";
	public static final String ACCESS_MODEL_TRANSIENT_MESSAGING_DATA = "tribefire.cortex.services:tribefire-transient-messaging-data-model";
	public static final String ACCESS_MODEL_TRANSIENT_MESSAGING_DATA_WB = "tribefire.cortex.services:tribefire-transient-messaging-data-workbench-model";

	public static final String TF_WB_MODEL = "tribefire.cortex.services:tribefire-workbench-model";

	public static String getComponentUrlName(final TribefireComponent component) {
		switch (component) {
			case ControlCenter:
				return DEFAULT_TF_CONTROLCENTER_URL_NAME;
			case Explorer:
				return DEFAULT_TF_EXPLORER_URL_NAME;
			case Repository:
				return DEFAULT_TF_REPOSITORY_URL_NAME;
			case Services:
				return DEFAULT_TF_SERVICESE_URL_NAME;
			case Host:
				return "";
			default:
				throw new UnknownEnumException(component);
		}
	}
}
