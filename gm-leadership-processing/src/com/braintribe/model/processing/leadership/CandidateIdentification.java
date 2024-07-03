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

import com.braintribe.logging.Logger;
import com.braintribe.model.leadership.CandidateType;
import com.braintribe.model.service.api.InstanceId;
import com.braintribe.utils.DigestGenerator;

public class CandidateIdentification {

	private static final Logger logger = Logger.getLogger(CandidateIdentification.class);

	protected String domainId = null;
	protected InstanceId instanceId = null;
	protected String candidateId = null;
	protected CandidateType candidateType = null;

	public CandidateIdentification(String domainId, InstanceId instanceId, String candidateId, CandidateType candidateType) {
		this.instanceId = instanceId;
		this.candidateId = candidateId;
		if (candidateId == null) {
			throw new NullPointerException("The candidate ID must not be null.");
		}
		this.domainId = domainId;
		if (domainId == null) {
			throw new NullPointerException("The domain ID must not be null.");
		}
		this.domainId = truncateId(this.domainId);

		if (candidateType == null) {
			this.candidateType = CandidateType.Dbl;
		} else {
			this.candidateType = candidateType;
		}
	}

	protected String truncateId(String id) {
		if (id.length() > 240) {
			String md5;
			try {
				md5 = DigestGenerator.stringDigestAsString(id, "MD5");
			} catch (Exception e) {
				logger.error("Could not generate an MD5 sum of ID " + id, e);
				md5 = "";
			}
			String cutId = id.substring(0, 200);
			String newId = cutId.concat("#").concat(md5);
			return newId;
		}
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CandidateIdentification)) {
			return false;
		}
		CandidateIdentification other = (CandidateIdentification) obj;
		return this.candidateId.equals(other.candidateId);
	}

	@Override
	public int hashCode() {
		return this.candidateId.hashCode();
	}

	public String getDomainId() {
		return domainId;
	}
	public InstanceId getInstanceId() {
		return instanceId;
	}
	public String getCandidateId() {
		return candidateId;
	}

	public CandidateType getCandidateType() {
		return candidateType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.candidateId);
		sb.append(" (domain: ");
		sb.append(this.domainId);
		if (instanceId != null) {
			sb.append(", instance: ");
			sb.append(this.instanceId);
			sb.append(")");
		}
		return sb.toString();
	}
}
