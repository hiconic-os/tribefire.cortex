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
package com.braintribe.model.processing.aspect.crypto.interceptor;

import java.util.HashMap;
import java.util.Map;

import com.braintribe.crypto.Cryptor;
import com.braintribe.crypto.Decryptor;
import com.braintribe.crypto.Encryptor;
import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.data.crypto.PropertyCrypting;
import com.braintribe.model.processing.aop.api.context.AroundContext;
import com.braintribe.model.processing.aop.api.interceptor.AroundInterceptor;
import com.braintribe.model.processing.aop.api.interceptor.InterceptionException;
import com.braintribe.model.processing.crypto.provider.CryptorProvider;
import com.braintribe.model.processing.meta.cmd.CmdResolver;

/**
 * <p>
 * Provides common functionalities to {@link CryptoInterceptorProcessor} concrete implementations.
 * 
 *
 * @param <I>
 *            {@link AroundInterceptor} standard input.
 * @param <O>
 *            {@link AroundInterceptor} standard output.
 */
public abstract class AbstractCryptoInterceptorProcessor<I, O> implements CryptoInterceptorProcessor<O> {

	protected AroundContext<I, O> aroundContext;
	protected CmdResolver metaDataResolver;

	protected CryptorProvider<Cryptor, PropertyCrypting> cryptorProvider;
	private Map<Object, Cryptor> cachedCryptors;
	private final boolean cacheEnabled;

	protected final Logger logger;
	protected final boolean isTraceLogEnabled;
	protected final boolean isDebugLogEnabled;

	protected AbstractCryptoInterceptorProcessor(CryptoInterceptorConfiguration cryptoInterceptorConfiguration, AroundContext<I, O> aroundContext, Logger log) {

		// pre assignment validations

		if (aroundContext == null) {
			throw new IllegalArgumentException(AroundContext.class.getName() + " argument cannot be null");
		}

		if (cryptoInterceptorConfiguration == null) {
			throw new IllegalArgumentException(CryptoInterceptorConfiguration.class.getName() + " argument cannot be null ");
		}

		if (log == null) {
			throw new IllegalArgumentException(Logger.class.getName() + " argument cannot be null ");
		}

		// assignments

		this.aroundContext = aroundContext;
		this.metaDataResolver = aroundContext.getSession().getModelAccessory().getCmdResolver();

		this.cryptorProvider = cryptoInterceptorConfiguration.getCryptorProvider();
		this.cacheEnabled = cryptoInterceptorConfiguration.isCacheCryptorsPerContext();

		if (cacheEnabled) {
			cachedCryptors = new HashMap<>();
		}

		// logging related assignments

		this.logger = log;
		this.isTraceLogEnabled = log.isTraceEnabled();
		this.isDebugLogEnabled = log.isDebugEnabled();

		// post assignment validations

		if (this.cryptorProvider == null) {
			throw new IllegalArgumentException("Given " + CryptoInterceptorConfiguration.class.getName() + " provided no " + CryptorProvider.class.getName());
		}

		if (this.metaDataResolver == null) {
			throw new IllegalArgumentException("Given " + AroundContext.class.getName() + " provided no " + CmdResolver.class.getName());
		}

	}

	public abstract boolean mustProcessRequest() throws CryptoInterceptionException;

	public abstract I processRequest() throws CryptoInterceptionException;

	public abstract boolean mustProcessResponse() throws CryptoInterceptionException;

	public abstract O processResponse(I request, O response) throws CryptoInterceptionException;

	@Override
	public O proceed() throws InterceptionException {

		I request = preProceed();

		O response = null;
		try {
			response = aroundContext.proceed(request);
		} catch (InterceptionException e) {
			throw e;
		} catch (Exception e) {
			throw new InterceptionException("Interceptor processing failed" + (e.getMessage() != null ? ": " + e.getMessage() : ""), e);
		}

		return postProceed(request, response);

	}

	public I preProceed() throws CryptoInterceptionException {
		if (mustProcessRequest()) {
			return processRequest();
		} else {
			return aroundContext.getRequest();
		}
	}

	public O postProceed(I request, O response) throws CryptoInterceptionException {
		if (mustProcessResponse()) {
			return processResponse(request, response);
		} else {
			return response;
		}
	}

	protected Encryptor mustEncrypt(String typeSignature, String propertyName) throws CryptoInterceptionException {

		EntityType<? extends GenericEntity> entityType =  GMF.getTypeReflection().getEntityType(typeSignature);

		return mustEncrypt(entityType, propertyName);

	}

	protected Encryptor mustEncrypt(EntityType<? extends GenericEntity> entityType, String propertyName) throws CryptoInterceptionException {

		Encryptor cryptor = null;

		if (cacheEnabled) {
			cryptor = getCryptorFromContextCache(Encryptor.class, entityType.getTypeSignature(), propertyName);
			if (cryptor != null) {
				return cryptor;
			}
		}

		PropertyCrypting propertyCrypting = resolvePropertyCryptingFor(entityType, propertyName);

		if (propertyCrypting == null) {
			return null;
		}

		cryptor = requireCryptorFor(Encryptor.class, propertyCrypting);

		if (cacheEnabled && cryptor != null) {
			saveCryptorToContextCache(entityType.getTypeSignature(), propertyName, cryptor);
		}

		return cryptor;

	}

	protected Decryptor mustDecrypt(EntityType<? extends GenericEntity> entityType, String propertyName) throws CryptoInterceptionException {

		if (entityType == null) {
			throw new IllegalArgumentException("entityType argument cannot be null");
		}

		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName argument be null");
		}

		Decryptor cryptor = null;

		if (cacheEnabled) {
			cryptor = getCryptorFromContextCache(Decryptor.class, entityType.getTypeSignature(), propertyName);
			if (cryptor != null) {
				return cryptor;
			}
		}

