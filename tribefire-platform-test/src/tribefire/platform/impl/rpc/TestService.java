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
package tribefire.platform.impl.rpc;

import java.util.List;
import java.util.Set;

import com.braintribe.model.processing.securityservice.api.exceptions.AuthenticationException;

public interface TestService {

	String collection = "collection";
	String entity = "entity";
	String entityCollection = "entityCollection";
	String mixed = "mixed";
	String mixedWithString = "mixedWithString";
	String mixedWithCollection = "mixedWithCollection";

	int noarg();

	int primitive(int i);

	int primitive(int i, int j);

	TestResponse collection(Set<String> request);

	TestResponse entity(TestRequest request) throws AuthenticationException;

	TestResponse entityCollection(List<TestRequest> request);

	TestResponse mixed(TestRequest request, boolean testBoolean) throws AuthenticationException;

	TestResponse mixed(TestRequest request, boolean testBoolean, String testString) throws AuthenticationException;

	TestResponse mixed(TestRequest request, boolean testBoolean, Set<String> testCollection) throws AuthenticationException;

}
