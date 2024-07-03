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
package tribefire.platform.impl.deployment.proxy;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.accessdeployment.smood.CollaborativeSmoodAccess;
import com.braintribe.model.extensiondeployment.StateChangeProcessor;
import com.braintribe.model.processing.deployment.api.DcProxy;
import com.braintribe.model.processing.deployment.api.DcProxyListener;
import com.braintribe.model.processing.deployment.api.DeploymentException;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.testing.junit.assertions.assertj.core.api.Assertions;

import tribefire.platform.impl.deployment.BasicDeployRegistry;

public class DcProxyFactoryTest {
	public interface A {
		String foo();
		String bar();
	}

	public interface B {
		String fix();
		String fox();
	}

	public interface C {
		String fix();
		String foo();
	}

	static class ABC implements A, B, C {
		// @formatter:off
		@Override public String fix() { return "fix"; }
		@Override public String fox() { return "fox"; }
		@Override public String foo() { return "foo"; }
		@Override public String bar() { return "bar"; }
		// @formatter:on
	}
	
	@Test
	public void testInterface() throws Exception {
		InstanceId instanceId = InstanceId.of("#1", "test");
		BasicDeployRegistry registry = new BasicDeployRegistry();

		// PGA: I guess this is testing no exception happens?
		DcProxyFactory.forInterfaces(Collections.singletonList(IncrementalAccess.class), "egal", CollaborativeSmoodAccess.T, registry, instanceId);
	}

	@Test
	public void testMultInterface() throws Exception {
		DcProxy proxy = createProxy(asList(A.class, B.class, C.class));
		setDefaultDelegate(proxy, new ABC());

		A a = (A) proxy;
		B b = (B) proxy;
		C c = (C) proxy;

		assertThat(a.foo()).isEqualTo("foo");
		assertThat(a.bar()).isEqualTo("bar");
		assertThat(b.fix()).isEqualTo("fix");
		assertThat(b.fox()).isEqualTo("fox");
		assertThat(c.fix()).isEqualTo("fix");
		assertThat(c.foo()).isEqualTo("foo");

	}

	@Test(expected = DeploymentException.class)
	public void ifaceProxy() throws Exception {
		TestInterface proxy = createProxy(TestInterface.class);

		proxy.getFoo();
	}

	@Test(expected = DeploymentException.class)
	public void ifaceProxy2() throws Exception {
		TestInterface2 proxy = createProxy(TestInterface2.class);

		proxy.getBar();
	}

	@Test(expected = DeploymentException.class)
	public void ifaceProxy3() throws Exception {
		TestInterface3 proxy = createProxy(TestInterface3.class);

		proxy.getFoo();
	}

	public interface TestInterface {
		boolean getFoo();
	}

	public interface TestInterface2 {
		boolean getFoo(String test);
		long getBar();
		void longTest(long t);
		void intTest(int t);
		void floatTest(float t);
		void doubleTest(double t);
		void shortTest(short t);
		void byteTest(byte t);
		void booleanTest(boolean t);
		void objectTest(Object t);
	}

	public interface TestInterface3 {
		void getFoo();
	}

	@Test
	public void testProblem() {
		DelegatedInterface proxy = DcProxyFactory.forInterface(DelegatedInterface.class, "externalId", StateChangeProcessor.T, deployRegistry(),
				instanceId());

		setDefaultDelegate(proxy, data -> data);

		String array[] = {};

		Assertions.assertThat(proxy.provide(array)).as("proxy did not correctly address delegate").isSameAs(array);
	}

	@Test
	public void classProxy() throws Exception {
		DelegatedClass regularInstance = new DelegatedClass();
		DelegatedClass proxy = createProxy(DelegatedClass.class);

		setDefaultDelegate(proxy, regularInstance);

		assertThat(proxy.foobar()).isEqualTo(regularInstance.foobar());
		assertThat(proxy.toString()).isEqualTo(regularInstance.toString());

		proxy.setFinalFoobar("ABC"); // this value will be delegated
		assertThat(regularInstance.getFinalFoobar()).isEqualTo("ABC"); // so regular instance has the ABC value set
		assertThat(proxy.getFinalFoobar()).isNull(); // this final getter is not delegated, thus the value is null
	}

	// #####################################################
	// ## . . . . . . . . DcProxyListener . . . . . . . . ##
	// #####################################################

	@Test
	public void proxyListeners() {
		final String[] DELEGATE = {};

		TestDcProxyListener listener = new TestDcProxyListener();
		DelegatedInterface proxy = DcProxyFactory.forInterface( //
				DelegatedInterface.class, "externalId", StateChangeProcessor.T, deployRegistry(), instanceId());

		DcProxy.getConfigurableDelegateManager(proxy).addDcProxyListener(listener);

		setDefaultDelegate(proxy, DELEGATE);
		assertThat(listener.defaultDelegate).isSameAs(DELEGATE);
	}

	// @formatter:off
	static  class TestDcProxyListener implements DcProxyListener {
		public Object defaultDelegate;
		public Object delegate;
		@Override public void onDefaultDelegateSet(Object defaultDelegate) { this.defaultDelegate = defaultDelegate; }
		@Override public void onDelegateSet(Object delegate) { this.delegate = delegate; }
		@Override public void onDelegateCleared(Object delegate) { this.delegate = null; }
	}
	// @formatter:on

	private <T> T createProxy(Class<T> iface) {
		try {
			return DcProxyFactory.forInterface(iface, "test", StateChangeProcessor.T, deployRegistry(), instanceId());
		} catch (DeploymentException e) {
			// wrap it so tests which expect a DeploymentException still fail if the exception happens here
			throw new RuntimeException("Error while creating proxy", e);
		}
	}

	private DcProxy createProxy(List<Class<?>> ifaces) {
		return DcProxyFactory.forInterfaces(ifaces, "test", StateChangeProcessor.T, deployRegistry(), instanceId());
	}

	private BasicDeployRegistry deployRegistry() {
		return new BasicDeployRegistry();
	}

	protected InstanceId instanceId() {
		InstanceId instanceId = InstanceId.T.create();
		instanceId.setApplicationId("test-app");
		instanceId.setNodeId("127.0.0.1");
		return instanceId;
	}

	private <T> void setDefaultDelegate(T proxy, T delegate) {
		DcProxy.getConfigurableDelegateManager(proxy).setDefaultDelegate(delegate);
	}

}
