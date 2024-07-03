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
package tribefire.module.api;

import java.io.File;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Provides methods for obtaining different representations of a resource reference, including ways of obtaining the
 * resource contents.
 */
public interface ResourceHandle {

	/**
	 * <p>
	 * Returns the resolved path as {@link URL}.
	 * 
	 * @return The resolved path as {@link URL}.
	 * @throws UncheckedIOException
	 *             If the resolved path cannot be represented as {@link URL}.
	 */
	URL asUrl() throws UncheckedIOException;

	/**
	 * <p>
	 * Returns the resolved path as {@link Path}.
	 * 
	 * @return The resolved path as {@link Path}.
	 * @throws UncheckedIOException
	 *             If the resolved path cannot be represented as {@link Path}.
	 */
	Path asPath() throws UncheckedIOException;

	/**
	 * <p>
	 * Returns the resolved path as {@link File}.
	 * 
	 * @return The resolved path as {@link File}.
	 * @throws UncheckedIOException
	 *             If the resolved path cannot be represented as {@link File}.
	 */
	File asFile() throws UncheckedIOException;

	/**
	 * <p>
	 * Returns the resource contents as String.
	 * 
	 * @param encoding
	 *            The encoding used for reading the resource contents.
	 * @return The resource contents as String.
	 * @throws UncheckedIOException
	 *             In case of IOException(s) while reading the resource contents.
	 */
	String asString(String encoding) throws UncheckedIOException;

	/**
	 * <p>
	 * Returns an {@link InputStream} for reading the resource contents.
	 * 
	 * @return An {@link InputStream} for reading the resource contents.
	 * @throws UncheckedIOException
	 *             In case of IOException(s) while obtaining the {@link InputStream}.
	 */
	InputStream asStream() throws UncheckedIOException;

	/**
	 * <p>
	 * Returns a {@link Properties} instance as loaded from the resource.
	 * 
	 * <p>
	 * The resource compatibility for this operation is described in the {@link Properties#load(InputStream)} method
	 * documentation.
	 * 
	 * @return A {@link Properties} instance as loaded from the resource.
	 * @throws UncheckedIOException
	 *             In case of IOException(s) while reading the resource or loading it as a {@link Properties} object.
	 */
	Properties asProperties() throws UncheckedIOException;

}
