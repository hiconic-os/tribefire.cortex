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

import java.util.function.BiConsumer;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.web.servlet.auth.WebCredentialsProvider;
import com.braintribe.web.servlet.home.model.LinkCollection;

import tribefire.module.api.DenotationTransformerRegistry;
import tribefire.module.api.EnvironmentDenotations;

/**
 * Contract for binding {@link EnvironmentDenotations Environment Denotation Registry} transformers.
 * 
 * @author peter.gazdik
 */
public interface WebPlatformHardwiredExpertsContract extends HardwiredExpertsContract {

	<T extends GenericEntity> void bindLandingPageLinkConfigurer(String groupPattern, EntityType<T> type, BiConsumer<T, LinkCollection> configurer);

	void registerWebCredentialsProvider(String key, WebCredentialsProvider webCredentialsProvider);
}
