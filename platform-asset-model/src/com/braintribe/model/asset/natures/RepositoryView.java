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
package com.braintribe.model.asset.natures;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * A <code>RepositoryView</code> holds (partial or full) repository data based on which a repository configuration can
 * be created. For more information see <code>com.braintribe.devrock.model.repositoryview.RepositoryView</code.
 * <p>
 * Note that views are no longer assets, i.e. respective artifacts do not have an <code>asset.man</code> part. However,
 * since Jinni 2.0 uses the <code>PlatformAssetResolver</code> (+ collector / builder) to resolve
 * <code>RepositoryView</code>s, we still have to keep this nature for now to maintain backwards compatibitily.
 */
public interface RepositoryView extends PlatformAssetNature {

	final EntityType<RepositoryView> T = EntityTypes.T(RepositoryView.class);

}
