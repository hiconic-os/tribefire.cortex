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
package com.braintribe.model.processing.cortex.service.connection;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.cortexapi.connection.CreateModelFromDbSchema;
import com.braintribe.model.cortexapi.connection.DbSchemaRequest;
import com.braintribe.model.cortexapi.connection.DbSchemaResponse;
import com.braintribe.model.cortexapi.connection.SynchronizeDbSchema;
import com.braintribe.model.cortexapi.connection.SynchronizeModelWithDbSchema;
import com.braintribe.model.cortexapi.model.CreateModel;
import com.braintribe.model.cortexapi.model.CreateModelResponse;
import com.braintribe.model.cortexapi.model.ModelCreated;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessors;
import com.braintribe.model.processing.deployment.api.DeployRegistry;
import com.braintribe.model.processing.notification.api.builder.Notifications;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class DbSchemaRequestProcessor implements AccessRequestProcessor<DbSchemaRequest, DbSchemaResponse> {

	private DeployRegistry deployRegistry;
	
	@Configurable
	@Required
	public void setDeployRegistry(DeployRegistry deployRegistry) {
		this.deployRegistry = deployRegistry;
	}
	
	private final AccessRequestProcessor<DbSchemaRequest, DbSchemaResponse> dispatcher = AccessRequestProcessors.dispatcher(config->{
		config.register(SynchronizeDbSchema.T, this::synchronizeDbSchema);
		config.register(SynchronizeModelWithDbSchema.T, this::synchronizeModelWithDbSchema);
		config.register(CreateModelFromDbSchema.T, this::createModelFromDbSchema);
	});

	@Override
	public DbSchemaResponse process(AccessRequestContext<DbSchemaRequest> context) {
		return dispatcher.process(context);
	}
	
	public DbSchemaResponse synchronizeDbSchema(AccessRequestContext<SynchronizeDbSchema> context) {
		// @formatter:off
 		SynchronizeDbSchema request = context.getRequest();
		return new DbSchemaSynchronizer(
					request.getConnectionPool(), 
					deployRegistry, 
					context.getSession()).run();
		// @formatter:on
	}
	
	
	public DbSchemaResponse synchronizeModelWithDbSchema(AccessRequestContext<SynchronizeModelWithDbSchema> context) {
		throw new UnsupportedOperationException("Method 'DbSchemaRequestProcessor.synchronizeModelWithDbSchema' is not supported!");
//		SynchronizeModelWithDbSchema request = context.getRequest();
//		HibernateAccess access = request.getAccess();
//		
//		return 
//			new ModelWithDbSchemaSynchronizer(
//				access.getMetaModel(),
//				access.getConnector().getDbSchemas(),
//				context.getSession(),
//				request.getResolveRelationships(),
//				request.getIgnoreUnsupportedTables()
//				).run();
	}
	
	public DbSchemaResponse createModelFromDbSchema(AccessRequestContext<CreateModelFromDbSchema> context) {
		throw new UnsupportedOperationException("Method 'DbSchemaRequestProcessor.createModelFromDbSchema' is not supported!");
//		CreateModelFromDbSchema request = context.getRequest();
//		HibernateAccess access = request.getAccess();
//		PersistenceGmSession session = context.getSession();
//		
//		CreateModel createModelRequest = CreateModel.T.create();
//		createModelRequest.setName(request.getName());
//		createModelRequest.setGroupId(request.getGroupId());
//		createModelRequest.setVersion(request.getVersion());
//		createModelRequest.setDependencies(request.getDependencies());
//		
//		CreateModelResponse createModelResponse = createModelRequest.eval(session).get();
//		if (createModelResponse instanceof ModelCreated) {
//			ModelCreated createdResponse = (ModelCreated) createModelResponse;
//			GmMetaModel model = createdResponse.getModel();
//			access.setMetaModel(model);
//			return 
//					new ModelWithDbSchemaSynchronizer(
//						access.getMetaModel(),
//						access.getConnector().getDbSchemas(),
//						context.getSession(),
//						request.getResolveRelationships(),
//						request.getIgnoreUnsupportedTables()
//						).run();
//		} 
//			
//		DbSchemaResponse response = DbSchemaResponse.T.create();
//		response.getNotifications().addAll(
//			Notifications
//				.build()
//				.add()
//					.message().confirmError("Could not create model from DB.")
//				.close()
//				.list()
//			);
//		
//		response.getNotifications().addAll(createModelResponse.getNotifications());
//		return response;
		
	}

}
