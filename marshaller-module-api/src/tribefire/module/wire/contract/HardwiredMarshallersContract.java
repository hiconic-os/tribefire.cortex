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
package tribefire.module.wire.contract;

import java.util.function.Supplier;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.model.marshallerdeployment.HardwiredMarshaller;

/**
 * Offers methods for binding {@link Marshaller}s.
 * 
 * @see HardwiredDeployablesContract
 */
public interface HardwiredMarshallersContract extends HardwiredDeployablesContract {

	default HardwiredMarshaller bindMarshaller(String externalId, String name, Marshaller marshaller, String... mimeTypes) {
		return bindMarshaller(externalId, name, () -> marshaller, mimeTypes);
	}

	/** Recommended externalId convention: marshaller.${type} (e.g. marshaller.xml) */
	HardwiredMarshaller bindMarshaller(String externalId, String name, Supplier<Marshaller> marshaller, String... mimeTypes);

}
