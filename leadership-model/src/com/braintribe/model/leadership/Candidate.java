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
package com.braintribe.model.leadership;

import java.util.Date;

import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.InstanceId;


public interface Candidate extends StandardStringIdentifiable {

	final EntityType<Candidate> T = EntityTypes.T(Candidate.class);

	public final static String domainId = "domainId";
	public final static String instanceId = "instanceId";
	public final static String candidateId = "candidateId";
	public final static String candidateType = "candidateType";
	public final static String priority = "priority";
	public final static String isLeader = "isLeader";
	public final static String pingTimestamp = "pingTimestamp";
	public final static String leadershipPingTimestamp = "leadershipPingTimestamp";
	
	void setDomainId(String domainId);
	String getDomainId();

	void setInstanceId(InstanceId instanceId);
	InstanceId getInstanceId();
	
	void setCandidateId(String candidateId);
	String getCandidateId();

	void setCandidateType(CandidateType candidateType);
	CandidateType getCandidateType();

	void setPriority(int priority);
	int getPriority();
	
	void setIsLeader(boolean isLeader);
	boolean getIsLeader();
	
	void setPingTimestamp(Date pingTimestamp);
	Date getPingTimestamp();

	void setLeadershipPingTimestamp(Date leadershipPingTimestamp);
	Date getLeadershipPingTimestamp();

}
