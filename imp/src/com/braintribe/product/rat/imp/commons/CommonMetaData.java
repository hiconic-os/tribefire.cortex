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
package com.braintribe.product.rat.imp.commons;

import com.braintribe.model.extensiondeployment.meta.OnChange;
import com.braintribe.model.extensiondeployment.meta.ProcessWith;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.data.constraint.Mandatory;
import com.braintribe.model.meta.data.prompt.Visible;

public interface CommonMetaData {

	EntityType<OnChange> onChange = OnChange.T;
	EntityType<Visible> visible = Visible.T;
	EntityType<ProcessWith> processWith = ProcessWith.T;
	EntityType<Mandatory> mandatory = Mandatory.T;
}
