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
package com.braintribe.model.processing.accessory.api;

import com.braintribe.model.meta.data.components.ModelExtension;
import com.braintribe.model.processing.accessory.impl.PlatformModelAccessoryFactory;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;

/**
 * Supplier of {@link PlatformModelEssentials} for given {@link #getForAccess(String, String, boolean) access},
 * {@link #getForServiceDomain(String, String, boolean) serviceDomain} or {@link #getForModelName(String, String) modelName}.
 * <p>
 * When resolving for access and serviceDomain, a boolean parameter called <tt>extended</tt> can be used to extend the actual models according to the
 * corresponding {@link ModelExtension} meta-data.
 * <p>
 * This is used as the supplier of model related data for the {@link PlatformModelAccessoryFactory}. The reason for the split in two layers is that
 * one can have different {@link CmdResolver meta data resolvers} for given model/access/serviceDomain, e.g. configured with different aspects like
 * user roles. Then, different {@link ModelAccessory}s are needed, but the model and {@link ModelOracle} can be shared. This supplier is responsible
 * for re-using these objects as much as possible, while the factory uses them for multiple model accessories.
 * 
 * @author peter.gazdik
 */
public interface PlatformModelEssentialsSupplier {

	PlatformModelEssentials getForAccess(String accessId, String perspective, boolean extended);

	PlatformModelEssentials getForServiceDomain(String serviceDomainId, String perspective, boolean extended);

	PlatformModelEssentials getForModelName(String modelName, String perspective);

}
