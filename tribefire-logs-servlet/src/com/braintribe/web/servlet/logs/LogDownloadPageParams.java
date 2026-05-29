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
package com.braintribe.web.servlet.logs;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Encapsulates the page-level parameters for the log download page. These parameters control the current view state
 * (grouping mode, date filters) and are preserved across interactions such as downloading selected files.
 */
/* package */ class LogDownloadPageParams {

	public static final String PARAM_GROUP_BY = "groupBy";
	public static final String PARAM_FROM = "from";
	public static final String PARAM_TO = "to";
	public static final String PARAM_SELECTED_FILES = "selectedFiles";

	public static final String GROUP_BY_NODE = "node";
	public static final String GROUP_BY_DATE = "date";

	public final String groupBy;
	public final String from;
	public final String to;
	public final Set<String> selectedFiles;

	private LogDownloadPageParams(String groupBy, String from, String to, Set<String> selectedFiles) {
		this.groupBy = groupBy;
		this.from = from;
		this.to = to;
		this.selectedFiles = Collections.unmodifiableSet(selectedFiles);
	}

	/** Parses page parameters from the given HTTP request. Missing or blank values default to sensible values. */
	public static LogDownloadPageParams parse(HttpServletRequest req) {
		String groupBy = normalizeGroupBy(req.getParameter(PARAM_GROUP_BY));
		String from = blankToNull(req.getParameter(PARAM_FROM));
		String to = blankToNull(req.getParameter(PARAM_TO));
		Set<String> selectedFiles = parseSelectedFiles(req.getParameterValues(PARAM_SELECTED_FILES));
		return new LogDownloadPageParams(groupBy, from, to, selectedFiles);
	}

	private static String normalizeGroupBy(String value) {
		return GROUP_BY_DATE.equals(value) ? GROUP_BY_DATE : GROUP_BY_NODE;
	}

	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value;
	}

	private static Set<String> parseSelectedFiles(String[] values) {
		Set<String> result = new LinkedHashSet<>();
		if (values != null) {
			for (String v : values) {
				if (v != null && !v.isBlank())
					result.add(v);
			}
		}
		return result;
	}

	/**
	 * Appends the non-null parameters as query string parameters to the given base URL.
	 * Useful for building redirect URLs that preserve the current page state.
	 */
	public String appendToUrl(String baseUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append(LogsServletContext.appendParameter(baseUrl, asParameterMap()));

		for (String file : selectedFiles) {
			sb.append(sb.toString().contains("?") ? '&' : '?');
			sb.append(PARAM_SELECTED_FILES).append('=').append(urlEncode(file));
		}

		return sb.toString();
	}

	private Map<String, String> asParameterMap() {
		Map<String, String> params = new HashMap<>();
		if (groupBy != null) params.put(PARAM_GROUP_BY, groupBy);
		if (from != null) params.put(PARAM_FROM, from);
		if (to != null) params.put(PARAM_TO, to);
		return params;
	}

	private static String urlEncode(String value) {
		try {
			return java.net.URLEncoder.encode(value, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
