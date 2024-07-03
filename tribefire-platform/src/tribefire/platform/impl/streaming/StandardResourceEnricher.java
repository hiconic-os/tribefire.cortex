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

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.resource.enrichment.ResourceEnricher;
import com.braintribe.model.processing.resource.enrichment.ResourceEnrichingStreamer;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resourceapi.enrichment.EnrichResource;
import com.braintribe.model.resourceapi.enrichment.EnrichResourceResponse;

/**
 * <p>
 * A standard {@link ResourceEnrichingStreamer}-based {@link ResourceEnricher}.
 * 
 */
public class StandardResourceEnricher implements ResourceEnricher {

	private ResourceEnrichingStreamer enrichingStreamer;

	@Required
	@Configurable
	public void setEnrichingStreamer(ResourceEnrichingStreamer enrichingStreamer) {
		this.enrichingStreamer = enrichingStreamer;
	}

	@Override
	public EnrichResourceResponse enrich(AccessRequestContext<EnrichResource> context) {

		EnrichResource request = context.getOriginalRequest();

		Resource resource = request.getResource();

		// @formatter:off
		boolean enriched = enrichingStreamer
								.stream()
									.onlyIfEnriched()
									.context(context)
									.enriching(resource);
		// @formatter:on

		EnrichResourceResponse response = EnrichResourceResponse.T.create();

		if (enriched) {
			// response with no Resource means no enrichment took place
			response.setResource(resource);
		}

		return response;

	}

}
