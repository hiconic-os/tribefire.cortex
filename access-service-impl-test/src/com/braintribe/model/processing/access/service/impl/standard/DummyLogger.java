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
package com.braintribe.model.processing.access.service.impl.standard;

import com.braintribe.logging.Logger;

/**
 * The {@link DummyLogger} is enabled for all log levels and swallows log messages.
 * 
 * 
 */
public class DummyLogger extends Logger {

	public DummyLogger(@SuppressWarnings("unused") Class<?> cls) {
		// nothing to do
	}

	private static boolean isTraceEnabled = true;

	@Override
	public boolean isTraceEnabled() {
		return isTraceEnabled;
	}
	
	@Override 
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public void trace(String msg) {
		// swallowing log messages
	}

	@Override
	public void trace(Throwable t) {
		// swallowing log messages
	}

	@Override
	public void trace(String msg, Throwable t) {
		// swallowing log messages
	}

	@Override
	public void debug(String msg) {
		// swallowing log messages
	}

	@Override
	public void debug(Throwable t) {
		// swallowing log messages
	}

	@Override
	public void debug(String msg, Throwable t) {
		// swallowing log messages
	}

	@Override
	public void error(String msg) {
		// swallowing log messages
	}

	@Override
	public void info(String msg) {
		// swallowing log messages
	}

	@Override
	public void info(Throwable t) {
		// swallowing log messages
	}

	@Override
	public void info(String msg, Throwable t) {
		// swallowing log messages
	}

	@Override
	public void warn(String msg) {
		// swallowing log messages
	}

	@Override
	public void warn(Throwable ex) {
		// swallowing log messages
	}

	@Override
	public void warn(String msg, Throwable ex) {
		// swallowing log messages
	}

	@Override
	public void error(String msg, Throwable ex) {
		// swallowing log messages
	}

	public static void setTraceEnabled(boolean isTraceEnabled) {
		DummyLogger.isTraceEnabled = isTraceEnabled;
	}

	@Override
	public void log(final LogLevel logLevel, final String msg) {
		// NO OP
	}

	@Override
	public void log(final LogLevel logLevel, final String msg, final Throwable ex) {
		// NO OP
	}

	@Override
	public void pushContext(String context) {
		// NO OP
	}
	@Override
	public void popContext() {
		// NO OP
	}
	@Override
	public void removeContext() {
		// NO OP
	}

}
