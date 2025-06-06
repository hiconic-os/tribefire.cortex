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
package tribefire.module.api;

import java.util.List;
import java.util.function.Supplier;

import com.braintribe.model.accessapi.AccessRequest;
import com.braintribe.model.extensiondeployment.HardwiredServiceProcessor;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Model;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.service.api.ServiceRequest;

public interface HardwiredServiceDomainBinder {

	/** Eager instantiation version of {@link #accessRequestProcessor(String, String, EntityType, Supplier)} */
	default <T extends AccessRequest> HardwiredServiceDomainBinder accessRequestProcessor( //
			String externalId, String name, EntityType<T> requestType, AccessRequestProcessor<? super T, ?> processor) {
		return accessRequestProcessor(externalId, name, requestType, () -> processor);
	}

	/**
	 * Convenience for binding a hardwired {@link AccessRequestProcessor}, that internally delegates to
	 * {@link #serviceProcessor(String, String, EntityType, Supplier)}.
	 * <p>
	 * This means the denotation type will still be a {@link HardwiredServiceProcessor} (HardwiredAccessRequestProcessor doesn't even exist).
	 */
	<T extends AccessRequest> HardwiredServiceDomainBinder accessRequestProcessor( //
			String externalId, String name, EntityType<T> requestType, Supplier<AccessRequestProcessor<? super T, ?>> processorSupplier);

	/** Eager instantiation version of {@link #serviceProcessor(String, String, EntityType, Supplier)} */
	default <T extends ServiceRequest> HardwiredServiceDomainBinder serviceProcessor( //
			String externalId, String name, EntityType<T> requestType, ServiceProcessor<? super T, ?> serviceProcessor) {
		return serviceProcessor(externalId, name, requestType, () -> serviceProcessor);
	}

	/**
	 * Binds given ServiceProcessor factory with given {@link ServiceRequest} type
	 * <p>
	 * Technically this creates a {@link HardwiredServiceProcessor} instance and configures a ProcessWith meta-data with this instance on given
	 * request type on the service domain's model.
	 */
	<T extends ServiceRequest> HardwiredServiceDomainBinder serviceProcessor( //
			String externalId, String name, EntityType<T> requestType, Supplier<ServiceProcessor<? super T, ?>> serviceProcessorSupplier);

	/** Adds the declaring model of given entity type as a dependency to the service domain's model. */
	HardwiredServiceDomainBinder modelOf(EntityType<?> entityType);

	/** Adds a <b>classpath model</b> given by it's name to the service domain's model. */
	HardwiredServiceDomainBinder model(String modelName);

	/** Adds given model as a dependency to the service domain's model. */
	HardwiredServiceDomainBinder model(Model model);

	/**
	 * Optional last step of the fluent API to retrieve the denotation types for all hardwired service processors bound on given domain.
	 */
	List<HardwiredServiceProcessor> please();

}
