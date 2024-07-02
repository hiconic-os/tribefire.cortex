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
package com.braintribe.model.processing.platformreflection.processor;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.session.InputStreamProvider;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.StringTools;
import com.braintribe.utils.date.NanoClock;
import com.braintribe.utils.stream.AutoCloseInputStream;
import com.braintribe.utils.stream.api.StreamPipe;
import com.braintribe.utils.stream.api.StreamPipeFactory;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.InternalZipConstants;

public class ResourceBasedEncryptedZippingInputStreamProvider implements InputStreamProvider {

	private static Logger logger = Logger.getLogger(ResourceBasedEncryptedZippingInputStreamProvider.class);

	private Collection<Resource> resources;
	private String folderName = null;
	private char[] password = null;

	private StreamPipe pipe;

	public ResourceBasedEncryptedZippingInputStreamProvider(StreamPipeFactory streamPipeFactory, String name, Collection<Resource> resources,
			String password) throws IOException {
		if (!StringTools.isBlank(password)) {
			this.password = password.toCharArray();
		}
		this.resources = resources;
		this.pipe = streamPipeFactory.newPipe(name);
		this.doZip();
	}

	private void doZip() throws IOException {
		Instant start = NanoClock.INSTANCE.instant();
		try (ZipOutputStream out = new ZipOutputStream(pipe.openOutputStream(), this.password, InternalZipConstants.CHARSET_UTF_8)) {

			ZipParameters zipParameters = new ZipParameters();
			if (password != null) {
				zipParameters.setEncryptFiles(true);
				zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
			}
			for (Resource resource : resources) {
				String name = resource.getName();

				try (InputStream in = resource.openStream()) {
					String entryName = folderName != null ? folderName + "/" + name : name;
					zipParameters.setFileNameInZip(entryName);
					out.putNextEntry(zipParameters);
					IOTools.transferBytes(in, out, IOTools.BUFFER_SUPPLIER_64K);
				} finally {
					out.closeEntry();
				}
			}

		}
		logger.debug(() -> "Zipping " + resources.size() + " files took: " + StringTools.prettyPrintDuration(start, true, ChronoUnit.MILLIS));
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return new AutoCloseInputStream(pipe.openInputStream());
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
