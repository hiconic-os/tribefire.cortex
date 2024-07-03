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
package tribefire.platform.api.binding;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.model.accessapi.AccessRequest;
import com.braintribe.model.accessdeployment.CollaborativeAccess;
import com.braintribe.model.dcsadeployment.DcsaSharedStorage;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionInfoProvider;
import com.braintribe.model.deployment.database.pool.DatabaseConnectionPool;
import com.braintribe.model.extensiondeployment.BinaryPersistence;
import com.braintribe.model.extensiondeployment.BinaryRetrieval;
import com.braintribe.model.extensiondeployment.ResourceEnricher;
import com.braintribe.model.extensiondeployment.StateChangeProcessorRule;
import com.braintribe.model.extensiondeployment.WebTerminal;
import com.braintribe.model.extensiondeployment.check.CheckProcessor;
import com.braintribe.model.extensiondeployment.check.ParameterizedCheckProcessor;
import com.braintribe.model.leadershipdeployment.LeadershipManager;
import com.braintribe.model.lockingdeployment.LockManager;
import com.braintribe.model.messagingdeployment.Messaging;
import com.braintribe.model.processing.accessrequest.api.AccessRequestProcessor;
import com.braintribe.model.processing.deployment.api.ComponentBinder;
import com.braintribe.model.processing.securityservice.api.UserSessionService;
import com.braintribe.model.processing.service.api.ServiceAroundProcessor;
import com.braintribe.model.processing.service.api.ServicePostProcessor;
import com.braintribe.model.processing.service.api.ServicePreProcessor;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.sp.api.StateChangeProcessor;
import com.braintribe.model.processing.worker.api.Worker;
import com.braintribe.model.resourceapi.enrichment.EnrichResource;
import com.braintribe.model.resourceapi.enrichment.EnrichResourceResponse;
import com.braintribe.model.resourceapi.persistence.BinaryPersistenceRequest;
import com.braintribe.model.resourceapi.persistence.BinaryPersistenceResponse;
import com.braintribe.model.resourceapi.stream.BinaryRetrievalRequest;
import com.braintribe.model.resourceapi.stream.BinaryRetrievalResponse;
import com.braintribe.transport.messaging.api.MessagingConnectionProvider;

import tribefire.cortex.model.deployment.mimetypedetection.MimeTypeDetector;

/**
 * Defines the basic {@link com.braintribe.model.processing.deployment.api.ComponentBinder ComponentBinders} which are essential for the platform
 * internals and thus available to every extension cartridge.
 */
public interface ComponentBinders {

	ComponentBinder<com.braintribe.model.extensiondeployment.ServiceProcessor, ServiceProcessor<?, ?>> serviceProcessor();

	ComponentBinder<com.braintribe.model.extensiondeployment.ServicePreProcessor, ServicePreProcessor<?>> servicePreProcessor();

	ComponentBinder<com.braintribe.model.extensiondeployment.ServiceAroundProcessor, ServiceAroundProcessor<?, ?>> serviceAroundProcessor();

	ComponentBinder<com.braintribe.model.extensiondeployment.ServicePostProcessor, ServicePostProcessor<?>> servicePostProcessor();

	ComponentBinder<com.braintribe.model.extensiondeployment.access.AccessRequestProcessor, AccessRequestProcessor<?, ?>> accessRequestProcessor();

	ComponentBinder<BinaryPersistence, ServiceProcessor<? super BinaryPersistenceRequest, ? super BinaryPersistenceResponse>> binaryPersistenceProcessor();

	ComponentBinder<BinaryRetrieval, ServiceProcessor<? super BinaryRetrievalRequest, ? super BinaryRetrievalResponse>> binaryRetrievalProcessor();

	ComponentBinder<ResourceEnricher, ServiceProcessor<? super EnrichResource, ? super EnrichResourceResponse>> resourceEnricherProcessor();

	ComponentBinder<com.braintribe.model.marshallerdeployment.Marshaller, Marshaller> marshaller();

	ComponentBinder<Messaging, MessagingConnectionProvider<?>> messaging();

	ComponentBinder<MimeTypeDetector, com.braintribe.mimetype.MimeTypeDetector> mimeTypeDetector();

	ComponentBinder<LockManager, com.braintribe.model.processing.lock.api.LockManager> lockingManager();

	ComponentBinder<LeadershipManager, tribefire.cortex.leadership.api.LeadershipManager> leadershipManager();

	ComponentBinder<DcsaSharedStorage, com.braintribe.model.access.collaboration.distributed.api.DcsaSharedStorage> dcsaSharedStorage();

	ComponentBinder<tribefire.cortex.model.deployment.usersession.service.UserSessionService, UserSessionService> userSessionService();

	ComponentBinder<DatabaseConnectionPool, DataSource> databaseConnectionPool();

	ComponentBinder<DatabaseConnectionInfoProvider, tribefire.module.api.DatabaseConnectionInfoProvider> databaseConnectionInfoProvider();

	ComponentBinder<com.braintribe.model.extensiondeployment.Worker, Worker> worker();

	ComponentBinder<WebTerminal, HttpServlet> webTerminal();

	ComponentBinder<com.braintribe.model.extensiondeployment.StateChangeProcessor, StateChangeProcessor<?, ?>> stateChangeProcessor();

	ComponentBinder<StateChangeProcessorRule, com.braintribe.model.processing.sp.api.StateChangeProcessorRule> stateChangeProcessorRule();

	ComponentBinder<CheckProcessor, com.braintribe.model.processing.check.api.CheckProcessor> checkProcessor();

	ComponentBinder<ParameterizedCheckProcessor, com.braintribe.model.processing.check.api.ParameterizedCheckProcessor<?>> parameterizedCheckProcessor();

	ComponentBinder<ParameterizedCheckProcessor, com.braintribe.model.processing.check.api.ParameterizedAccessCheckProcessor<? extends AccessRequest>> parameterizedAccessCheckProcessor();

	ComponentBinder<CollaborativeAccess, com.braintribe.model.processing.session.api.collaboration.CollaborativeAccess> collaborativeAccess();

}