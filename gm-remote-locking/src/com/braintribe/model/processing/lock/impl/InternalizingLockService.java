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
package com.braintribe.model.processing.lock.impl;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import com.braintribe.cfg.Required;
import com.braintribe.model.processing.lock.api.LockBuilder;
import com.braintribe.model.processing.lock.api.LockManager;
import com.braintribe.model.processing.lock.api.LockService;
import com.braintribe.model.processing.lock.api.LockServiceException;
import com.braintribe.model.processing.time.TimeSpanConversion;
import com.braintribe.model.time.TimeSpan;

public class InternalizingLockService implements LockService {
	private LockManager lockManager;
	
	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Override
	public boolean tryLock(String identification, String holderId, String callerSignature, String machineSignature, boolean exclusive, TimeSpan timeout) throws LockServiceException, InterruptedException {
		return tryLock(identification, holderId, callerSignature, machineSignature, exclusive, timeout, null);
	}
	
	
	@Override
	public boolean tryLock(String identification, String holderId, String callerSignature, String machineSignature, boolean exclusive, TimeSpan timeout, TimeSpan lockTtl) throws LockServiceException, InterruptedException {
		
		long lockTtlInMs = -1L;
		if (lockTtl != null) {
			Duration duration = TimeSpanConversion.getDuration(lockTtl);
			lockTtlInMs = duration.toMillis();
		}
		
		LockBuilder lockBuilder = lockManager
				.forIdentifier(identification)
				.holderId(holderId)
				.caller(callerSignature)
				.machine(machineSignature)
				.lockTtl(lockTtlInMs, TimeUnit.MILLISECONDS);
		
		Lock lock = exclusive? lockBuilder.exclusive(): lockBuilder.shared();

		long convertedTimeout = (long)TimeSpanConversion.fromTimeSpan(timeout).unit(com.braintribe.model.time.TimeUnit.milliSecond).toValue();
		
		boolean lockAcquired = lock.tryLock(convertedTimeout, TimeUnit.MILLISECONDS);
		
		return lockAcquired;
	}

	@Override
	public void unlock(String identification, String holderId, String callerSignature, String machineSignature, boolean exclusive) throws LockServiceException {
		LockBuilder lockBuilder = lockManager.forIdentifier(identification).holderId(holderId).caller(callerSignature).machine(machineSignature);
		Lock lock = exclusive? lockBuilder.exclusive(): lockBuilder.shared();

		lock.unlock();
	}

}
