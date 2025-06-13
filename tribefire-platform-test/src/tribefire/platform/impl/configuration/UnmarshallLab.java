package tribefire.platform.impl.configuration;

import java.io.StringReader;

import com.braintribe.cartridge.common.processing.configuration.url.model.RegistryEntry;
import com.braintribe.codec.marshaller.api.GmDeserializationOptions;
import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.model.generic.GMF;

public class UnmarshallLab {
	public static void main(String[] args) {
		YamlMarshaller yamlMarshaller = new YamlMarshaller();
		
		GmDeserializationOptions options = GmDeserializationOptions.deriveDefaults().setInferredRootType(GMF.getTypeReflection().getListType(RegistryEntry.T)).build();
		
		String yaml = "[{ bindId: egal }]";
		
		Object result = yamlMarshaller.unmarshall(new StringReader(yaml), options);
		
		System.out.println(result);
		
		String yaml2 = "!com.braintribe.cartridge.common.processing.configuration.url.model.RegistryEntry { bindId: egal }]";
		
		Object result2 = yamlMarshaller.unmarshall(new StringReader(yaml2), options);
		
		System.out.println(result2);
	}
}
