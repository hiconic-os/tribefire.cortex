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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.braintribe.codec.Codec;
import com.braintribe.model.crypto.key.encoded.EncodedKeyPair;
import com.braintribe.model.crypto.key.encoded.EncodedPrivateKey;
import com.braintribe.model.crypto.key.encoded.EncodedPublicKey;
import com.braintribe.model.crypto.key.encoded.EncodedSecretKey;
import com.braintribe.model.crypto.key.encoded.KeyEncodingStringFormat;
import com.braintribe.model.crypto.key.keystore.KeyStoreCertificate;
import com.braintribe.model.crypto.key.keystore.KeyStoreKeyPair;
import com.braintribe.model.crypto.key.keystore.KeyStoreSecretKey;
import com.braintribe.model.processing.core.expert.api.GmExpertRegistry;
import com.braintribe.model.processing.crypto.token.KeyCodecProvider;
import com.braintribe.model.processing.crypto.token.loader.CertificateLoader;
import com.braintribe.model.processing.crypto.token.loader.EncryptionTokenLoader;
import com.braintribe.model.processing.crypto.token.loader.KeyPairLoader;
import com.braintribe.model.processing.crypto.token.loader.PrivateKeyLoader;
import com.braintribe.model.processing.crypto.token.loader.PublicKeyLoader;
import com.braintribe.model.processing.crypto.token.loader.SecretKeyLoader;
import com.braintribe.wire.api.space.WireSpace;

public interface CryptoContract extends WireSpace {

	/* ========== Key Loaders ========== */

	GmExpertRegistry keyLoaderExpertRegistry();

	Map<Class<?>, EncryptionTokenLoader<?, ?>> keyLoaderExpertMap();

	KeyPairLoader<EncodedKeyPair> encodedKeyPairLoader();

	PublicKeyLoader<EncodedPublicKey, PublicKey> encodedPublicKeyLoader();

	PrivateKeyLoader<EncodedPrivateKey, PrivateKey> encodedPrivateKeyLoader();

	SecretKeyLoader<EncodedSecretKey, SecretKey> encodedSecretKeyLoader();

	KeyPairLoader<KeyStoreKeyPair> keyStoreKeyPairLoader();

	CertificateLoader<KeyStoreCertificate, java.security.cert.Certificate> keyStoreCertificateLoader();

	SecretKeyLoader<KeyStoreSecretKey, javax.crypto.SecretKey> keyStoreSecretKeyLoader();

	Map<KeyEncodingStringFormat, Codec<byte[], String>> keyStringCodecs();

	/* ========== Key Codecs ========== */

	KeyCodecProvider<PublicKey> publicKeyCodecProvider();

	KeyCodecProvider<PrivateKey> privateKeyCodecProvider();

	KeyCodecProvider<SecretKey> secretKeyCodecProvider();

	GmExpertRegistry keyCodecProviderRegistry();

	/* ========== Commons ========== */

	Codec<byte[], String> base64Codec();

	Codec<byte[], String> hexCodec();

	Function<byte[], String> fingerprintFunction();

}
