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
package tribefire.platform.impl.service;

import java.util.List;
import java.util.Objects;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.deployment.Deployable;
import com.braintribe.model.extensiondeployment.meta.AroundProcessWith;
import com.braintribe.model.extensiondeployment.meta.InterceptWith;
import com.braintribe.model.extensiondeployment.meta.InterceptionType;
import com.braintribe.model.extensiondeployment.meta.PostProcessWith;
import com.braintribe.model.extensiondeployment.meta.PreProcessWith;
import com.braintribe.model.extensiondeployment.meta.ProcessWith;
import com.braintribe.model.extensiondeployment.meta.ProcessWithComponent;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.processing.deployment.api.DeployedComponentResolver;
import com.braintribe.model.processing.meta.cmd.builders.EntityMdResolver;
import com.braintribe.model.processing.service.api.ProceedContext;
import com.braintribe.model.processing.service.api.ProceedContextBuilder;
import com.braintribe.model.processing.service.api.ServiceAroundProcessor;
import com.braintribe.model.processing.service.api.ServiceInterceptionChainBuilder;
import com.braintribe.model.processing.service.api.ServiceInterceptorProcessor;
import com.braintribe.model.processing.service.api.ServicePostProcessor;
import com.braintribe.model.processing.service.api.ServicePreProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.api.aspect.DomainIdAspect;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.service.api.DispatchableRequest;
import com.braintribe.model.service.api.ExecuteInDomain;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * <p>
 * A {@link ServiceAroundProcessor} which intercepts incoming {@link ServiceRequest} instances resolving further {@link ServiceInterceptorProcessor}s
 * and a {@link ServiceProcessor} override from a ServiceDomain's metadata to extend the {@link ProceedContext} with them.
 * 
 * @author Neidhart Orlich
 * @author Dirk Scheffler
 */
public class ServiceDomainMappedDispatchingInterceptor implements ServiceAroundProcessor<ServiceRequest, Object> {

	private ModelAccessoryFactory modelAccessoryFactory;
	private DeployedComponentResolver deployedComponentResolver;
			
	@Required
	@Configurable
	public void setDeployedComponentResolver(DeployedComponentResolver deployedComponentResolver) {
		this.deployedComponentResolver = deployedComponentResolver;
	}
	
	@Required
	@Configurable
	public void setModelAccessoryFactory(ModelAccessoryFactory modelAccessoryFactory) {
		this.modelAccessoryFactory = modelAccessoryFactory;
	}
	
	@Override
	public Object process(ServiceRequestContext requestContext, ServiceRequest request, ProceedContext proceedContext) {
		String domainId = ServiceDomains.getDomainId(request);
		
		// in case of ExecuteInDomain we choose the domainId of that ExecuteInDomain request 
		// but actually proceed with the unwrapped payload request of it.
		if (request instanceof ExecuteInDomain) {
			ExecuteInDomain executeInDomain = (ExecuteInDomain)request;
			request = executeInDomain.getServiceRequest();
		}
		
		ProceedContext enrichedProceedContext = enrichProceedContextFromMetaData(request, domainId, proceedContext);
		ServiceRequestContext enrichedContext = requestContext.derive().set(DomainIdAspect.class, domainId).build();
		
		return enrichedProceedContext.proceed(enrichedContext, request);
	}

	private ServiceProcessor<ServiceRequest, Object> resolveProcessor(EntityMdResolver mdResolver, ServiceRequest request) {
		String serviceId = getServiceId(request);
		
		if (serviceId == null) {
			return resolveSingletonProcessor(mdResolver);
		}
		else {
			return resolveDispatchProcessor(mdResolver, serviceId);
		}
		
	}

	private ServiceProcessor<ServiceRequest, Object> resolveDispatchProcessor(EntityMdResolver mdResolver, String serviceId) {
		ProcessWithComponent processWithComponent = mdResolver.meta(ProcessWithComponent.T).exclusive();
		final EntityType<? extends Deployable> componentType;
		
		if (processWithComponent == null) {
			componentType = com.braintribe.model.extensiondeployment.ServiceProcessor.T;
		}
		else {
			GmEntityType gmComponentType = processWithComponent.getComponentType();
			Objects.requireNonNull(gmComponentType, "ProcessWithComponent misses assigned componenType: " + processWithComponent);
			componentType = gmComponentType.reflectionType();
		}

		return deployedComponentResolver.resolve(serviceId, componentType);
	}

