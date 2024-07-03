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
package tribefire.platform.impl.resource;

import java.io.File;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.codec.marshaller.api.GmSerializationOptions;
import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.api.OutputPrettiness;
import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.provider.Hub;
import com.braintribe.utils.FileTools;

import tribefire.platform.api.resource.ResourcesBuilder;

/**
 * @author peter.gazdik
 */
public class ResourcesBuilderBasedPersistence<T> implements Hub<T> {

	private Supplier<ResourcesBuilder> storageResourcesBuilderFactory;
	private Marshaller marshaller;
	private GmSerializationOptions serializationOptions = GmSerializationOptions.defaultOptions.derive().outputPrettiness(OutputPrettiness.high).build();
	private String descriptor = "";

	@Required
	public void setStorageResourcesBuilderFactory(Supplier<ResourcesBuilder> storageResourcesBuilderFactory) {
		this.storageResourcesBuilderFactory = storageResourcesBuilderFactory;
	}

	@Required
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	@Configurable
	public void setSerializationOptions(GmSerializationOptions serializationOptions) {
		this.serializationOptions = serializationOptions;
	}

	@Configurable
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public T get() {
		return storageResourcesBuilderFactory.get().asAssembly(marshaller, null);
	}

	@Override
	public void accept(T value) {
		File file = storageResourcesBuilderFactory.get().asFile();

		try {
			FileTools.write(file).usingOutputStream(os -> marshaller.marshall(os, value, serializationOptions));

		} catch (Exception e) {
			throw new GenericModelException("Storing " + descriptor + " failed.", e);
		}
	}

}
