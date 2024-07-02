// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.cortex.testing.junit.classpathfinder;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class provides an iterator over all file names in a jar file. Directories are not considered to be files.
 */
public class JarFilenameIterator implements Iterator<String>, Iterable<String> {

	private final Enumeration<JarEntry> entries;

	private JarEntry next;

	private final JarFile jar;

	public JarFilenameIterator(File jarFile) throws IOException {
		jar = new JarFile(jarFile);
		entries = jar.entries();
		retrieveNextElement();
	}

	private void retrieveNextElement() {
		next = null;
		while (entries.hasMoreElements()) {
			next = entries.nextElement();
			if (!next.isDirectory()) {
				break;
			}
		}
		if (!hasNext()) {
			try {
				jar.close();
			} catch (IOException e) {
				throw new RuntimeException("Cannot close jar: " + jar.getName(), e);
			}
		}
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public String next() {
		if (next == null) {
			throw new NoSuchElementException();
		}
		String value = next.getName();
		retrieveNextElement();
		return value;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

}