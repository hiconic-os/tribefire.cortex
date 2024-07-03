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
package tribefire.platform.impl.streaming;

import static com.braintribe.utils.lcd.CollectionTools2.isEmpty;
import static com.braintribe.utils.lcd.CollectionTools2.nullSafe;

import java.util.List;
import java.util.Set;

import com.braintribe.cfg.Required;
import com.braintribe.logging.Logger;
import com.braintribe.model.extensiondeployment.meta.MimeBasedResourceEnriching;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.resource.enrichment.ResourceEnricher;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resourceapi.enrichment.EnrichResource;
import com.braintribe.model.resourceapi.enrichment.EnrichResourceResponse;

/**
 * Handles {@link EnrichResource} according to the {@link MimeBasedResourceEnriching} meta-data (configured on the
 * access the {@link EnrichResource} is evaluated against).
 * 
 * @author peter.gazdik
 */
public class MimeBasedDispatchingResourceEnricher implements ResourceEnricher, ServiceProcessor<EnrichResource, EnrichResourceResponse> {

	public static final String externalId = "resourceEnricher.mime-based-dispatching";
	public static final String globalId = "hardwired:service/" + externalId;

	public static final String mimeTypeDetectingEnricherExternalId = "resourceEnricher.mime-type-detector";
	public static final String mimeTypeDetectingEnricherGlobalId = "hardwired:service/" + externalId;

	private static final Logger log = Logger.getLogger(MimeBasedDispatchingResourceEnricher.class);
	
	private ModelAccessoryFactory systemModelAccessoryFactory;

	@Required
	public void setSystemModelAccessoryFactory(ModelAccessoryFactory systemModelAccessoryFactory) {
		this.systemModelAccessoryFactory = systemModelAccessoryFactory;
	}

	@Override
	public EnrichResourceResponse enrich(AccessRequestContext<EnrichResource> context) {
		return process(context, context.getOriginalRequest());
	}
	
	@Override
	public EnrichResourceResponse process(ServiceRequestContext context, EnrichResource request) {
		EnrichResourceResponse response = EnrichResourceResponse.T.create();

		Resource resource = request.getResource();

		String mimeType = resource.getMimeType();
		if (mimeType == null)
			return response;

		List<MimeBasedResourceEnriching> enrichings = resolveEnrichings(context);
		if (isEmpty(enrichings))
			return response;

		for (MimeBasedResourceEnriching enriching : nullSafe(enrichings))
			if (matches(enriching, mimeType))
				resource = enrich(context, resource, enriching);

		response.setResource(resource);

		return response;
	}

	private List<MimeBasedResourceEnriching> resolveEnrichings(ServiceRequestContext context) {
		
		return systemModelAccessoryFactory.getForAccess(context.getDomainId()).getCmdResolver().getMetaData().meta(MimeBasedResourceEnriching.T).list();
	}

	private boolean matches(MimeBasedResourceEnriching enriching, String mimeType) {
		if (enriching == null)
			return false;

		Set<String> mimeTypes = enriching.getMimeTypes();
		if (mimeType == null)
			return false;

		return mimeTypes.contains(mimeType);
	}

	private Resource enrich(ServiceRequestContext context, Resource resource, MimeBasedResourceEnriching enriching) {
		com.braintribe.model.extensiondeployment.ResourceEnricher resourceEnricher = enriching.getResourceEnricher();
		if (resourceEnricher == null) {
			log.warn("Misconfigured MimeBasedResourceEnriching with globalId: " + enriching.getGlobalId() + ". Enricher is null.");
			return resource;
		}

		EnrichResource request = EnrichResource.T.create();
		request.setResource(resource);
		request.setServiceId(resourceEnricher.getExternalId());
		request.setDomainId(context.getDomainId());

		Resource enrichedResource = request.eval(context).get().getResource();

		return enrichedResource != null ? enrichedResource : resource;
	}

}
