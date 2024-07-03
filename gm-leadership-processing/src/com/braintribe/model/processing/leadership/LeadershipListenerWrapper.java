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
package com.braintribe.model.processing.leadership;

import com.braintribe.model.processing.leadership.api.LeadershipHandle;
import com.braintribe.model.processing.leadership.api.LeadershipListener;
import com.braintribe.model.processing.leadership.api.LifeSignProvider;

public class LeadershipListenerWrapper implements LifeSignProvider {

	protected LeadershipListener leadershipListener = null;
	protected boolean leadershipAware = false;

	public LeadershipListenerWrapper(LeadershipListener leadershipListener) {
		this.leadershipListener = leadershipListener;
	}

	public boolean isLeadershipAware() {
		return leadershipAware;
	}
	public void setLeadershipAware(boolean leadershipAware) {
		this.leadershipAware = leadershipAware;
	}

	public LeadershipListener getLeadershipListener() {
		return leadershipListener;
	}

	public void onLeadershipGranted(LeadershipHandle handle) {
		if (this.leadershipAware) {
			return;
		}
		this.leadershipAware = true;
		this.leadershipListener.onLeadershipGranted(handle);
	}
	public void surrenderLeadership(LeadershipHandle handle) {
		if (!this.leadershipAware) {
			return;
		}
		this.leadershipListener.surrenderLeadership(handle);
	}

	@Override
	public boolean isWorkingAsLeader(String candidateId) {
		if (this.leadershipListener instanceof LifeSignProvider) {
			boolean isAlive = ((LifeSignProvider) this.leadershipListener).isWorkingAsLeader(candidateId);
			this.leadershipAware = isAlive;
			return isAlive;
		}
		return this.leadershipAware;
	}

	@Override
	public boolean isAvailableAsCandidate(String candidateId) {
		if (this.leadershipListener instanceof LifeSignProvider) {
			boolean isAvailable = ((LifeSignProvider) this.leadershipListener).isAvailableAsCandidate(candidateId);
			return isAvailable;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Wrapper (leadershipAware: " + leadershipAware + ") of: " + leadershipListener;
	}
}
