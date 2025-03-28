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
package com.braintribe.model.processing.aspect.crypto.test.commons.model;

import java.util.Arrays;
import java.util.List;

import com.braintribe.model.crypto.configuration.CryptoConfiguration;
import com.braintribe.model.crypto.configuration.encryption.EncryptionConfiguration;
import com.braintribe.model.crypto.configuration.hashing.HashingConfiguration;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.data.crypto.PropertyCrypting;
import com.braintribe.model.processing.aspect.crypto.test.commons.TestDataProvider;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

public class TestMetaModelProvider {

	// @formatter:off
	public static final List<EntityType<?>> types = Arrays.asList(
			Standard.T,
			Encrypted.T,
			Hashed.T,
			Mixed.T,
			EncryptedMulti.T,
			HashedMulti.T,
			MixedMulti.T,
			EncryptedNonDeterministic.T,
			HashedNonDeterministic.T
	);
	// @formatter:on

	public static GmMetaModel provideEnrichedModel() {
		EntityType<?> currentType = null; 
		
		GmMetaModel metaModel = new NewMetaModelGeneration().buildMetaModel("gm:EncryptableModel", types);
		
		ModelMetaDataEditor editor = new BasicModelMetaDataEditor(metaModel);

		// ########################
		// # .. Hashed .......... #
		// ########################
		
		currentType = Hashed.T;

		addPropertyHashingMetaData(editor		, currentType, "hashedProperty"	, "MD5");
		
		
		// ########################
		// # .. Encrypted ....... #
		// ########################
		
		currentType = Encrypted.T;

		addPropertyEncryptionMetaData(editor	, currentType, "encryptedProperty"	, "AES");
		
		// ########################
		// # .. Mixed .......... #
		// ########################

		currentType = Mixed.T;

		addPropertyHashingMetaData(editor		, currentType, "hashedProperty"		, "MD5-SALTED");
		addPropertyEncryptionMetaData(editor	, currentType, "encryptedProperty"	, "AES");
		

		// ########################
		// # .. HashedMulti ..... #
		// ########################

		currentType = HashedMulti.T;

		addPropertyHashingMetaData(editor, currentType, "hashedProperty", "SHA-256");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty1", "SHA-256");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty2", "SHA-256-SALTED");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty3", "SHA-1");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty4", "SHA-1-SALTED");

		
		// ########################
		// # .. EncryptedMulti .. #
		// ########################

		currentType = EncryptedMulti.T;

		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty"	, "AES");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty1"	, "DESede");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty2"	, "DESede");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty3"	, "DES");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty4"	, "AES");
		
		
		// ########################
		// # .. MixedMulti ...... #
		// ########################

		currentType = MixedMulti.T;

		//not set to MixedMulti, cascaded from EncryptedMulti and HashedMulti
		TestDataProvider.savePropertyConfiguration(currentType, "encryptedProperty", "AES"); 
		TestDataProvider.savePropertyConfiguration(currentType, "hashedProperty", "SHA-256"); 
		
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty1"	, "DESede");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty2"	, "AES");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty3"	, "AES");
		addPropertyEncryptionMetaData(editor, currentType, "encryptedProperty4"	, "DES");
		
		addPropertyHashingMetaData(editor, currentType, "hashedProperty1", "MD5");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty2", "SHA-1");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty3", "SHA-1-SALTED");
		addPropertyHashingMetaData(editor, currentType, "hashedProperty4", "SHA-256-SALTED");


		// ################################
		// # .. HashedNonDeterministic .. #
		// ################################

		currentType = HashedNonDeterministic.T;

		addPropertyHashingMetaData(editor, currentType, "hashedProperty"	, "SHA-256-RANDOM-SALTED");
		
		return metaModel;
	}

	public static PropertyCrypting createPropertyCrypting(CryptoConfiguration cryptoConfiguration) {
		PropertyCrypting propertyCrypting = PropertyCrypting.T.create();
		propertyCrypting.setCryptoConfiguration(cryptoConfiguration);
		return propertyCrypting;
	}

	public static HashingConfiguration getHashingConfiguration(String key) {
		return (HashingConfiguration) TestDataProvider.configurationMap.get(key);
	}

	public static EncryptionConfiguration getEncryptionConfiguration(String key) {
		return (EncryptionConfiguration) TestDataProvider.configurationMap.get(key);
	}

	private static void addPropertyHashingMetaData(ModelMetaDataEditor editor, EntityType<?> type, String property, String configurationKey) {
		editor.onEntityType(type).addPropertyMetaData(property, createPropertyCrypting(getHashingConfiguration(configurationKey)));
		TestDataProvider.savePropertyConfiguration(type, property, configurationKey);
	}

	private static void addPropertyEncryptionMetaData(ModelMetaDataEditor editor, EntityType<?> type, String property, String configurationKey) {
		editor.onEntityType(type).addPropertyMetaData(property, createPropertyCrypting(getEncryptionConfiguration(configurationKey)));
		TestDataProvider.savePropertyConfiguration(type, property, configurationKey);
	}

}
