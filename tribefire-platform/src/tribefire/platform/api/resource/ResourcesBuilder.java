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
package tribefire.platform.api.resource;

import java.io.UncheckedIOException;

import com.braintribe.codec.marshaller.api.MarshallException;
import com.braintribe.codec.marshaller.api.Marshaller;

import tribefire.module.api.ResourceHandle;

/**
 * @see ResourceHandle
 */
public interface ResourcesBuilder extends ResourceHandle {

	/**
	 * <p>
	 * Returns an unmarshalled object from the resource contents.
	 * 
	 * <p>
	 * Assumes the resource is a marshalled representation of a object compatible with the given {@link Marshaller}
	 * instance.
	 * 
	 * @param marshaller
	 *            The {@link Marshaller} to be used for unmarshalling the resource.
	 * @return An unmarshalled object from the resource contents.
	 * @throws UncheckedIOException
	 *             In case of IOException(s) while reading the resource contents.
	 * @throws MarshallException
	 *             Upon failures while unmarshalling the resource.
	 */
	<T> T asAssembly(Marshaller marshaller) throws UncheckedIOException, MarshallException;

	/**
	 * Similar to {@link #asAssembly(Marshaller)}, but returns the default value if the underlying resource (e.g. File)
	 * does not exist.
	 */
	<T> T asAssembly(Marshaller marshaller, T defaultValue);

}
