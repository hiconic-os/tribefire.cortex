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
package com.braintribe.model.asset;

import java.util.List;

import com.braintribe.model.artifact.info.HasRepositoryOrigins;
import com.braintribe.model.asset.natures.PlatformAssetNature;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.HasMetaData;

@SelectiveInformation("${name}")
public interface PlatformAsset extends HasMetaData, HasRepositoryOrigins {
	
	EntityType<PlatformAsset> T = EntityTypes.T(PlatformAsset.class);
	
	public static final String name = "name";
	public static final String version = "version";
	public static final String resolvedRevision = "resolvedRevision";
	public static final String groupId = "groupId";
	public static final String assets = "assets";
	public static final String nature = "nature";
	public static final String natureDefinedAsPart = "natureDefinedAsPart";
	public static final String hasUnsavedChanges = "hasUnsavedChanges";
	public static final String platformProvided = "platformProvided";
	public static final String effective = "effective";
	
	void setName(String name);
	String getName();
	
	@Initializer("'1.0'")
	String getVersion();
	void setVersion(String version);
	
	void setResolvedRevision(String resolvedRevision);
	String getResolvedRevision();
	
	void setGroupId( String groupId);
	String getGroupId();

	List<PlatformAssetDependency> getQualifiedDependencies();
	void setQualifiedDependencies(List<PlatformAssetDependency> qualifiedDependencies);
	
	void setNature( PlatformAssetNature nature);
	PlatformAssetNature getNature();
	
	boolean getNatureDefinedAsPart();
	void setNatureDefinedAsPart(boolean natureDefinedAsPart);
	
	boolean getHasUnsavedChanges();
	void setHasUnsavedChanges(boolean hasUnsavedChanges);
	
	boolean getIsContextualized();
	void setIsContextualized(boolean isContextualized);
	
	boolean getPlatformProvided();
	void setPlatformProvided(boolean platformProvided);
	
	// TODO COREPA-367: Rework this initializer as qualified deps are not effective!
	@Initializer("true")
	boolean getEffective();
	void setEffective(boolean effective);
	
	default String versionlessName() {
		return getGroupId() + ":" + getName();
	}

	default String qualifiedAssetName() {
		return versionlessName() + "#" + getVersion();
	}

	default String qualifiedRevisionedAssetName() {
		String rev = getResolvedRevision();
		return qualifiedAssetName() + (rev == null ? "" : "." + rev);
	}

	default String versionWithRevision() {
		String rev = getResolvedRevision();
		return getVersion() + (rev == null ? "" : "." + rev);
	}
}
