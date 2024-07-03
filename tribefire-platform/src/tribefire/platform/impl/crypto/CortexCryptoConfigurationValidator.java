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
package tribefire.platform.impl.crypto;

import java.util.UUID;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.InitializationAware;
import com.braintribe.crypto.BidiCryptor;
import com.braintribe.crypto.Cryptor;
import com.braintribe.crypto.Encryptor;
import com.braintribe.logging.Logger;

public class CortexCryptoConfigurationValidator implements InitializationAware {

	private static final Logger log = Logger.getLogger(CortexCryptoConfigurationValidator.class);

	private boolean runOnStartUp = true;
	private Encryptor cortexHasher;
	private BidiCryptor cortexSymmetricCryptor;
	private BidiCryptor cortexAsymmetricCryptor;

	@Configurable
	public void setRunOnStartUp(boolean runOnStartUp) {
		this.runOnStartUp = runOnStartUp;
	}

	@Configurable
	public void setCortexHasher(Encryptor cortexHasher) {
		this.cortexHasher = cortexHasher;
	}

	@Configurable
	public void setCortexSymmetricCryptor(BidiCryptor cortexSymmetricCryptor) {
		this.cortexSymmetricCryptor = cortexSymmetricCryptor;
	}

	public void setCortexAsymmetricCryptor(BidiCryptor cortexAsymmetricCryptor) {
		this.cortexAsymmetricCryptor = cortexAsymmetricCryptor;
	}

	@Override
	public void postConstruct() {
		if (!runOnStartUp) {
			return;
		}
		validateConfiguration();
	}

	public void validateConfiguration() throws CortexCryptoConfiguratorException {

		byte[] validationData = UUID.randomUUID().toString().getBytes();

		if (cortexHasher != null) {
			validateCryptor("Cortex hasher", cortexHasher, validationData);
		}

		if (cortexSymmetricCryptor != null) {
			validateCryptor("Cortex symmetric key-based cryptor", cortexSymmetricCryptor, validationData);
		}

		if (cortexAsymmetricCryptor != null) {
			validateCryptor("Cortex asymmetric key-based cryptor", cortexAsymmetricCryptor, validationData);
		}

	}

	protected static void validateCryptor(String context, Cryptor cryptor, byte[] validationData) throws CortexCryptoConfiguratorException {

		testCryptor(context, cryptor, validationData);

		if (log.isDebugEnabled()) {
			log.debug(context + " is valid");
		}

	}

	protected static void testCryptor(String context, Cryptor cryptor, byte[] validationData) throws CortexCryptoConfiguratorException {
		if (cryptor instanceof BidiCryptor) {
			testBidiCryptor(context, (BidiCryptor) cryptor, validationData);
		} else if (cryptor instanceof Encryptor) {
			testEncryptor(context, (Encryptor) cryptor, validationData);
		}
	}

	protected static byte[] testEncryptor(String context, Encryptor encryptor, byte[] validationData) throws CortexCryptoConfiguratorException {

		try {
			byte[] encrypted = encryptor.encrypt(validationData).result().asBytes();

			if (log.isTraceEnabled()) {
				log.trace(context + " encrypted successfully");
			}

			return encrypted;

		} catch (Exception e) {
			throw new CortexCryptoConfiguratorException(context + " failed to encrypt: " + e.getMessage(), e);
		}

	}

	protected static byte[] testBidiCryptor(String context, BidiCryptor cryptor, byte[] validationData) throws CortexCryptoConfiguratorException {

		byte[] encrypted = testEncryptor(context, cryptor, validationData);

		try {
			byte[] decrypted = cryptor.decrypt(encrypted).result().asBytes();

			if (log.isTraceEnabled()) {
				log.trace(context + " decrypted successfully");
			}

			return decrypted;

		} catch (Exception e) {
			throw new CortexCryptoConfiguratorException(context + " failed to decrypt: " + e.getMessage(), e);
		}

	}

}
