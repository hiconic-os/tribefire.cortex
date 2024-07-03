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
package tribefire.platform.impl.deployment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.function.Supplier;

import com.braintribe.logging.Logger;

public class DynamicDeployableProxy<T> implements InvocationHandler {
	
	private static final Logger log = Logger.getLogger(DynamicDeployableProxy.class);
	
	private Class<T> serviceInterface;
	private T defaultDelegate;
	private Supplier<T> dynamicDelegateProvider;
	
	private DynamicDeployableProxy(Class<T> serviceInterface, T defaultDelegate, Supplier<T> dynamicDelegateProvider) {
		this.serviceInterface = serviceInterface;
		this.defaultDelegate = defaultDelegate;
		this.dynamicDelegateProvider = dynamicDelegateProvider;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		T delegate = (dynamicDelegateProvider != null) ? dynamicDelegateProvider.get() : defaultDelegate;
		
		if (delegate == null) {
			delegate = defaultDelegate;
		}
		
		if (log.isTraceEnabled()) {
			if (delegate == defaultDelegate) {
				log.trace("Invoking [ "+serviceInterface+" ] proxy with default delegate [ "+delegate+" ]");
			} else {
				log.trace("Invoking [ "+serviceInterface+" ] proxy with dynamic delegate [ "+delegate+" ]");
			}
		}
		
		try {
			return method.invoke(delegate, args);
		} catch (UndeclaredThrowableException | InvocationTargetException ex) {
			// avoid unnecessary exception wrapping 
			throw ex.getCause();
		}
	}
	
	public static <T> T create(Class<T> serviceInterface, T defaultDelegate, Supplier<T> dynamicDelegateProvider) {
		DynamicDeployableProxy<T> invocationHandler = new DynamicDeployableProxy<T>(serviceInterface, defaultDelegate, dynamicDelegateProvider);
		@SuppressWarnings("unchecked")
		T proxy = (T)Proxy.newProxyInstance(DynamicDeployableProxy.class.getClassLoader(), new Class<?>[]{serviceInterface}, invocationHandler);
		return proxy;
	}
	
	public static <T> T create(Class<T> serviceInterface, T defaultDelegate) {
		return create(serviceInterface, defaultDelegate, null);
	}
	
}
