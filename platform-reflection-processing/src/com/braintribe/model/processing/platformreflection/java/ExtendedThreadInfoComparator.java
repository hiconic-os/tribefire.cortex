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
package com.braintribe.model.processing.platformreflection.java;

import java.util.Comparator;

public class ExtendedThreadInfoComparator implements Comparator<ExtendedThreadInfo> {
	
	private String type;

	public ExtendedThreadInfoComparator(String type) {
		this.type = type;
	}

	@Override
	public int compare(ExtendedThreadInfo o1, ExtendedThreadInfo o2) {
		 if ("cpu".equals(type)) {
             return (int) (o2.cpuTimeInNanos - o1.cpuTimeInNanos);
         } else if ("wait".equals(type)) {
             return (int) (o2.waitedTimeInMs - o1.waitedTimeInMs);
         } else if ("block".equals(type)) {
             return (int) (o2.blockedTimeInMs - o1.blockedTimeInMs);
         }
         throw new IllegalArgumentException("expected thread type to be either 'cpu', 'wait', or 'block', but was " + type);
	}

}
