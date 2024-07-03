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
package tribefire.module.api;

import java.security.MessageDigest;

import javax.imageio.ImageIO;

import com.braintribe.mimetype.PlatformMimeTypeDetector;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.specification.RasterImageSpecification;

/**
 * Module-specific resolver of {@link Resource} meta-data.
 * 
 * @see #resolve(String)
 * 
 * @author peter.gazdik
 */
public interface ResourceMetaDataResolver {

	/**
	 * For give module-resource path (i.e. path to a file inside modules's 'resources' folder) resolves a {@link Resource} instance with following
	 * properties set.
	 * <ul>
	 * <li>name - based on File's name.
	 * <li>created - date resolved from the attributes of given file
	 * <li>creator - name of this module ("$groupId:$artifactId")
	 * <li>md5 - computed using {@link MessageDigest}
	 * <li>fileSize - straight forward
	 * <li>mimeType - detected using {@link PlatformMimeTypeDetector}
	 * <li>specification (optional) - for some mimeTypes (currently jpg, png, bmp, gif) computes a {@link RasterImageSpecification} using
	 * {@link ImageIO}.
	 * </ul>
	 */
	Resource resolve(String relativePath);

}
