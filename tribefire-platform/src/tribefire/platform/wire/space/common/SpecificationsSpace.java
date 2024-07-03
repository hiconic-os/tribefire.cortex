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
package tribefire.platform.wire.space.common;
// ============================================================================

// BRAINTRIBE TECHNOLOGY GMBH - www.braintribe.com
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2018 - All Rights Reserved
// It is strictly forbidden to copy, modify, distribute or use this code without written permission
// To this file the Braintribe License Agreement applies.
// ============================================================================

import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.map;

import com.braintribe.model.processing.resource.enrichment.DelegatingSpecificationDetector;
import com.braintribe.model.processing.resource.enrichment.ImageSpecificationDetector;
import com.braintribe.model.processing.resource.enrichment.PdfSpecificationDetector;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class SpecificationsSpace implements WireSpace {

	@Managed
	public DelegatingSpecificationDetector standardSpecificationDetector() {
		DelegatingSpecificationDetector bean = new DelegatingSpecificationDetector();

		bean.setDetectorMap(map( //
				entry("image/png", imageSpecificationDetector()), //
				entry("image/jpeg", imageSpecificationDetector()), //
				entry("image/bmp", imageSpecificationDetector()), //
				entry("image/gif", imageSpecificationDetector()), //
				entry("image/pjpeg", imageSpecificationDetector()), //
				entry("image/tiff", imageSpecificationDetector()), //
				entry("application/pdf", pdfSpecificationDetector()) //
		));

		return bean;
	}

	@Managed
	public ImageSpecificationDetector imageSpecificationDetector() {
		ImageSpecificationDetector bean = new ImageSpecificationDetector();
		return bean;
	}

	@Managed
	public PdfSpecificationDetector pdfSpecificationDetector() {
		PdfSpecificationDetector bean = new PdfSpecificationDetector();
		return bean;
	}

}
