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
package com.braintribe.model.extensiondeployment.meta;

import com.braintribe.model.extensiondeployment.RequestProcessing;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.PropertyMetaData;

/**
 * This metadata is similar to the VirtualEnum metadata. The only difference is that instead of having the constants defined
 * in the metadata itself, a ServiceRequest can be configured on it, which will then return a list of possible values.
 */
public interface DynamicSelectList extends PropertyMetaData {
	
	EntityType<DynamicSelectList> T = EntityTypes.T(DynamicSelectList.class);
	
	RequestProcessing getRequestProcessing();
	void setRequestProcessing(RequestProcessing rp);
	
	/**
	 * Used for knowing which editor should be used. If false (default), then an editor similar to the one used for the
	 * VirtualEnum should be used, this means a combo box is shown. If true, then a simplified selection UI should be
	 * used.
	 */
	boolean getOutlined();
	void setOutlined(boolean outlined);
	
	/**
	 * Used for disabling the client cache of the loading of the select list.
	 */
	boolean getDisableCache();
	void setDisableCache(boolean disableCache);

}
