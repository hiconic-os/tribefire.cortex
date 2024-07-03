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
package com.braintribe.model.processing.securityservice.usersession.cleanup;

import java.util.concurrent.Future;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.codec.Codec;
import com.braintribe.gm.model.user_session_service.CleanupUserSessions;
import com.braintribe.logging.Logger;
import com.braintribe.model.extensiondeployment.HardwiredWorker;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.time.TimeSpanCodec;
import com.braintribe.model.processing.worker.api.Worker;
import com.braintribe.model.processing.worker.api.WorkerContext;
import com.braintribe.model.processing.worker.api.WorkerException;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.time.TimeSpan;
import com.braintribe.model.time.TimeUnit;

public class UserSessionCleanupWorker implements Worker, Runnable {

	private boolean run;
	private Future<?> workerFuture;
	
	private Evaluator<ServiceRequest> requestEvaluator;
	
	private boolean enableUserSessionCleanup;
	private TimeSpan userSessionCleanupIntervalStart = millisToTimeSpan(600000);
	private TimeSpan userSessionCleanupInterval = millisToTimeSpan(600000);

	private static final Codec<TimeSpan, Double> timeSpanCodec = new TimeSpanCodec();
	
	private static final Logger log = Logger.getLogger(UserSessionCleanupWorker.class);

	@Configurable @Required
	public void setRequestEvaluator(Evaluator<ServiceRequest> requestEvaluator) {
		this.requestEvaluator = requestEvaluator;
	}

	@Configurable
	public void setEnableUserSessionCleanup(boolean enableUserSessionCleanup) {
		this.enableUserSessionCleanup = enableUserSessionCleanup;
	}

	/**
	 * @param userSessionCleanupIntervalStart
	 *            Defaults to 600000 ms (10 minutes).
	 */
	@Configurable
	public void setUserSessionCleanupIntervalStart(TimeSpan userSessionCleanupIntervalStart) {
		this.userSessionCleanupIntervalStart = userSessionCleanupIntervalStart;
	}

	/**
	 * @param userSessionCleanupInterval
	 *            Defaults to 600000 ms (10 minutes).
	 */
	@Configurable
	public void setUserSessionCleanupInterval(TimeSpan userSessionCleanupInterval) {
		this.userSessionCleanupInterval = userSessionCleanupInterval;
	}
	
	@Override
	public GenericEntity getWorkerIdentification() {
		HardwiredWorker hw = HardwiredWorker.T.create();
		hw.setId(getClass().getName());
		return hw;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void start(WorkerContext workerContext) throws WorkerException {
		if (enableUserSessionCleanup) {
			run = true;
			workerFuture = workerContext.submit(this);
		} else {
			log.warn(() -> "Suppressed the user session cleanup worker start-up as it is disabled");
		}
	}
	
	@Override
	public void stop(WorkerContext workerContext) throws WorkerException {
		run = false;
		if (workerFuture != null) {
			workerFuture.cancel(true);
		}
		workerFuture = null;
	}
	
	@Override
	public void run() {
		Long intervalStartInMillis = timeSpanToMillis(userSessionCleanupIntervalStart);
		Long intervalInMillis = timeSpanToMillis(userSessionCleanupInterval);

		log.debug(() -> "Scheduled user session cleanup to execute every " + intervalInMillis + " ms; Starting in " + intervalStartInMillis + " ms");
		
		try {
			Thread.sleep(intervalStartInMillis);
		} catch (InterruptedException e) {
			log.debug(() -> "User session cleanup worker got interrupted");
			run = false;
		}
		
		while (run) {
			try {
				CleanupUserSessions.T.create().eval(requestEvaluator).get();
			} catch (Exception e) {
				log.error(() -> "Failed to cleanup invalid user sessions", e);
			} finally {
				try {
					Thread.sleep(intervalInMillis);
				} catch (InterruptedException e) {
					log.debug(() -> "User session cleanup worker got interrupted");
					run = false;
				}
			}
		}
		log.warn(() -> "User session cleanup has stopped");
	}

	private Long timeSpanToMillis(TimeSpan timeSpan) {
		if (timeSpan == null) {
			return null;
		}
		try {
			return timeSpanCodec.encode(timeSpan).longValue();
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert '" + timeSpan + "' to milliseconds", e);
		}
	}

	private TimeSpan millisToTimeSpan(long millis) {
		TimeSpan maxIdleTime = TimeSpan.T.create();
		maxIdleTime.setUnit(TimeUnit.milliSecond);
		maxIdleTime.setValue(millis);
		return maxIdleTime;
	}
	
}
