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
package com.braintribe.logging.listener;

import com.braintribe.logging.Logger;
import com.braintribe.logging.Logger.LogLevel;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.bootstrapping.listener.RuntimePropertyChangeListener;

/**
 * Implementation of the {@link RuntimePropertyChangeListener} interface. It specifically listens for changes of the
 * logging level in the TribefireRuntime properties and changes the log level accordingly.
 */
public class LoggingRuntimeListener implements RuntimePropertyChangeListener {

	private static Logger logger = Logger.getLogger(LoggingRuntimeListener.class);

	protected static LoggingRuntimeListener listener = null;

	public static void register() {
		if (listener != null) {
			return;
		}

		listener = new LoggingRuntimeListener();

		LogLevel logLevel = listener.getLogLevel();
		if (logLevel != null) {
			TribefireRuntime.setProperty(TribefireRuntime.ENVIRONMENT_LOG_LEVEL, logLevel.name());
		}

		TribefireRuntime.addPropertyChangeListener(TribefireRuntime.ENVIRONMENT_LOG_LEVEL, listener);
	}

	public static void unregister() {
		if (listener == null) {
			return;
		}
		TribefireRuntime.removePropertyChangeListener(TribefireRuntime.ENVIRONMENT_LOG_LEVEL, listener);
		TribefireRuntime.setProperty(TribefireRuntime.ENVIRONMENT_LOG_LEVEL, null);
		listener = null;
	}

	protected LogLevel getLogLevel() {
		if (logger.isTraceEnabled()) {
			return LogLevel.TRACE;
		} else if (logger.isDebugEnabled()) {
			return LogLevel.DEBUG;
		} else if (logger.isInfoEnabled()) {
			return LogLevel.INFO;
		} else if (logger.isWarnEnabled()) {
			return LogLevel.WARN;
		} else if (logger.isErrorEnabled()) {
			return LogLevel.ERROR;
		} else {
			return null;
		}
	}

	protected LogLevel translateLogLevel(String levelName) {
		if (levelName == null) {
			return null;
		}
		levelName = levelName.trim().toUpperCase();
		switch (levelName) {
			case "TRACE":
			case "FINEST":
			case "FINER":
				return LogLevel.TRACE;
			case "DEBUG":
			case "FINE":
				return LogLevel.DEBUG;
			case "INFO":
				return LogLevel.INFO;
			case "WARN":
			case "WARNING":
				return LogLevel.WARN;
			case "ERROR":
			case "SEVERE":
				return LogLevel.ERROR;
			default:
				return null;
		}
	}

	@Override
	public void propertyChanged(String propertyName, String oldValue, String newValue) {
		if (propertyName == null || newValue == null) {
			return;
		}

		if (propertyName.equals(TribefireRuntime.ENVIRONMENT_LOG_LEVEL)) {
			logger.info(() -> "Received log level change request to " + newValue + " on logger " + logger);

			LogLevel logLevel = this.translateLogLevel(newValue);
			if (logLevel != null) {
				if (logLevel.equals(getLogLevel()) == false) {
					logger.setLogLevel(logLevel);

					logger.info(() -> "Log level has been changed to " + logLevel.name());
				} else if (logger.isDebugEnabled()) {
					logger.info(() -> "Log level is already " + logLevel.name());
				}
			} else if (logger.isDebugEnabled()) {
				logger.info(() -> "Invalid log level " + newValue);
			}
		}
	}
}
