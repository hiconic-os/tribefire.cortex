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
package com.braintribe.web.api;

import java.io.File;
import java.util.function.Supplier;

public class RealPathSupplier implements Supplier<File> {
	
	public static final RealPathSupplier INSTANCE = new RealPathSupplier();
	
	private RealPathSupplier() {
	}

	@Override
	public File get() {
		String realPath = WebApps.servletContext().getRealPath("/");
		if (realPath != null)
			return new File(realPath);
		else
			throw new WebAppException("The real path could not be resolved");
	}
	
}
