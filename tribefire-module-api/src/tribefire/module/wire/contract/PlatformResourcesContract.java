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

import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.api.ResourceHandle;

/**
 * @author peter.gazdik
 */
public interface PlatformResourcesContract extends WireSpace {

	/**
	 * Returns a resource based on a namespace prefix such as "tmp:foobar/fixfox.txt"
	 */
	ResourceHandle resource(String path);
	
	ResourceHandle tmp(String path);

	ResourceHandle cache(String path);

	ResourceHandle storage(String path);

	ResourceHandle database(String path);

	/** @see ModuleResourcesContract#resource(String) */
	ResourceHandle publicResources(String path);

}
