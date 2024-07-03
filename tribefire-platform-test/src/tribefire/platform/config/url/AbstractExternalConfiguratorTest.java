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
package tribefire.platform.config.url;

import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.braintribe.cartridge.common.processing.configuration.url.model.RegistryEntry;
import com.braintribe.config.configurator.ConfiguratorException;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.meta.GmMetaModel;

import tribefire.platform.impl.configuration.EnvironmentDenotationRegistry;

public class AbstractExternalConfiguratorTest {

	private static final String json = "[{\"_type\":\"com.braintribe.cartridge.common.processing.configuration.url.model.RegistryEntry\",\"bindId\":\"model\",\"denotation\":{\"_type\":\"com.braintribe.model.meta.GmMetaModel\",\"name\":\"TestModel\"}}]";

	@Test
	public void testReadFromInputStream() throws Exception {
		
		AbstractExternalConfigurator configurator = new AbstractExternalConfigurator() {
			// @formatter:off
			@Override protected List<RegistryEntry> getEntries() { return null; }
			@Override protected String getSourceInformation() { return null; }
			// @formatter:on
		};
		List<RegistryEntry> list = configurator.readConfigurationFromInputStream(new StringReader(json));
		
		assertThat(list).isNotNull().hasSize(1);
		assertThat(list.get(0)).isInstanceOf(RegistryEntry.class);
		assertThat(list.get(0).getBindId()).isEqualTo("model");
	}
	
	@Test
	public void testRegistration() throws Exception {
		AbstractExternalConfigurator configurator = new AbstractExternalConfigurator() {
			@Override
			protected List<RegistryEntry> getEntries() throws ConfiguratorException {
				try (StringReader reader = new StringReader(json)) {
					return super.readConfigurationFromInputStream(reader);
				} catch(Exception e) {
					throw new ConfiguratorException("Error while trying to read value "+json, e);
				}
			}

			@Override
			protected String getSourceInformation() {
				return "Test";
			}
			
		};
		configurator.configure();
		
		GenericEntity entity = EnvironmentDenotationRegistry.getInstance().lookup("model");
		assertThat(entity).isNotNull().isInstanceOf(GmMetaModel.class);
		assertThat(((GmMetaModel) entity).getName()).isEqualTo("TestModel");
	}
}
