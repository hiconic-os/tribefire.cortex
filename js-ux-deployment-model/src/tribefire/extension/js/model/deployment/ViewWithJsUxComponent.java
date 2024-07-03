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
import com.braintribe.model.resource.Icon;

/**
 * This meta data links an entity type to a {@link JsUxComponent JS UX component} in which the entity type should be viewed. 
 *
 */
public interface ViewWithJsUxComponent extends EntityTypeMetaData {

	EntityType<ViewWithJsUxComponent> T = EntityTypes.T(ViewWithJsUxComponent.class);
	
	/**
	 * The component is referenced via its global id
	 */
	JsUxComponent getComponent();
	void setComponent(JsUxComponent component);
	
	boolean getListView();
	void setListView(boolean listView);
	
	Icon getIcon();
	void setIcon(Icon icon);

	LocalizedString getDisplayName();
	void setDisplayName(LocalizedString displayName);
	
	boolean getHideDetails();
	void setHideDetails(boolean hideDetails);
	
	public boolean getReadOnly();
	public void setReadOnly(boolean readOnly);
	
}
