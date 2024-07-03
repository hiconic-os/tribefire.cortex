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
package tribefire.platform.impl.gmql.wire.space;

import static tribefire.platform.impl.gmql.GmqlProcessorTestCommons.createPerson;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.braintribe.gm.service.wire.common.contract.CommonServiceProcessingContract;
import com.braintribe.gm.service.wire.common.contract.ServiceProcessingConfigurationContract;
import com.braintribe.model.accessapi.GmqlRequest;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.model.processing.smood.Smood;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.util.meta.NewMetaModelGeneration;
import com.braintribe.testing.model.test.demo.person.Person;
import com.braintribe.utils.CollectionTools;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;

import tribefire.platform.impl.gmql.TestBasicPersistenceGmSessionFactory;
import tribefire.platform.impl.gmql.wire.contract.GmqlProcessorTestContract;
import tribefire.platform.impl.gmql.GmqlProcessor;

@Managed
public class GmqlProcessorTestSpace implements GmqlProcessorTestContract {
	@Import
	private ServiceProcessingConfigurationContract serviceProcessingConfiguration;
	
	@Import
	private CommonServiceProcessingContract commonServiceProcessing;
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		serviceProcessingConfiguration.registerServiceConfigurer(this::configureServices);
	}
	
	private void configureServices(ConfigurableDispatchingServiceProcessor bean) {
		bean.removeInterceptor("auth");
		bean.register(GmqlRequest.T, gmqlProcessor());
	}
	
	private GmqlProcessor gmqlProcessor() {
		GmqlProcessor bean = new GmqlProcessor();
		bean.setSessionFactory(sessionFactory());
		return bean;
	}
	
	@Override
	public Evaluator<ServiceRequest> evaluator() {
		return commonServiceProcessing.evaluator();
	}
	
	@Managed
	private TestBasicPersistenceGmSessionFactory sessionFactory() {
		TestBasicPersistenceGmSessionFactory bean = new TestBasicPersistenceGmSessionFactory();
		bean.reset(smood());
		return bean;
	}
	
	private static GmMetaModel model(String name, EntityType<?>... entities) {
		return new NewMetaModelGeneration().buildMetaModel(name, CollectionTools.getList(entities));
	}
	
	@Managed
	@Override
	public Smood smood() {
		Smood smood = new Smood(new ReentrantReadWriteLock());
		smood.setAccessId("test.access");
		
		GmMetaModel personModel = model("PersonModel", Person.T);
		
		smood.setMetaModel(personModel);
		
		Person person1 = createPerson("Foo");
		smood.registerEntity(person1, true);
		
		return smood;
	}
	

	



}