	private ServiceProcessor<ServiceRequest, Object> resolveSingletonProcessor(EntityMdResolver mdResolver) {
		ProcessWith processWith = mdResolver.meta(ProcessWith.T).exclusive();
		
		if (processWith == null)
			return null;
		
		final EntityType<? extends Deployable> componentType;

		GmEntityType gmComponentType = processWith.getComponentType();
		
		if (gmComponentType == null) {
			componentType = com.braintribe.model.extensiondeployment.ServiceProcessor.T;
		}
		else {
			componentType = gmComponentType.entityType();
		}
		
		com.braintribe.model.extensiondeployment.ServiceProcessor processorDeployable = processWith.getProcessor();
		
		Objects.requireNonNull(processorDeployable, "ProcessWith misses assigned processor: " + processWith);
		
		return deployedComponentResolver.resolve(processorDeployable, componentType);
	}
	
	private String getServiceId(ServiceRequest request) {
		if (request.dispatchable()) {
			DispatchableRequest dispatchableRequest = (DispatchableRequest)request;
			return dispatchableRequest.getServiceId();
		}
		
		return null;
	}

	private ProceedContext enrichProceedContextFromMetaData(ServiceRequest request, String domainId, ProceedContext proceedContext) {
		EntityMdResolver mdResolver = modelAccessoryFactory.getForServiceDomain(domainId).getCmdResolver().getMetaData().entityType(request.entityType());
		
		ServiceProcessor<ServiceRequest, Object> processor = resolveProcessor(mdResolver, request);
		
		final ProceedContextBuilder interceptionBuilder;
		
		if (processor == null) {
			interceptionBuilder = proceedContext.extend();
		}
		else {
			interceptionBuilder = proceedContext.newInterceptionChain(processor);
		}
		
		if (request.interceptable())
			enrichWithInterceptors(mdResolver, interceptionBuilder);

		return interceptionBuilder.build();
	}
	
	
	private void enrichWithInterceptors(EntityMdResolver mdResolver, ServiceInterceptionChainBuilder interceptionBuilder) {
		List<InterceptWith> interceptWiths = mdResolver.meta(InterceptWith.T).list();
		
		for (InterceptWith interceptWith: interceptWiths) {
			
			InterceptionType interceptionType = interceptWith.interceptionType();
			switch (interceptionType) {
				case preProcess: 
					interceptionBuilder.preProcessWith(resolvePreProcessor((PreProcessWith)interceptWith));
					break;
				case aroundProcess: 
					interceptionBuilder.aroundProcessWith(resolveAroundProcessor((AroundProcessWith)interceptWith));
					break;
				case postProcess: 
					interceptionBuilder.postProcessWith(resolvePostProcessor((PostProcessWith)interceptWith));
					break;
				default:
					throw new UnsupportedOperationException("Unsupported InterceptorKind: " + interceptionType);
			}
		}
	}
	
	private ServicePostProcessor<?> resolvePostProcessor(PostProcessWith postProcessWith) {
		return deployedComponentResolver.resolve(postProcessWith.getProcessor(), com.braintribe.model.extensiondeployment.ServicePostProcessor.T);
	}

	private ServiceAroundProcessor<?, ?> resolveAroundProcessor(AroundProcessWith aroundProcessWith) {
		return deployedComponentResolver.resolve(aroundProcessWith.getProcessor(), com.braintribe.model.extensiondeployment.ServiceAroundProcessor.T);
	}

	private ServicePreProcessor<?> resolvePreProcessor(PreProcessWith preProcessWith) {
		return deployedComponentResolver.resolve(preProcessWith.getProcessor(), com.braintribe.model.extensiondeployment.ServicePreProcessor.T);
	}
}
