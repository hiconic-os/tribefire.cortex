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

import static tribefire.module.api.DenotationEnrichmentResult.allDone;

import com.braintribe.model.accessdeployment.IncrementalAccess;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.deployment.database.pool.HasDatabaseConnectionPool;
import com.braintribe.model.deployment.resource.sql.SqlBinaryProcessor;
import com.braintribe.model.extensiondeployment.BinaryPersistence;
import com.braintribe.model.extensiondeployment.BinaryRetrieval;
import com.braintribe.model.extensiondeployment.meta.BinaryProcessWith;
import com.braintribe.model.generic.session.GmSession;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.resource.source.ResourceSource;
import com.braintribe.model.resource.source.SqlSource;

import tribefire.module.api.DenotationEnrichmentResult;
import tribefire.module.api.DenotationTransformationContext;
import tribefire.module.api.PlatformBindIds;
import tribefire.module.api.SimpleDenotationEnricher;
import tribefire.platform.wire.space.messaging.accesses.TransientMessagingDataAccessSpace;

/**
 * If a {@link DatabaseConnectionPool} is configured for {@link TransientMessagingDataAccessSpace transient-messaging access}, and there are
 * transformers that convert it to an SQL-based access (e.g. HibernateAccess, which extends {@link HasDatabaseConnectionPool}), then this enricher
 * configures a custom {@link SqlBinaryProcessor} on this access.
 * 
 * @author peter.gazdik
 */
public class TransientMessagingAccessWithSqlBinaryProcessorEnricher extends SimpleDenotationEnricher<HasDatabaseConnectionPool> {

	public TransientMessagingAccessWithSqlBinaryProcessorEnricher() {
		super(HasDatabaseConnectionPool.T);
	}

	@Override
	public DenotationEnrichmentResult<HasDatabaseConnectionPool> enrich(//
			DenotationTransformationContext context, HasDatabaseConnectionPool denotation) {

		if (!PlatformBindIds.TRANSIENT_MESSAGING_DATA_DB_BIND_ID.equals(context.denotationId()))
			return DenotationEnrichmentResult.nothingNowOrEver();

		if (!(denotation instanceof IncrementalAccess))
			return DenotationEnrichmentResult.nothingNowOrEver();

		GmMetaModel model = ((IncrementalAccess) denotation).getMetaModel();
		if (model == null)
			return DenotationEnrichmentResult.nothingYetButCallMeAgain();

		DatabaseConnectionPool connector = denotation.getConnector();
		if (connector == null)
			return DenotationEnrichmentResult.nothingYetButCallMeAgain();

		SqlBinaryProcessor bp = configureSqlBinaryProcessor(context, model, connector);

		return allDone(denotation, "Configured SqlBinaryProcessor [" + bp.getExternalId() + "] for transient-messaging access of type ["
				+ denotation.entityType().getShortName() + "] based on it's connector of type [" + connector.entityType().getShortName() + "]");
	}

	private SqlBinaryProcessor configureSqlBinaryProcessor( //
			DenotationTransformationContext context, GmMetaModel model, DatabaseConnectionPool connector) {


		SqlBinaryProcessor bp = context.create(SqlBinaryProcessor.T);
		bp.setGlobalId("hardwired:binaryProcessor.transientMessaging.sql");
		bp.setName("Transient Messaging SQL Binary Processor");
		bp.setExternalId("binaryProcessor.transientMessaging.sql");
		bp.setConnectionPool(connector);

		configureStreamAndUploadWith(model, bp, "hardwired:transient-messaging-data", true);

		return bp;
	}

	/* package */ static <BP extends BinaryRetrieval & BinaryPersistence> void configureStreamAndUploadWith( //
			GmMetaModel model, BP binaryProcessor, String globalIdPrefix, boolean includeResourceSource) {

		GmSession session = model.session();

		BinaryProcessWith bpWith = session.create(BinaryProcessWith.T, globalIdPrefix + "/BinaryProcessWith");
		bpWith.setRetrieval(binaryProcessor);
		bpWith.setPersistence(binaryProcessor);

		ModelMetaDataEditor mdEditor = BasicModelMetaDataEditor.create(model).withSession(session).done();
		mdEditor.onEntityType(SqlSource.T).addMetaData(bpWith);

		if (includeResourceSource)
			mdEditor.onEntityType(ResourceSource.T).addMetaData(bpWith);
	}


}
