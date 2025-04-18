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
package tribefire.extension.js.model.deployment;

import com.braintribe.model.generic.i18n.LocalizedString;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;

/**
 * This metadata is configured for an entityType where the given component will be displayed as detail panel when the master view has
 * the type selected. 
 *
 */
public interface DetailWithUiComponent extends EntityTypeMetaData {
	

	EntityType<DetailWithUiComponent> T = EntityTypes.T(DetailWithUiComponent.class);
	
	/**
	 * The component is referenced via its global id
	 */
	JsUxComponent getComponent();
	void setComponent(JsUxComponent component);
	
	/**
	 * True for hiding the default details panel (the PP).
	 */
	boolean getHideDefaultDetails();
	void setHideDefaultDetails(boolean hideDefaultDetails);
	
	LocalizedString getDisplayName();
	void setDisplayName(LocalizedString displayName);
	
	public boolean getReadOnly();
	public void setReadOnly(boolean readOnly);

}
