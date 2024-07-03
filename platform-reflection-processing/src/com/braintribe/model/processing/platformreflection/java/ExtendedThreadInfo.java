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

import java.lang.management.ThreadInfo;

public class ExtendedThreadInfo {
	  long cpuTimeInNanos;
      long blockedCount;
      long blockedTimeInMs;
      long waitedCount;
      long waitedTimeInMs;
      boolean deltaDone;
      ThreadInfo info;

      public ExtendedThreadInfo(long cpuTimeInNanos, ThreadInfo info) {
          blockedCount = info.getBlockedCount();
          blockedTimeInMs = info.getBlockedTime();
          waitedCount = info.getWaitedCount();
          waitedTimeInMs = info.getWaitedTime();
          this.cpuTimeInNanos = cpuTimeInNanos;
          this.info = info;
      }

      void setDelta(long cpuTime, ThreadInfo info) {
          if (deltaDone) throw new IllegalStateException("setDelta already called once");
          blockedCount = info.getBlockedCount() - blockedCount;
          blockedTimeInMs = info.getBlockedTime() - blockedTimeInMs;
          waitedCount = info.getWaitedCount() - waitedCount;
          waitedTimeInMs = info.getWaitedTime() - waitedTimeInMs;
          this.cpuTimeInNanos = cpuTime - this.cpuTimeInNanos;
          deltaDone = true;
          this.info = info;
      }
}
