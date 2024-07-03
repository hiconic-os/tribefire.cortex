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
package tribefire.cortex.testing.junit.classpathfinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class provides an iterator over all file names in a directory and its subdirectories. The filenames are given
 * relative to the root. Directories are not considered to be files.
 */
public class RecursiveFilenameIterator implements Iterator<String>, Iterable<String> {

	private List<RecursiveFilenameIterator> innerIterators;

	private int prefixLength;

	private File root;

	private boolean alreadyUsed = false;

	private int index = 0;

	public RecursiveFilenameIterator(File root) {
		this(root, root.getAbsolutePath().length() + 1);
	}

	private RecursiveFilenameIterator(File root, int prefixLength) {
		this.root = root;
		this.prefixLength = prefixLength;
		if (!isRootFile()) {
			innerIterators = getInnerIterators(root);
		}
	}

	private boolean isRootFile() {
		return this.root.isFile();
	}

	private List<RecursiveFilenameIterator> getInnerIterators(File root) {
		List<RecursiveFilenameIterator> iterators = new ArrayList<RecursiveFilenameIterator>();
		for (File each : root.listFiles()) {
			iterators.add(new RecursiveFilenameIterator(each, prefixLength));
		}
		return iterators;
	}

	@Override
	public boolean hasNext() {
		if (isRootFile()) {
			return !alreadyUsed;
		}
		if (index >= innerIterators.size()) {
			return false;
		}
		if (currentIterator().hasNext()) {
			return true;
		}
		index++;
		return hasNext();
	}

	private RecursiveFilenameIterator currentIterator() {
		return innerIterators.get(index);
	}

	@Override
	public String next() {
		if (isRootFile()) {
			if (alreadyUsed) {
				throw new NoSuchElementException();
			}
			alreadyUsed = true;
			return root.getAbsolutePath().substring(prefixLength);
		}
		if (hasNext()) {
			return currentIterator().next();
		}
		throw new NoSuchElementException();
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