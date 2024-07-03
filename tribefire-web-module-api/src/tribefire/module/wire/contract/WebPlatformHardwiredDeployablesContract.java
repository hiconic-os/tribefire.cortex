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

import java.util.function.Supplier;

import javax.servlet.http.HttpServlet;

import com.braintribe.model.access.collaboration.distributed.api.DcsaSharedStorage;
import com.braintribe.model.cortexapi.CortexRequest;
import com.braintribe.model.extensiondeployment.HardwiredServiceProcessor;
import com.braintribe.model.extensiondeployment.HardwiredWebTerminal;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.web.api.registry.WebRegistries;
import com.braintribe.web.impl.registry.ConfigurableWebRegistry;

import tribefire.module.api.WebRegistryConfiguration;

public interface WebPlatformHardwiredDeployablesContract extends //
		HardwiredCheckProcessorsContract, //
		HardwiredMarshallersContract, //
		HardwiredResourceProcessorsContract, //
		HardwiredWorkersContract, //
		HardwiredStateProcessorsContract
{

	/** Eager instantiation version of {@link #bindCortexServiceProcessor(String, String, EntityType, Supplier)}. */
	default <T extends CortexRequest> HardwiredServiceProcessor bindCortexServiceProcessor( //
			String externalId, String name, EntityType<T> requestType, ServiceProcessor<? super T, ?> serviceProcessor) {
		return bindCortexServiceProcessor(externalId, name, requestType, () -> serviceProcessor);
	}

	/**
	 * Binds a service processor on the "cortex" domain. Note that given {@code requestType}'s declaring model must be a part (dependency) of the
	 * cortex's service model.
	 */
	<T extends CortexRequest> HardwiredServiceProcessor bindCortexServiceProcessor( //
			String externalId, String name, EntityType<T> requestType, Supplier<ServiceProcessor<? super T, ?>> serviceProcessorSupplier);

	/** Eager instantiation version of {@link #bindWebTerminal(String, String, String, Supplier)}. */
	default HardwiredWebTerminal bindWebTerminal(String externalId, String name, String pathIdentifier, HttpServlet servlet) {
		return bindWebTerminal(externalId, name, pathIdentifier, () -> servlet);
	}

	/**
	 * Binds a web terminal ({@link HttpServlet}) on the "components" servlet. This means the URL to target this servlet will be of form
	 * <tt>tribefire-services/components/${pathIdentifier}</tt>.
	 * <p>
	 * In oder to register a top-level (i.e. not under "components") use {@link #webRegistry()}'s
	 * {@link ConfigurableWebRegistry#addServlet(com.braintribe.web.api.registry.ServletRegistration) addServlet} method.
	 */
	HardwiredWebTerminal bindWebTerminal(String externalId, String name, String pathIdentifier, Supplier<HttpServlet> servletSupplier);

	/**
	 * Registry for filters, servlets, etc. Use this to register top-level (in terms of URL) servlets (or other components).
	 * <p>
	 * In order to conveniently create entries for this registry use {@link WebRegistries} utility class. Example:
	 * 
	 * <pre>
	 * hardwiredDeployables.webRegistry().addServlet( //
	 * 		WebRegistries.servlet() //
	 * 				.name("MyServlet") //
	 * 				.instance(new MyServlet()) //
	 * 				.pattern("/mypath") //
	 * );
	 * </pre>
	 */
	WebRegistryConfiguration webRegistry();

	/**
	 * Supplier for the standard {@link DcsaSharedStorage} used by the platform's accesses like "cortex". <> WARNING: Do not resolve the storage
	 * during the module loading/wiring phase, but only after the server is up and running. 
	 */
	Supplier<DcsaSharedStorage> sharedStorageSupplier();

}
