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
package com.braintribe.model.platformreflection;

import java.util.List;

import com.braintribe.model.asset.PlatformAsset;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.packaging.Packaging;
import com.braintribe.model.platformreflection.request.PlatformReflectionResponse;
import com.braintribe.model.platformreflection.tf.ClasspathContainer;

public interface PackagingInformation extends PlatformReflectionResponse {

	EntityType<PackagingInformation> T = EntityTypes.T(PackagingInformation.class);

	void setPackaging(Packaging packaging);
	Packaging getPackaging();

	void setPlatformAsset(PlatformAsset setupAsset);
	PlatformAsset getPlatformAsset();

	List<ClasspathContainer> getClasspathContainers();
	void setClasspathContainers(List<ClasspathContainer> classpathContainers);
}
