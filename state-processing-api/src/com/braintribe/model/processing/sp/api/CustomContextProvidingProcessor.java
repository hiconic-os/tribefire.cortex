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
package com.braintribe.model.processing.sp.api;

import com.braintribe.model.generic.GenericEntity;

/**
 * Extension of {@link StateChangeProcessor} with capability of creating a custom context. This method is invoked before
 * any processing begins (before the {@link #onBeforeStateChange(BeforeStateChangeContext)} method) and the instance
 * returned will then be injected into contexts for all "onStateChange" methods.
 * <p>
 * This can be used when some context is needed that should span over more than one manipulation.
 * 
 * @see StateChangeProcessor
 * @see StateChangeContext#getProcessorContext()
 * @deprecated use {@link StateChangeProcessor} directly as it now declared {@link StateChangeProcessor#createProcessorContext()}
 */
public interface CustomContextProvidingProcessor<T extends GenericEntity, C extends GenericEntity> extends StateChangeProcessor<T, C> {

}
