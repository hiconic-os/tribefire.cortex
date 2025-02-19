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
package com.braintribe.web.repository.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Defines a writer of Repolet responses
 * 
 * @param <T>
 */
public interface RepoletWriter<T extends Writer> {

	/**
	 * Writes to the given {@code writer} an enumeration of {@code entries} available in the given {@code path}
	 * @param path
	 * @param breadCrumbs
	 * @param entries
	 * @param writer
	 * @throws IOException
	 */
	void writeList(String path, Collection<BreadCrumb> breadCrumbs, Collection<String> entries, T writer, Map<String, Object> attributes) throws IOException;

	/**
	 * Writes to the given {@code writer} a 404 page for the {@code path} that was not found 
	 * @param path
	 * @param printInspectedPaths
	 * @param inspectedPaths
	 * @param writer
	 * @throws IOException
	 */
	void writeNotFound(String path, boolean printInspectedPaths, Collection<String> inspectedPaths, T writer, Map<String, Object> attributes) throws IOException;
	 
}
