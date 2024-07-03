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

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.braintribe.model.platformreflection.hotthreads.HotThread;
import com.braintribe.model.platformreflection.hotthreads.HotThreads;
import com.braintribe.model.platformreflection.hotthreads.StackTraceElement;
import com.braintribe.utils.DateTools;

public class PlatformReflectionTools {

	public static com.braintribe.model.platformreflection.hotthreads.StackTraceElement convertStackTraceElement(java.lang.StackTraceElement stackTraceElement) {
		
		StackTraceElement ste = com.braintribe.model.platformreflection.hotthreads.StackTraceElement.T.create();
		ste.setDeclaringClass(stackTraceElement.getClassName());
		ste.setFileName(stackTraceElement.getFileName());
		ste.setMethodName(stackTraceElement.getMethodName());
		ste.setLineNumber(stackTraceElement.getLineNumber());
		return ste;
		
	}
	
	public static String toString(com.braintribe.model.platformreflection.hotthreads.StackTraceElement stackTraceElement) {
		int lineNumber = stackTraceElement.getLineNumber();
		String fileName = stackTraceElement.getFileName();
		boolean isNativeMethod = (lineNumber == -2);
		String declaringClass = stackTraceElement.getDeclaringClass();
		String methodName = stackTraceElement.getMethodName();
		
		return declaringClass + "." + methodName +
	            (isNativeMethod ? "(Native Method)" :
	             (fileName != null && lineNumber >= 0 ?
	              "(" + fileName + ":" + lineNumber + ")" :
	              (fileName != null ?  "("+fileName+")" : "(Unknown Source)")));
	}
	
	public static String toString(HotThreads hts) {
		if (hts == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append("Hot threads at ");
		sb.append(DateTools.encode(hts.getTimestamp(), DateTools.ISO8601_DATE_FORMAT));
		sb.append(", interval=");
		sb.append(hts.getIntervalInMs());
		sb.append(", busiestThreads=");
		sb.append(hts.getNoOfBusiestThreads());
		sb.append(", ignoreIdleThreads=");
		sb.append(hts.getIgnoreIdleThreads());
		sb.append(":\n");
		
		List<HotThread> hotThreadList = hts.getHotThreadList();
		if (hotThreadList != null) {
			for (HotThread ht : hotThreadList) {
				
				sb.append(String.format(Locale.ROOT, "%n%4.1f%% (%s micros out of %s ms) %s usage by thread '%s'%n", ht.getPercent(), TimeUnit.NANOSECONDS.toMicros(ht.getTimeInNanoSeconds()), hts.getIntervalInMs(), hts.getType(), ht.getThreadName()));
				
				List<StackTraceElement> stackTraceElements = ht.getStackTraceElements();
				if (stackTraceElements != null) {
					
					if (ht.getCount() == 1) {
						sb.append(String.format(Locale.ROOT, "  unique snapshot%n"));
					} else {
						sb.append(String.format(Locale.ROOT, "  %d/%d snapshots sharing following %d elements%n", ht.getCount(), hts.getThreadElementsSnapshotCount(), ht.getMaxSimilarity()));
					}
					for (StackTraceElement ste : stackTraceElements) {
						sb.append(String.format(Locale.ROOT, "    %s%n", PlatformReflectionTools.toString(ste)));
					}
				}
			}
		}
		
		return sb.toString();
	}
}
