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
package com.braintribe.model.processing.deployment.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringVariableEvaluator {

	private static Pattern magicReplaceVariablesPattern = Pattern.compile("(\\{(.+?)(\\[(\\d+)(,(\\d+))?\\])?\\})");

	/**
	 * @see StringVariableEvaluator#magicReplaceVariables(String, Map, boolean)
	 */
	public static String magicReplaceVariables(String builder, Properties vars, boolean useEmptyValues)
			throws Exception {
		Map<String, String> tmpMap = new HashMap<String, String>();
		Set<Map.Entry<Object, Object>> entries = vars.entrySet();
		for (Iterator<Map.Entry<Object, Object>> it = entries.iterator(); it.hasNext();) {
			Map.Entry<Object, Object> entry = it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			tmpMap.put(key, value);
		}
		return StringVariableEvaluator.magicReplaceVariables(builder, tmpMap, useEmptyValues);
	}

	/**
	 * @see StringVariableEvaluator#magicReplaceVariables(String, Map, boolean)
	 */
	public static String[] magicReplaceVariables(String[] items, Map<String, String> map, boolean useEmptyValues)
			throws Exception {
		if ((items == null) || (items.length == 0)) {
			return items;
		}
		List<String> result = new ArrayList<String>();
		for (String item : items) {
			String resultingItems = StringVariableEvaluator.magicReplaceVariables(item, map, useEmptyValues);
			if ((resultingItems != null) && (resultingItems.trim().length() > 0)) {
				String[] resultingItemsArray = resultingItems.split(",");
				for (String resultItem : resultingItemsArray) {
					result.add(resultItem);
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Replace variables in string defined as <code>{var1[start,end]}</code> or <code>{var2[start]}</code> or <code>{var3}</code> 
	 * where {@code var1}, {@code var2}, {@code var3} must in the map {@code vars}
	 * <p>
	 * {@code start}, {@code end} means only string from {@code start} to {@code end} of 
	 * varXX is copied
	 * 
	 * @param builder
	 *            The template string containing variables.
	 * @param vars
	 *            A map of variables and their values.
	 * @param useEmptyValues
	 *            If set to true, non-defined variables are replaced by empty strings. If set to false and a non-defined
	 *            variable is referenced in the builder parameter, an exception is thrown.
	 * @return String with replaced variables
	 */
	public static String magicReplaceVariables(String builder, Map<String, String> vars, boolean useEmptyValues) throws Exception {

		Matcher m = StringVariableEvaluator.magicReplaceVariablesPattern.matcher(builder);
		StringBuffer result = new StringBuffer(); // We'll build the updated copy here
		while (m.find()) {
			String name = m.group(2).intern();
			String value = null;

			if (vars.containsKey(name)) {
				value = vars.get(name);
			}

			if (value == null) {
				if (useEmptyValues) {
					value = "";
				} else {
					throw new Exception("magicReplaceVariables: Variable [" + name + "] not found!");
				}
			}

			int start = 0;
			int stop = value.length();
			if (m.group(4) != null) {
				start = Integer.parseInt(m.group(4));
				if (m.group(6) != null) {
					stop = Integer.parseInt(m.group(6));
				}
				if (((start > 0) && (stop == 0)) || (stop < start) || (stop > value.length())) {
					stop = value.length();
				}
				if (start < value.length()) {
					value = value.substring(start, stop);
				}
			}
			m.appendReplacement(result, "");
			result.append(value);

		}
		m.appendTail(result);
		return result.toString();
	}

	/**
	 * Calls the method {@link #magicReplaceVariables(String, Map, boolean)} if the boolean parameter
	 * {@code useEmptyValues} set to false.
	 * 
	 * @return String with replaced variables
	 */
	public static String magicReplaceVariables(String builder, Map<String, String> vars) throws Exception {
		return magicReplaceVariables(builder, vars, false);
	}

}


