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
package com.braintribe.model.processing.resource.filesystem;

public interface BinaryProcessorTestCommons {

	String SERVICE_ID_SIMPLE = "simple";
	
	/**
	 * {@link FileSystemBinaryProcessor} doesn't perform enriching any more. The simple and the enriching one are now identical.
	 * TODO: Find out if it's in the scope of these tests to add an enricher
	 */
	@Deprecated
	String SERVICE_ID_ENRICHING = "enriching";

}
