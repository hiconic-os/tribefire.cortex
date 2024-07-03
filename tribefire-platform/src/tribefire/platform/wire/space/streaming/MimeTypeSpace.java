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
package tribefire.platform.wire.space.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.mimetype.MimeTypeDetector;
import com.braintribe.model.resource.api.MimeTypeRegistry;
import com.braintribe.utils.StringTools;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.platform.wire.space.MasterResourcesSpace;
import tribefire.platform.wire.space.common.ResourceProcessingSpace;

@Managed
public class MimeTypeSpace implements WireSpace {

	private final static Logger logger = Logger.getLogger(MimeTypeSpace.class);

	@Import
	private MasterResourcesSpace resources;

	@Import
	private ResourceProcessingSpace resourceProcessing;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		WireSpace.super.onLoaded(configuration);
		// Make sure that the mime type registry is prepared
		this.mimeTypeRegistry();
	}

	public MimeTypeDetector detector() {
		return resourceProcessing.mimeTypeDetector();
	}

	@Managed
	public MimeTypeRegistry mimeTypeRegistry() {
		MimeTypeRegistry bean = resourceProcessing.mimeTypeRegistry();

		// TODO 28.2.2023 This file also exists in platform-api under com/braintribe/mimetype/mime-extensions.properties
		List<String> lines = readMimeExtensionsProperties();


		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			if (StringTools.isBlank(line)) {
				continue;
			}
			int idx = line.indexOf(":");
			if (idx <= 0) {
				logger.debug("Could not understand line: " + line + " of mime-extensions.properties");
				continue;
			}
			String mimeType = line.substring(0, idx).trim();
			if (StringTools.isBlank(mimeType)) {
				logger.debug("Could not get Mime-Type from line: " + line + " of mime-extensions.properties");
				continue;
			}
			if (!mimeType.contains("/")) {
				logger.debug("Could not get valid Mime-Type from line: " + line + " of mime-extensions.properties");
				continue;
			}
			String extensions = line.substring(idx + 1).trim();
			if (extensions.equals("[]")) {
				continue;
			}
			if (!extensions.startsWith("[") && !extensions.endsWith("]")) {
				logger.debug("Could not understand extensions " + extensions + " of line: " + line + " of mime-extensions.properties");
				continue;
			}
			extensions = StringTools.removeFirstAndLastCharacter(extensions);
			String[] extArray = StringTools.splitCommaSeparatedString(extensions, true);
			if (extArray.length == 0) {
				logger.debug("Could not understand extensions " + extensions + " of line: " + line + " of mime-extensions.properties");
				continue;
			}
			for (String ext : extArray) {
				bean.registerMapping(mimeType, ext);
			}
		}

		return bean;
	}

	private List<String> readMimeExtensionsProperties() {
		try (InputStream in = getClass().getResource("mime-extensions.properties").openStream()) {
			return StringTools.readLinesFromInputStream(in, "UTF-8", false);

		} catch (IOException e) {
			throw new UncheckedIOException("Error while reading mime-extensions.properties", e);
		}
	}

}
