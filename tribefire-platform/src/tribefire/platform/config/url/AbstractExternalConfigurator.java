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

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.braintribe.cartridge.common.processing.configuration.url.model.RegistryEntry;
import com.braintribe.cfg.Configurable;
import com.braintribe.codec.marshaller.api.CharacterMarshaller;
import com.braintribe.codec.marshaller.api.EntityFactory;
import com.braintribe.codec.marshaller.api.GmDeserializationOptions;
import com.braintribe.codec.marshaller.api.PlaceholderSupport;
import com.braintribe.codec.marshaller.api.options.GmDeserializationContextBuilder;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.common.lcd.Numbers;
import com.braintribe.config.configurator.Configurator;
import com.braintribe.config.configurator.ConfiguratorContext;
import com.braintribe.config.configurator.ConfiguratorException;
import com.braintribe.config.configurator.ContextAware;
import com.braintribe.exception.Exceptions;
import com.braintribe.gm.config.ConfigVariableResolver;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.stream.TeeReader;

import tribefire.platform.impl.configuration.EnvironmentDenotationRegistry;
import tribefire.platform.impl.security.EntityDecryption;
import tribefire.platform.impl.security.EntityObfuscation;

/**
 * An abstract {@link Configurator} that gets a list of deployable of a sub-class and registers them with the {@link EnvironmentDenotationRegistry}. A
 * sub-class of this class is likely to be configured as a Configurator that is executed during the startup of a Cartridge and the TF services.
 */
public abstract class AbstractExternalConfigurator implements Configurator, ContextAware {

	private static final Logger log = Logger.getLogger(AbstractExternalConfigurator.class);

	protected CharacterMarshaller jsonMarshaller;
	protected CharacterMarshaller yamlMarshaller = new YamlMarshaller();
	protected ConfiguratorContext context;

	public AbstractExternalConfigurator() {
		JsonStreamMarshaller marshaller = new JsonStreamMarshaller();
		marshaller.setUseBufferingDecoder(true);
		this.jsonMarshaller = marshaller;
	}

	@Configurable
	public void setMarshaller(CharacterMarshaller marshaller) {
		this.jsonMarshaller = marshaller;
	}

	@Override
	public void setContext(ConfiguratorContext context) {
		this.context = context;
	}

	@Override
	public void configure() throws ConfiguratorException {

		// Get a list of entries from an implementing subclass. The result might be null.
		List<RegistryEntry> entries = getEntries();

		if (entries != null && !entries.isEmpty()) {
			List<RegistryEntry> filteredEntries = entries.stream().filter(e -> e != null && e.getBindId() != null && e.getDenotation() != null)
					.toList();
			filteredEntries.parallelStream().forEach(entry -> {
				try {
					GenericEntity registeredGenericEntity = entry.getDenotation();

					EntityObfuscation.deobfuscateProperties(registeredGenericEntity);
					EntityDecryption.decryptProperties(registeredGenericEntity);

				} catch (Exception e) {
					e = Exceptions.contextualize(e, "Entry: " + entry + ", Source: " + entry.getSource());
					if (e instanceof ConfiguratorException) {
						throw ((ConfiguratorException) e);
					} else {
						throw Exceptions.unchecked(e);
					}
				}
			});
			filteredEntries.forEach(entry -> EnvironmentDenotationRegistry.getInstance().register(entry.getBindId(), entry.getDenotation()));
		}
	}

	/**
	 * Returns a list of entries gathered by any means by a subclass. A subclass may use the helper method
	 * {@link #readConfigurationFromInputStream(Reader)} to read entries from an {@link InputStream}.
	 * 
	 * @return A list of RegistryEntry elements or null if no entries were found.
	 * @throws ConfiguratorException
	 *             Thrown if an error occurred during the loading of entries.
	 */
	protected abstract List<RegistryEntry> getEntries() throws ConfiguratorException;

	/**
	 * Textual information about the source of the entries. This information is intended to provide a meaningful information in log files. This
	 * information is NOT intended to server as a source URL or file path as it will only be used for logging.
	 * 
	 * @return Any information deemed important by the subclass to identify the source of entries.
	 */
	protected abstract String getSourceInformation();

	/**
	 * Helper method to load entries from a {@link Reader}. This method will not throw any exception but will only log errors/warnings to the logging
	 * framework.
	 * 
	 * @param reader
	 *            The input stream that provides entries in JSON format (or any other format if an alternative marshaller is set).
	 * @return A list of entries or null, if no were found or an error occurred.
	 */
	protected List<RegistryEntry> readConfigurationFromInputStream(Reader reader) {
		return readConfigurationFromInputStream(reader, null);
	}
	
	/**
	 * Helper method to load entries from a {@link Reader}. This method will not throw any exception but will only log errors/warnings to the logging
	 * framework.
	 * 
	 * @param reader
	 *            The input stream that provides entries in JSON format (or any other format if an alternative marshaller is set).
	 * @param filename the filename used as a hint to select a marshaller (*.json, *.yaml) or null which implies json
	 * @return A list of entries or null, if no were found or an error occurred.
	 */
	protected List<RegistryEntry> readConfigurationFromInputStream(Reader reader, String filename) {
		List<RegistryEntry> entries = new ArrayList<RegistryEntry>();
		try {

			TeeReader teeReader = new TeeReader(reader, Numbers.MILLION);

			boolean placeholderSupport = Boolean.TRUE.toString().equals(
					TribefireRuntime.getProperty("CX_EXTERNAL_CONFIG_PLACEHOLDER_SUPPORT"));
			
				GmDeserializationContextBuilder optionsBuilder = GmDeserializationOptions.deriveDefaults()
					.set(EntityFactory.class, EntityType::create)
					.set(PlaceholderSupport.class, placeholderSupport);

			CharacterMarshaller effectiveMarshaller = selectMarshallerAndOptions(filename, optionsBuilder);

			GmDeserializationOptions options = optionsBuilder.build();
			Object result = effectiveMarshaller.unmarshall(teeReader, options);

			if (placeholderSupport)
				result = new ConfigVariableResolver().resolvePlaceholders(result).get();
			
			if (result instanceof RegistryEntry) {
				entries.add((RegistryEntry) result);
			} else if (result instanceof List<?>) {
				for (Object entry : ((List<?>) result)) {
					if (entry instanceof RegistryEntry) {
						entries.add((RegistryEntry) entry);
					} else {
						log.warn("Unsupported registry entry of type: " + entry + " found. Ignore.");
					}
				}
			} else {
				String resultClassname = result != null ? result.getClass().getName() : "null";
				log.warn("Unsupported configuration content: " + result + ". Expecting entries of type: " + RegistryEntry.class.getName()
						+ " but got " + resultClassname + ". The original content was: " + teeReader.getBuffer() + ". Ignore.");
			}

			if (entries.size() > 0) {
				log.info("Successfully read configuration from: " + getSourceInformation() + " which returned: " + entries.size()
						+ " denotations to register.");
			}
			return entries;

		} catch (Exception e) {
			log.error("Could not read configuration located at: " + getSourceInformation(), e);
		}

		return entries;

	}

	private CharacterMarshaller selectMarshallerAndOptions(String filename, GmDeserializationContextBuilder optionsBuilder) {
		String extension = filename != null? FileTools.getExtension(filename).toLowerCase(): "json";
		
		switch (extension) {
			case "yml":
			case "yaml":
				optionsBuilder.setInferredRootType(GMF.getTypeReflection().getListType(RegistryEntry.T));
				return yamlMarshaller;
			case "json":
			default:
				return jsonMarshaller;
		}
	}
}
