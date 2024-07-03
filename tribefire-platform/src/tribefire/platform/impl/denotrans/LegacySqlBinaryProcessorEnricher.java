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
package tribefire.platform.impl.denotrans;

import static tribefire.platform.impl.denotrans.TransientMessagingAccessWithSqlBinaryProcessorEnricher.configureStreamAndUploadWith;

import com.braintribe.model.deployment.resource.sql.SqlBinaryProcessor;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.resource.source.SqlSource;

import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.PlatformBindIds;
import tribefire.module.api.SimpleDenotationEnricher;

/**
 * @author peter.gazdik
 */
public class LegacySqlBinaryProcessorEnricher extends SimpleDenotationEnricher<SqlBinaryProcessor> {

	public LegacySqlBinaryProcessorEnricher() {
		super(SqlBinaryProcessor.T);
	}

	@Override
	public DenotationEnrichmentResult<SqlBinaryProcessor> enrich(DenotationTransformationContext context, SqlBinaryProcessor denotation) {

		if (!PlatformBindIds.RESOURCES_DB.equals(context.denotationId()))
			return DenotationEnrichmentResult.nothingNowOrEver();

		String globalId = "hardwired:service/" + denotation.getExternalId();
		denotation.setGlobalId(globalId);
		denotation.setName("SQL Binary Processor");
		denotation.setExternalId("binaryProcessor.sql");

		GmMetaModel basicResourceModel = context.findEntityByGlobalId(SqlSource.T.getModel().globalId());

		configureStreamAndUploadWith(basicResourceModel, denotation, "hardwired:legacy-sql-binary-processor", false);

		return DenotationEnrichmentResult.allDone(denotation, "Configured legacy SqlBinaryProcessor globalId to [" + globalId
				+ "] and configured as deafult BinaryProcessWith for SqlSource on basic-resource-model.");
	}

}
