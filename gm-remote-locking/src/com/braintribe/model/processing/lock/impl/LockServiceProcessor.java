// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.lock.impl;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.processing.lock.api.LockManager;
import com.braintribe.model.processing.lock.api.LockService;
import com.braintribe.model.processing.lock.api.model.LockRequest;
import com.braintribe.model.processing.lock.api.model.TryLock;
import com.braintribe.model.processing.lock.api.model.Unlock;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.service.api.result.Neutral;

public class LockServiceProcessor extends AbstractDispatchingServiceProcessor<LockRequest, Object>  {

	private final LockService lockService;
	
	LockServiceProcessor(LockManager lockManager){
		InternalizingLockService internalizingLockService = new InternalizingLockService();
		internalizingLockService.setLockManager(lockManager);
		
		lockService = internalizingLockService;
	}
	
	LockServiceProcessor(LockService lockService){
		this.lockService = lockService;
	}
	
	@Override
	protected void configureDispatching(DispatchConfiguration<LockRequest, Object> dispatching) {
		dispatching.register(TryLock.T, (c,r) -> tryLock(r));
		dispatching.register(Unlock.T, (c,r) -> unlock(r));
	}
	
	private boolean tryLock(TryLock r) {
		try {
			return lockService.tryLock(
					r.getIdentification(), 
					r.getHolderId(), 
					r.getCallerSignature(), 
					r.getMachineSignature(), 
					r.getExclusive(), 
					r.getTimeout(),
					r.getLockTtl());
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}
	
	private Neutral unlock(Unlock r) {
		try {
			lockService.unlock(
					r.getIdentification(), 
					r.getHolderId(), 
					r.getCallerSignature(), 
					r.getMachineSignature(), 
					r.getExclusive());
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
		
		return Neutral.NEUTRAL;
	}

}
