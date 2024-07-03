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
package tribefire.platform.wire.space.module;

import com.braintribe.codec.marshaller.api.CharacterMarshaller;
import com.braintribe.codec.marshaller.api.ConfigurableMarshallerRegistry;
import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.WebPlatformMarshallingContract;
import tribefire.platform.wire.space.common.MarshallingSpace;

/**
 * @author peter.gazdik
 */
@Managed
public class WebPlatformMarshallingSpace implements WebPlatformMarshallingContract {

	@Import
	private MarshallingSpace marshalling;

	@Override
	public ConfigurableMarshallerRegistry registry() {
		return marshalling.registry();
	}

	@Override
	public Marshaller binMarshaller() {
		return marshalling.binMarshaller();
	}

	@Override
	public CharacterMarshaller xmlMarshaller() {
		return marshalling.xmlMarshaller();
	}

	@Override
	public CharacterMarshaller jsonMarshaller() {
		return marshalling.jsonMarshaller();
	}

	@Override
	public CharacterMarshaller manMarshaller() {
		return marshalling.manMarshaller();
	}

	@Override
	public CharacterMarshaller yamlMarshaller() {
		return marshalling.yamlMarshaller();
	}

}