		PropertyCrypting propertyCrypting = resolvePropertyCryptingFor(entityType, propertyName);

		if (propertyCrypting == null) {
			return null;
		}

		cryptor = requireCryptorFor(Decryptor.class, propertyCrypting);

		if (cacheEnabled && cryptor != null) {
			saveCryptorToContextCache(entityType.getTypeSignature(), propertyName, cryptor);
		}

		return cryptor;

	}

	protected <T extends Cryptor> T getCryptorFromContextCache(Class<T> type, String typeSignature, String propertyName) {

		Object key = getContexCryptorCacheKey(typeSignature, propertyName);

		Cryptor cryptor = cachedCryptors.get(key);

		if (cryptor == null) {

			if (isTraceLogEnabled) {
				logger.trace("No " + Cryptor.class.getSimpleName() + " found in context cache for [ " + typeSignature + " ] property [ " + propertyName + " ]");
			}

			return null;

		} else {

			if (isDebugLogEnabled) {
				logger.debug("Found " + Cryptor.class.getSimpleName() + " in context cache for [ " + typeSignature + " ] property [ " + propertyName + " ]: [" + cryptor + "]");
			}

			if (!type.isInstance(cryptor)) {
				if (isDebugLogEnabled) {
					logger.debug("The [" + Cryptor.class.getSimpleName() + "] obtained from the cache (" + cryptor + ") for the requested type [" + typeSignature + "] and property [" + propertyName + "] is not compatible with the requested type [" + type.getName() + "]");
				}
				return null;
			}

			return type.cast(cryptor);

		}

	}

	/**
	 * <p>
	 * Obtains the {@link Cryptor} for the given {@link PropertyCrypting}.
	 * 
	 * <p>
	 * This method throws a {@link CryptoInterceptionException} if no {@link Cryptor} could be provided for the given
	 * {@link PropertyCrypting}, but simply returns null if the {@link Cryptor} associated with the
	 * {@link PropertyCrypting} is not compatible with the {@code type} argument.
	 * 
	 * @param type
	 *            {@code Cryptor} type to be checked for compatibility.
	 * @param propertyCrypting
	 *            {@link PropertyCrypting} for which a {@link Cryptor} must be retrieved.
	 * @return The {@link Cryptor} associated with the given {@link PropertyCrypting}, or {@code null} if the
	 *         {@link Cryptor} is not compatible with the given {@code type}.
	 * @throws CryptoInterceptionException
	 *             If no {@link Cryptor} could be provided for the given {@link PropertyCrypting}
	 */
	protected <T extends Cryptor> T requireCryptorFor(Class<T> type, PropertyCrypting propertyCrypting) throws CryptoInterceptionException {

		if (cryptorProvider == null) {
			throw new CryptoInterceptionException("Unable to obtain a cryptor as there is no cryptor provider configured to this context");
		}

		long s = 0;
		T cryptor = null;

		try {

			if (isTraceLogEnabled) {
				s = System.currentTimeMillis();
			}

			cryptor = cryptorProvider.provideFor(type, propertyCrypting);
			
			return cryptor;
			
		} catch (Exception e) {
			throw asCryptoInterceptionException("Failed to provide a cryptor", e);
		} finally {
			if (isTraceLogEnabled) {
				s = System.currentTimeMillis() - s;
				logger.trace("Obtaining a cryptor for [ " + propertyCrypting.getClass().getSimpleName() + " ] took " + s + " ms");
			}
		}

	}

	protected static CryptoInterceptionException asCryptoInterceptionException(String message, Throwable e) {
		return new CryptoInterceptionException(message + (e.getMessage() != null ? ": " + e.getMessage() : ""), e);
	}

	private Cryptor saveCryptorToContextCache(String typeSignature, String propertyName, Cryptor cryptor) {

		if (typeSignature == null) {
			throw new IllegalArgumentException("typeSignature argument cannot be null");
		}

		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName argument cannot be null");
		}

		if (cryptor == null) {
			throw new IllegalArgumentException("cryptor argument cannot be null");
		}

		Object key = getContexCryptorCacheKey(typeSignature, propertyName);

		Cryptor previousCryptor = cachedCryptors.put(key, cryptor);

		if (previousCryptor != null && cryptor != previousCryptor && logger.isWarnEnabled()) {
			logger.warn("This context already held a " + Cryptor.class + " for [" + key + "]. Check whether this context is being shared among threads.");
		}

		return previousCryptor;

	}

	private PropertyCrypting resolvePropertyCryptingFor(EntityType<? extends GenericEntity> entityType, String propertyName) throws CryptoInterceptionException {

		PropertyCrypting propertyCrypting = null;

		try {
			propertyCrypting = metaDataResolver.getMetaData().entityType(entityType).property(propertyName).meta(PropertyCrypting.T).exclusive();
		} catch (Exception e) {
			throw asCryptoInterceptionException("Failed to resolve an exclusive " + PropertyCrypting.class.getSimpleName() + " for [ " + entityType.getTypeSignature() + " ] property [ " + propertyName + " ]", e);
		}

		if (isDebugLogEnabled) {
			if (propertyCrypting != null) {
				logger.debug(PropertyCrypting.class.getSimpleName() + " resolved for [ " + entityType.getTypeSignature() + " ] property [ " + propertyName + " ]: [" + propertyCrypting + "]");
			} else if (isTraceLogEnabled) {
				logger.trace("No " + PropertyCrypting.class.getSimpleName() + " resolved for [ " + entityType.getTypeSignature() + " ] property [ " + propertyName + " ]");
			}

		}

		return propertyCrypting;

	}

	private static Object getContexCryptorCacheKey(String typeSignature, String propertyName) {
		return typeSignature + ":" + propertyName;
	}

}
