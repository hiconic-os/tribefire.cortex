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
package com.braintribe.model.processing.securityservice.basic.test.wire.space;

import static com.braintribe.wire.api.util.Lists.list;
import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.map;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.function.Supplier;

import com.braintribe.exception.Exceptions;
import com.braintribe.execution.CustomThreadFactory;
import com.braintribe.execution.ExecutorServiceSupplier;
import com.braintribe.model.processing.securityservice.api.UserSessionService;
import com.braintribe.model.processing.securityservice.basic.test.wire.space.access.UserSessionsAccessSpace;
import com.braintribe.model.processing.securityservice.basic.test.wire.space.access.UserStatisticsAccessSpace;
import com.braintribe.model.processing.securityservice.usersession.service.AccessUserSessionService;
import com.braintribe.model.processing.securityservice.usersession.service.UserSessionIdProvider;
import com.braintribe.model.processing.time.TimeSpanCodec;
import com.braintribe.model.time.TimeSpan;
import com.braintribe.model.time.TimeUnit;
import com.braintribe.model.user.Role;
import com.braintribe.model.user.User;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.model.usersession.UserSessionType;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class UserSessionServiceSpace implements WireSpace {

	@Import
	private UserSessionsAccessSpace userSessionAccess;

	@Import
	private UserStatisticsAccessSpace userStatisticsAccess;
	
	@Import
	private AuthContextSpace authContext;

	@Managed
	public UserSessionService service() {
		AccessUserSessionService bean = new AccessUserSessionService();
		
		// general
		bean.setPersistenceUserSessionGmSessionProvider(userSessionAccess::lowLevelSession);
		bean.setSessionIdProvider(userSessionIdFactory());
		bean.setDefaultUserSessionMaxIdleTime(defaultMaxIdleTime());

		// internal user sessions
		// @formatter:off
		bean.setInternalUserSessionHolders(
				list(
					authContext.internalUser().userSessionProvider()
				)
		);
		// @formatter:on

		return bean;
	}

	@Managed
	public Supplier<ScheduledExecutorService> scheduledExecutorSupplier() {

		ScheduledExecutorService executor = scheduledExecutor();

		// @formatter:off
		ExecutorServiceSupplier<ScheduledExecutorService> bean = 
				ExecutorServiceSupplier
					.<ScheduledExecutorService> create()
						.id(executorId())
						.awaitTermination(10)
						.executor(executor)
						.shutdown(executor::shutdown);
		// @formatter:on

		return bean;

	}

	@Managed
	public ScheduledExecutorService scheduledExecutor() {
		// @formatter:off
		ScheduledThreadPoolExecutor bean = 
				new ScheduledThreadPoolExecutor(
						10, 				// corePoolSize
						threadFactory(), 	// threadFactory
						new AbortPolicy()	// rejectedExecutionHandler
				);
		return bean;
		// @formatter:on
	}

	@Managed
	public ThreadFactory threadFactory() {
		return CustomThreadFactory.create().namePrefix(executorId() + "-");
	}

	@Managed
	public String executorId() {
		return "test.tribefire.system.executor";
	}
	
	@Managed
	public UserSessionIdProvider userSessionIdFactory() {
		// @formatter:off
		UserSessionIdProvider bean = new UserSessionIdProvider();
		bean.setTypePrefixes(
				map(
					entry(UserSessionType.internal, "i-"),
					entry(UserSessionType.trusted, "t-")
				)
			);
		return bean;
		// @formatter:on
	}

	public TimeSpan defaultMaxIdleTime() {
		return standardMaxIdleTime();
	}

	@Managed
	public TimeSpan standardMaxIdleTime() {
		TimeSpan bean = TimeSpan.T.create();
		bean.setUnit(TimeUnit.hour);
		bean.setValue(24.0);
		return bean;
	}

	@Managed
	public TimeSpan internalMaxIdleTime() {
		TimeSpan bean = TimeSpan.T.create();
		bean.setUnit(TimeUnit.hour);
		bean.setValue(24.0);
		return bean;
	}

	@Managed
	public TimeSpan internalRecyclingInterval() {
		TimeSpan bean = TimeSpan.T.create();
		bean.setUnit(TimeUnit.hour);
		bean.setValue(12.0);
		return bean;
	}

	@Managed
	public TimeSpanCodec timeSpanCodec() {
		TimeSpanCodec bean = new TimeSpanCodec();
		return bean;
	}

	public UserSession initialUserSession(User user) {

		UserSession bean = UserSession.T.create();

		String userSessionId = null;
		try {
			userSessionId = userSessionIdFactory().apply(UserSessionType.internal);
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Failed to generate an user session id");
		}

		Set<String> effectiveRoles = new HashSet<>();
		effectiveRoles.add("$all");
		effectiveRoles.add("$user-" + user.getName());
		for (Role userRole : user.getRoles()) {
			effectiveRoles.add(userRole.getName());
		}

		Date now = new Date();

		bean.setSessionId(userSessionId);
		bean.setType(UserSessionType.internal);
		bean.setCreationInternetAddress("0:0:0:0:0:0:0:1");
		bean.setCreationDate(now);
		bean.setLastAccessedDate(now);
		bean.setUser(user);
		bean.setEffectiveRoles(effectiveRoles);

		return bean;

	}

}
