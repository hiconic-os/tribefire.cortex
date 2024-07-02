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
package com.braintribe.model.processing.exchange.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.braintribe.model.exchange.ExchangePackage;

public interface Utils {
	
	public static final String encodedAssemblyName = "assembly";
	public static final String timestampFormat = "yyyyMMddHHmmssSSSS";
	public static final String exchangePackageDefaultName = "exchange-package";
	public static final String exchangePackageSuffix = ".tfx";
	
	public static String buildBaseName(ExchangePackage exchangePackage) {
		String name = exchangePackage.getName();
		if (name == null) {
			name = exchangePackageDefaultName;
		}
		String user = exchangePackage.getExportedBy();
		Date timestamp = exchangePackage.getExported();
		String timestampString = new SimpleDateFormat(timestampFormat).format(timestamp);
		return name + "-" + user + "-" +timestampString+exchangePackageSuffix;
	}
	
	public static String buildResourceName(String baseName, String extension) {
		return buildResourceName(baseName, extension, Utils.encodedAssemblyName);
	}
	
	public static String buildResourceName(String baseName, String extension, String defaultBaseName) {
		String resourceName = baseName;
		if (resourceName == null) {
			resourceName = defaultBaseName;
		}
		return Utils.addExtension(resourceName, extension);
	}

	
	public static String addExtension(String resourceName, String extension) {
		if (!resourceName.endsWith(".")) {
			resourceName += ".";
		}
		return resourceName+extension;
	}
	
}
