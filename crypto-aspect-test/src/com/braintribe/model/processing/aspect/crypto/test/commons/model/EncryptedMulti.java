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


import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface EncryptedMulti extends Encrypted {

	EntityType<EncryptedMulti> T = EntityTypes.T(EncryptedMulti.class);

	// @formatter:off
	String getEncryptedProperty1();
	void setEncryptedProperty1(String encryptedProperty1);

	String getEncryptedProperty2();
	void setEncryptedProperty2(String encryptedProperty2);

	String getEncryptedProperty3();
	void setEncryptedProperty3(String encryptedProperty3);

	String getEncryptedProperty4();
	void setEncryptedProperty4(String encryptedProperty4);
	// @formatter:on

}
